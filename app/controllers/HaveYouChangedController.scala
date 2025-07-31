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

import javax.inject.Inject
import models.{Mode, UserAnswers}
import navigation.Navigator
import pages.{HaveYouChangedExternalPage, HaveYouChangedInternalPage, HaveYouChangedSpacePage, QuestionPage}
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import repositories.SessionRepository
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import views.html.HaveYouChangedView
import config.AppConfig
import scala.concurrent.{ExecutionContext, Future}
import connectors.NGRConnector
import models.NavBarPageContents.createDefaultNavBar
import models.registration.CredId
import uk.gov.hmrc.http.NotFoundException

class HaveYouChangedController @Inject()(
                                         override val messagesApi: MessagesApi,
                                         sessionRepository: SessionRepository,
                                         navigator: Navigator,
                                         identify: IdentifierAction,
                                         getData: DataRetrievalAction,
                                         formProvider: HaveYouChangedFormProvider,
                                         ngrConnector: NGRConnector,
                                         val controllerComponents: MessagesControllerComponents,
                                         view: HaveYouChangedView
                                 )(implicit ec: ExecutionContext, appConfig: AppConfig) extends FrontendBaseController with I18nSupport {

  val form = formProvider()

  def onPageLoad(use: String, mode: Mode): Action[AnyContent] = (identify andThen getData).async {
    implicit request =>

      val credId = request.userId
      ngrConnector.getLinkedProperty(CredId(credId)).flatMap {
        case Some(property) => {
          val preparedForm = request.userAnswers.getOrElse(UserAnswers(request.userId)).get(pageType(use)) match {
            case None => form
            case Some(value) => form.fill(value)
          }
          val (title, hint) = getMessageKeys(use)
          Future.successful(Ok(view(property.addressFull, title, hint, preparedForm, use, mode, createDefaultNavBar())))
        }
        case None => throw new NotFoundException("Property not found")
      }
  }

  def getMessageKeys(use: String): (String, String) =
    use match {
      case "space" => ("haveYouChangedSpace.title", "haveYouChangedSpace.hint")
      case "internal" => ("haveYouChangedInternal.title", "haveYouChangedInternal.hint")
      case "external" => ("haveYouChangedExternal.title", "haveYouChangedExternal.hint")
    }

  private def pageType(use: String): QuestionPage[Boolean] =
    use match {
      case "space" => HaveYouChangedSpacePage
      case "internal" => HaveYouChangedInternalPage
      case "external" => HaveYouChangedExternalPage
    }

  def onSubmit(use: String, mode: Mode): Action[AnyContent] = (identify andThen getData).async {
    implicit request =>
      form.bindFromRequest().fold(
        formWithErrors =>
          Future.successful(BadRequest(view("address", "", "", formWithErrors, use, mode, createDefaultNavBar()))),

        value =>
          val page = pageType(use)
          for {
            updatedAnswers <- Future.fromTry(request.userAnswers.getOrElse(UserAnswers(request.userId)).set(page, value))
            _              <- sessionRepository.set(updatedAnswers)
          } yield Redirect(navigator.nextPage(page, mode, updatedAnswers))
      )
  }
}
