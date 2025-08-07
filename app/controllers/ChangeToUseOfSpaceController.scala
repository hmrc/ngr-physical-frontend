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
import forms.ChangeToUseOfSpaceFormProvider
import models.{ChangeToUseOfSpace, Mode, UserAnswers}
import navigation.Navigator
import pages.ChangeToUseOfSpacePage
import play.api.data.Form
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import repositories.SessionRepository
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import views.html.ChangeToUseOfSpaceView
import models.NavBarPageContents.createDefaultNavBar

import javax.inject.{Singleton, Inject}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ChangeToUseOfSpaceController @Inject()(
                                      sessionRepository: SessionRepository,
                                      navigator: Navigator,
                                      identify: IdentifierAction,
                                      getData: DataRetrievalAction,
                                      formProvider: ChangeToUseOfSpaceFormProvider,
                                      val controllerComponents: MessagesControllerComponents,
                                      view: ChangeToUseOfSpaceView
                                     )(implicit ec: ExecutionContext, appConfig: AppConfig) extends FrontendBaseController with I18nSupport {

  val form: Form[ChangeToUseOfSpace] = formProvider()

  //TODO: add requireData along with getData in onPageLoad and onSubmit methods when first page is implemented
  def onPageLoad(mode: Mode): Action[AnyContent] = (identify andThen getData) {
    implicit request =>
      val preparedForm = request.userAnswers.getOrElse(UserAnswers(request.userId)).get(ChangeToUseOfSpacePage) match {
        case None => form
        case Some(value) => form.fill(value)
      }

      Ok(view(request.property.addressFull, createDefaultNavBar(), preparedForm, mode))
  }

  def onSubmit(mode: Mode): Action[AnyContent] = (identify andThen getData).async {
    implicit request =>
      form.bindFromRequest()(request.request).fold(
        formWithErrors =>
          Future.successful(BadRequest(view(request.property.addressFull, createDefaultNavBar(), formWithErrors, mode))),
        value =>
          for {
            updatedAnswers <- Future.fromTry(request.userAnswers.getOrElse(UserAnswers(request.userId)).set(ChangeToUseOfSpacePage, value))
            _              <- sessionRepository.set(updatedAnswers)
          } yield Redirect(navigator.nextPage(ChangeToUseOfSpacePage, mode, updatedAnswers))
      )
  }
}
