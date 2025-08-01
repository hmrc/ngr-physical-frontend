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
import forms.HaveYouChangedFormProvider
import javax.inject.{Inject, Singleton}
import models.{HaveYouChangedControllerUse, Mode, UserAnswers}
import navigation.Navigator
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import repositories.SessionRepository
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import views.html.HaveYouChangedView
import config.AppConfig
import scala.concurrent.{ExecutionContext, Future}
import models.HaveYouChangedControllerUse.{getMessageKeys, pageType}
import models.NavBarPageContents.createDefaultNavBar
import play.api.data.Form

@Singleton
class HaveYouChangedController @Inject()(
                                          override val messagesApi: MessagesApi,
                                          sessionRepository: SessionRepository,
                                          navigator: Navigator,
                                          identify: IdentifierAction,
                                          getData: DataRetrievalAction,
                                          formProvider: HaveYouChangedFormProvider,
                                          val controllerComponents: MessagesControllerComponents,
                                          view: HaveYouChangedView
                                        )(implicit ec: ExecutionContext, appConfig: AppConfig) extends FrontendBaseController with I18nSupport {

  val form: Form[Boolean] = formProvider()

  def onPageLoad(use: HaveYouChangedControllerUse, mode: Mode): Action[AnyContent] = (identify andThen getData) {
    implicit request =>
      val preparedForm = request.userAnswers.getOrElse(UserAnswers(request.userId)).get(pageType(use)) match {
        case None => form
        case Some(value) => form.fill(value)
      }
      val (title, hint) = getMessageKeys(use)
      Ok(view(request.property.addressFull, title, hint, preparedForm, use, mode, createDefaultNavBar()))
  }

  def onSubmit(use: HaveYouChangedControllerUse, mode: Mode): Action[AnyContent] = (identify andThen getData).async {
    implicit request =>
      form.bindFromRequest().fold(
        formWithErrors =>
          val (title, hint) = getMessageKeys(use)
          Future.successful(BadRequest(view(request.property.addressFull, title, hint, formWithErrors, use, mode, createDefaultNavBar()))),
        value =>
          val page = pageType(use)
          for {
            updatedAnswers <- Future.fromTry(request.userAnswers.getOrElse(UserAnswers(request.userId)).set(page, value))
            _ <- sessionRepository.set(updatedAnswers)
          } yield Redirect(navigator.nextPage(page, mode, updatedAnswers))
      )
  }
}
