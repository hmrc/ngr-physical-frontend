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
import connectors.NGRNotifyConnector

import javax.inject.Inject
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import views.html.DeclarationView
import models.NavBarPageContents.createDefaultNavBar
import models.registration.CredId
import models.{AssessmentId, ExternalFeature, InternalFeature, NotifyPropertyChangeResponse, PropertyChangesUserAnswers, UserAnswers}
import pages.{AnythingElsePage, ChangeToUseOfSpacePage, DeclarationPage, HaveYouChangedExternalPage, HaveYouChangedInternalPage, UploadDocumentsPage, WhenCompleteChangePage}
import play.api.Logging
import play.api.libs.json.Json
import repositories.SessionRepository
import utils.UniqueIdGenerator

import java.time.LocalDate
import scala.concurrent.{ExecutionContext, Future}

class DeclarationController @Inject()(
                                       val controllerComponents: MessagesControllerComponents,
                                       view: DeclarationView,
                                       authenticate: IdentifierAction,
                                       getData: DataRetrievalAction,
                                       requireData: DataRequiredAction,
                                       sessionRepository: SessionRepository,
                                       connector: NGRNotifyConnector
                                     )(implicit ec: ExecutionContext, appConfig: AppConfig)  extends FrontendBaseController with I18nSupport with Logging {

  def show(assessmentId: AssessmentId): Action[AnyContent] =
    (authenticate andThen getData andThen requireData) {
      implicit request =>
        Ok(view(assessmentId, request.property.addressFull, createDefaultNavBar()))
    }

  def next(assessmentId: AssessmentId): Action[AnyContent] =
    (authenticate andThen getData andThen requireData).async  {
      implicit request =>

        request.userAnswers.get(WhenCompleteChangePage(assessmentId)) match {
          case Some(date) =>
            val userAnswers = PropertyChangesUserAnswers(
              CredId(request.credId),
              dateOfChange = date,
              useOfSpace = request.userAnswers.get(ChangeToUseOfSpacePage(assessmentId)),
              internalFeatures = InternalFeature.getAnswersToSend(request.userAnswers, assessmentId),
              externalFeatures = ExternalFeature.getAnswersToSend(request.userAnswers, assessmentId),
              additionalInfo = request.userAnswers.get(AnythingElsePage(assessmentId)),
              uploadedDocuments = Seq.empty[String] // TODO Implementation is pending for uploaded documents more tickets to follow
            )

            request.userAnswers.get(DeclarationPage(assessmentId)) match {
              case None =>
                val generateRef =  UniqueIdGenerator.generateId
                for {
                  updatedAnswers <- Future.fromTry(request.userAnswers.set(DeclarationPage(assessmentId), generateRef))
                  _ <- sessionRepository.set(updatedAnswers)
                  response <- connector.postPropertyChanges(userAnswers.copy(declarationRef = Some(generateRef)), assessmentId)
                } yield response.error match {
                  case None => Redirect(routes.SubmissionConfirmationController.onPageLoad(assessmentId))
                  case Some(e) =>
                    logger.error(s"[DeclarationController] error occurred: $e")
                    BadRequest
                }
              case Some(value) =>
                connector.postPropertyChanges(userAnswers.copy(declarationRef = Some(value)), assessmentId).map {
                  response => response.error match {
                    case None =>  Redirect(routes.SubmissionConfirmationController.onPageLoad(assessmentId))
                    case Some(e) => logger.error(s"[DeclarationController] error occurred: $e")
                      BadRequest
                  }
                }
            }

          case None => logger.error(s"[DeclarationController] missing date of completion")
            Future.successful(BadRequest)
        }
    }
}
