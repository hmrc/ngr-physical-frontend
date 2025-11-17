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

import actions.*
import config.AppConfig
import forms.HaveYouChangedFormProvider
import models.HaveYouChangedControllerUse.{getMessageKeys, pageType}
import models.NavBarPageContents.createDefaultNavBar
import models.{AssessmentId, External, HaveYouChangedControllerUse, Internal, Mode, Space, UserAnswers}
import navigation.Navigator
import play.api.data.Form
import play.api.i18n.I18nSupport
import play.api.mvc.{Action, AnyContent, Call, MessagesControllerComponents}
import repositories.SessionRepository
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import views.html.HaveYouChangedView

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class HaveYouChangedController @Inject()(
                                          sessionRepository: SessionRepository,
                                          navigator: Navigator,
                                          identify: IdentifierAction,
                                          getData: DataRetrievalAction,
                                          requireData: DataRequiredAction,
                                          formProvider: HaveYouChangedFormProvider,
                                          val controllerComponents: MessagesControllerComponents,
                                          view: HaveYouChangedView
                                        )(implicit ec: ExecutionContext, appConfig: AppConfig) extends FrontendBaseController with I18nSupport {

  def loadSpace(mode: Mode, assessmentId: AssessmentId): Action[AnyContent] = onPageLoad(Space, mode, assessmentId)
  def loadInternal(mode: Mode, assessmentId: AssessmentId): Action[AnyContent] = onPageLoad(Internal, mode, assessmentId)
  def loadExternal(mode: Mode, assessmentId: AssessmentId): Action[AnyContent] = onPageLoad(External, mode, assessmentId)

  private def onPageLoad(use: HaveYouChangedControllerUse, mode: Mode, assessmentId: AssessmentId): Action[AnyContent] = (identify andThen getData andThen requireData) {
    implicit request =>
      val form: Form[Boolean] = formProvider(use)
      val preparedForm = request.userAnswers.get(pageType(use)) match {
        case None => form
        case Some(value) => form.fill(value)
      }
      val (title, hint) = getMessageKeys(use)
      Ok(view(request.property.addressFull, title, hint, preparedForm, use, mode, submitAction(use, mode, assessmentId), createDefaultNavBar()))
  }
  
  def submitAction(use: HaveYouChangedControllerUse, mode: Mode, assessmentId: AssessmentId): Call = use match {
    case Space => routes.HaveYouChangedController.submitSpace(mode, assessmentId)
    case Internal => routes.HaveYouChangedController.submitInternal(mode, assessmentId)
    case External => routes.HaveYouChangedController.submitExternal(mode, assessmentId)
  }

  def submitSpace(mode: Mode,assessmentId: AssessmentId): Action[AnyContent] = onSubmit(Space, mode, assessmentId)
  def submitInternal(mode: Mode, assessmentId: AssessmentId): Action[AnyContent] = onSubmit(Internal, mode, assessmentId)
  def submitExternal(mode: Mode, assessmentId: AssessmentId): Action[AnyContent] = onSubmit(External, mode, assessmentId)

  private def onSubmit(use: HaveYouChangedControllerUse, mode: Mode, assessmentId: AssessmentId): Action[AnyContent] = (identify andThen getData andThen requireData).async {
    implicit request =>
      val form: Form[Boolean] = formProvider(use)
      form.bindFromRequest().fold(
        formWithErrors =>
          val (title, hint) = getMessageKeys(use)
          Future.successful(BadRequest(view(request.property.addressFull, title, hint, formWithErrors, use, mode, submitAction(use, mode, assessmentId), createDefaultNavBar()))),
        value =>
          val page = pageType(use)
          for {
            updatedAnswers <- Future.fromTry(request.userAnswers.set(page, value))
            _ <- sessionRepository.set(updatedAnswers)
          } yield Redirect(navigator.nextPage(page, mode, updatedAnswers, assessmentId))
      )
  }
  
}
