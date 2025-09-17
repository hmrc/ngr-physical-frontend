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

import javax.inject.Inject
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import views.html.DeclarationView
import models.NavBarPageContents.createDefaultNavBar
import models.UserAnswers
import pages.{DeclarationPage, WhenCompleteChangePage}
import repositories.SessionRepository
import utils.UniqueIdGenerator

import scala.concurrent.{ExecutionContext, Future}

class DeclarationController @Inject()(
                                       val controllerComponents: MessagesControllerComponents,
                                       view: DeclarationView,
                                       authenticate: IdentifierAction,
                                       isRegisteredCheck: RegistrationAction,
                                       getData: DataRetrievalAction,
                                       requireData: DataRequiredAction,
                                       sessionRepository: SessionRepository
                                     )(implicit ec: ExecutionContext, appConfig: AppConfig)  extends FrontendBaseController with I18nSupport {

  val show: Action[AnyContent] =
    (authenticate andThen isRegisteredCheck andThen getData andThen requireData) {
      implicit request =>
        Ok(view(request.property.addressFull, createDefaultNavBar()))
    }

  val next: Action[AnyContent] =
    (authenticate andThen isRegisteredCheck andThen getData andThen requireData).async  {
      //TODO replace the current creating page object and storing number and  with adding with adding directly to model 
      // and then sending the completed PropertyChangesUserAnswers when backend code is complete
      implicit request =>
        request.userAnswers.get(DeclarationPage) match {
          case None => 
            val generateRef =  UniqueIdGenerator.generateId
            for {
              updatedAnswers <- Future.fromTry(request.userAnswers.set(DeclarationPage, generateRef)) 
              _ <- sessionRepository.set(updatedAnswers)
            } yield Redirect(routes.SubmissionConfirmationController.onPageLoad()) 
          case Some(value) => Future.successful(Redirect(routes.SubmissionConfirmationController.onPageLoad()))
        }
    }
}
