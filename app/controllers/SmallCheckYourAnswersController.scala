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
import forms.SmallCheckYourAnswersFormProvider
import models.NavBarPageContents.createDefaultNavBar
import models.requests.OptionalDataRequest
import models.{CYAExternal, CYAInternal, CYAViewType, CheckMode, ExternalFeature, HowMuchOfProperty, InternalFeature, InternalFeatureGroup1, Mode, NormalMode, WhatHappenedTo}
import pages.{HaveYouChangedExternalPage, HaveYouChangedInternalPage, QuestionPage, SecurityCamerasChangePage}
import play.api.data.Form
import play.api.i18n.I18nSupport
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import repositories.SessionRepository
import uk.gov.hmrc.govukfrontend.views.viewmodels.summarylist.SummaryList
import uk.gov.hmrc.http.NotFoundException
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import viewmodels.govuk.all.SummaryListViewModel
import views.html.SmallCheckYourAnswersView

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class SmallCheckYourAnswersController @Inject()(identify: IdentifierAction,
                                                getData: DataRetrievalAction,
                                                sessionRepository: SessionRepository,
                                                formProvider: SmallCheckYourAnswersFormProvider,
                                                val controllerComponents: MessagesControllerComponents,
                                                view: SmallCheckYourAnswersView
                                               )(implicit appConfig: AppConfig, ec: ExecutionContext) extends FrontendBaseController with I18nSupport {

  private def getRows(viewType: CYAViewType, mode: Mode)(implicit request: OptionalDataRequest[AnyContent]): SummaryList = {
    viewType match {
      case CYAInternal => SummaryListViewModel(InternalFeature.getAnswers(request.userAnswers, mode))
      case CYAExternal => SummaryListViewModel(ExternalFeature.getAnswers(request.userAnswers, mode))
    }
  }

  def onPageLoad(viewType: CYAViewType, mode: Mode): Action[AnyContent] = (identify andThen getData) {
    implicit request =>
      val form: Form[Boolean] = formProvider(viewType)
      val rows = getRows(viewType, mode)
      Ok(view(viewType, request.property.addressFull, rows, form, createDefaultNavBar(), mode))
  }

  def onSubmit(viewType: CYAViewType, mode: Mode): Action[AnyContent] = (identify andThen getData).async {
    implicit request =>
      val form: Form[Boolean] = formProvider(viewType)
      
      form.bindFromRequest().fold(
        formWithErrors =>
          val rows = getRows(viewType, mode)
          Future.successful(BadRequest(view(viewType, request.property.addressFull, rows, formWithErrors, createDefaultNavBar(), mode))),
        {
          case true => viewType match {
            case CYAInternal => Future.successful(Redirect(routes.WhichInternalFeatureController.onPageLoad(mode)))
            case CYAExternal => Future.successful(Redirect(routes.WhichExternalFeatureController.onPageLoad(mode)))
          }
          case false => viewType match {
            case CYAInternal if mode == NormalMode => Future.successful(Redirect(routes.HaveYouChangedController.loadExternal(NormalMode)))
            case CYAExternal if mode == NormalMode => Future.successful(Redirect(routes.AnythingElseController.onPageLoad(NormalMode)))
            case _ => Future.successful(Redirect(routes.CheckYourAnswersController.onPageLoad()))
          }
        }
      )
  }

  def removeInternal(featureString: String, mode: Mode): Action[AnyContent] = (identify andThen getData).async {
    implicit request =>
      val feature = InternalFeature.withNameOption(featureString)
      val page: QuestionPage[?] = feature match {
        case Some(group1: InternalFeatureGroup1) => HowMuchOfProperty.page(group1)
        case Some(InternalFeature.SecurityCamera) => SecurityCamerasChangePage
        case None => throw new RuntimeException("no feature chosen to remove")
      }
      for {
        updatedAnswers <- Future.fromTry(request.userAnswers.getOrElse(throw new NotFoundException("User answers not found")).remove(page))
        newUpdatedAnswers <- if(InternalFeature.getAnswers(Some(updatedAnswers), mode).isEmpty) Future.fromTry(updatedAnswers.set(HaveYouChangedInternalPage, false)) else Future.successful(updatedAnswers)
        _ <- sessionRepository.set(newUpdatedAnswers)
      } yield Redirect(
        if (mode == NormalMode) routes.SmallCheckYourAnswersController.onPageLoad(CYAInternal, mode)
        else routes.CheckYourAnswersController.onPageLoad()
      )

  }

  def removeExternal(featureString: String, mode: Mode): Action[AnyContent] = (identify andThen getData).async {
    implicit request =>
      val feature = ExternalFeature.withNameOption(featureString).getOrElse(throw new NotFoundException("feature not found"))
      val page: QuestionPage[?] = WhatHappenedTo.page(feature)
      for {
        updatedAnswers <- Future.fromTry(request.userAnswers.getOrElse(throw new NotFoundException("User answers not found")).remove(page))
        newUpdatedAnswers <- if(ExternalFeature.getAnswers(Some(updatedAnswers), mode).isEmpty) Future.fromTry(updatedAnswers.set(HaveYouChangedExternalPage, false)) else Future.successful(updatedAnswers)
        _ <- sessionRepository.set(newUpdatedAnswers)
      } yield Redirect(
        if (mode == NormalMode) routes.SmallCheckYourAnswersController.onPageLoad(CYAExternal, mode)
        else routes.CheckYourAnswersController.onPageLoad()
      )

  }


}
