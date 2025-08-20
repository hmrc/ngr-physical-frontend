/*
 * Copyright 2025 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package controllers

import actions.{DataRetrievalAction, IdentifierAction}
import config.AppConfig
import forms.InternalCheckYourAnswersFormProvider
import models.NavBarPageContents.createDefaultNavBar
import models.{HowMuchOfProperty, InternalFeature, InternalFeatureGroup1}
import pages.{QuestionPage, SecurityCamerasChangePage}
import play.api.data.Form
import play.api.i18n.I18nSupport
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import repositories.SessionRepository
import uk.gov.hmrc.http.NotFoundException
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import viewmodels.govuk.all.SummaryListViewModel
import views.html.SmallCheckYourAnswersView

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class SmallCheckYourAnswersController @Inject()(identify: IdentifierAction,
                                                getData: DataRetrievalAction,
                                                sessionRepository: SessionRepository,
                                                formProvider: InternalCheckYourAnswersFormProvider,
                                                val controllerComponents: MessagesControllerComponents,
                                                view: SmallCheckYourAnswersView
                                                  )(implicit appConfig: AppConfig, ec: ExecutionContext) extends FrontendBaseController with I18nSupport {
  val form: Form[Boolean] = formProvider()

  def onPageLoad: Action[AnyContent] = (identify andThen getData) {
    implicit request =>
      val rows = SummaryListViewModel(InternalFeature.getAnswers(sessionRepository))
      Ok(view(request.property.addressFull, rows, form, createDefaultNavBar()))
  }

  def onSubmit: Action[AnyContent] = (identify andThen getData).async {
    implicit request =>
      form.bindFromRequest().fold(
        formWithErrors =>
          val rows = SummaryListViewModel(InternalFeature.getAnswers(sessionRepository))
          Future.successful(BadRequest(view(request.property.addressFull, rows, formWithErrors, createDefaultNavBar()))),
        {
          case true => Future.successful(Redirect(routes.WhichInternalFeatureController.onPageLoad))
          case false => Future.successful(Redirect(routes.SmallCheckYourAnswersController.onPageLoad))
        }
      )
  }

  def remove(featureString: String): Action[AnyContent] = (identify andThen getData).async {
    implicit request =>
      val feature = InternalFeature.withNameOption(featureString)
      val page: QuestionPage[?] = feature match {
        case Some(group1: InternalFeatureGroup1) => HowMuchOfProperty.page(group1)
        case Some(InternalFeature.SecurityCamera) => SecurityCamerasChangePage
        case None => throw new RuntimeException("no feature chosen to remove")
      }
      for {
        updatedAnswers <- Future.fromTry(request.userAnswers.getOrElse(throw new NotFoundException("User answers not found")).remove(page))
        _ <- sessionRepository.set(updatedAnswers)
      } yield Redirect(routes.SmallCheckYourAnswersController.onPageLoad)

  }


}
