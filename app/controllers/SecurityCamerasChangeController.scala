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
import forms.SecurityCamerasChangeFormProvider
import models.NavBarPageContents.createDefaultNavBar
import models.{Mode, UserAnswers}
import navigation.Navigator
import pages.SecurityCamerasChangePage
import play.api.data.Form
import play.api.i18n.I18nSupport
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import repositories.SessionRepository
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import views.html.SecurityCamerasChangeView

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class SecurityCamerasChangeController @Inject()(
                                        sessionRepository: SessionRepository,
                                        navigator: Navigator,
                                        identify: IdentifierAction,
                                        getData: DataRetrievalAction,
                                        requireData: DataRequiredAction,
                                        formProvider: SecurityCamerasChangeFormProvider,
                                        val controllerComponents: MessagesControllerComponents,
                                        view: SecurityCamerasChangeView
                                      )(implicit ec: ExecutionContext, appConfig: AppConfig) extends FrontendBaseController with I18nSupport {

  val form: Form[Int] = formProvider()

  def onPageLoad(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData) {
    implicit request =>

      val preparedForm = request.userAnswers.get(SecurityCamerasChangePage) match {
        case None => form
        case Some(value) => form.fill(value)
      }

      Ok(view(request.property.addressFull, preparedForm, mode, createDefaultNavBar()))
  }

  def onSubmit(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async {
    implicit request =>

      form.bindFromRequest().fold(
        formWithErrors =>
          Future.successful(BadRequest(view(request.property.addressFull, formWithErrors, mode, createDefaultNavBar()))),

        value =>
          for {
            updatedAnswers <- Future.fromTry(request.userAnswers.set(SecurityCamerasChangePage, value))
            _              <- sessionRepository.set(updatedAnswers)
          } yield Redirect(navigator.nextPage(SecurityCamerasChangePage, mode, updatedAnswers))
      )
  }
}
