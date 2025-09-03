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
import connectors.NGRConnector
import forms.{AnythingElseData, AnythingElseFormProvider}
import models.Mode
import navigation.Navigator
import pages.AnythingElsePage
import play.api.data.Form
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import repositories.SessionRepository
import uk.gov.hmrc.http.NotFoundException
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import views.html.AnythingElseView

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class AnythingElseController @Inject()(
                                        sessionRepository: SessionRepository,
                                        navigator: Navigator,
                                        identify: IdentifierAction,
                                        getData: DataRetrievalAction,
                                        formProvider: AnythingElseFormProvider,
                                        val controllerComponents: MessagesControllerComponents,
                                        view: AnythingElseView
                                      )(implicit ec: ExecutionContext, appConfig: AppConfig) extends FrontendBaseController with I18nSupport {

  val form: Form[AnythingElseData] = formProvider()

  def onPageLoad(mode: Mode): Action[AnyContent] = (identify andThen getData) {
    implicit request =>

      val preparedForm = request.userAnswers.getOrElse(throw new NotFoundException("answers not found")).get(AnythingElsePage) match {
        case None => form
        case Some(value) => form.fill(value)
      }

      Ok(view(request.property.addressFull, preparedForm, mode))
  }

  def onSubmit(mode: Mode): Action[AnyContent] = (identify andThen getData).async {
    implicit request =>

      form.bindFromRequest().fold(
        formWithErrors =>
          Future.successful(BadRequest(view(request.property.addressFull, formWithErrors, mode))),

        value =>
          for {
            updatedAnswers <- Future.fromTry(request.userAnswers.getOrElse(throw new NotFoundException("answers not found")).set(AnythingElsePage, value))
            _              <- sessionRepository.set(updatedAnswers)
          } yield Redirect(navigator.nextPage(AnythingElsePage, mode, updatedAnswers))
      )
  }
}
