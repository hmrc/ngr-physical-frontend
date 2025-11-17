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
import forms.RemoveFileFormProvider
import models.NavBarPageContents.createDefaultNavBar
import models.upscan.{UploadId, UploadStatus}
import models.{AssessmentId, Mode}
import navigation.Navigator
import pages.{RemoveFilePage, UploadDocumentsPage}
import play.api.data.Form
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import repositories.SessionRepository
import services.UploadProgressTracker
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import views.html.RemoveFileView

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class RemoveFileController @Inject()(
                                      override val messagesApi: MessagesApi,
                                      identify: IdentifierAction,
                                      sessionRepository: SessionRepository,
                                      getData: DataRetrievalAction,
                                      requireData: DataRequiredAction,
                                      formProvider: RemoveFileFormProvider,
                                      uploadProgressTracker: UploadProgressTracker,
                                      val controllerComponents: MessagesControllerComponents,
                                      view: RemoveFileView
                                    )(implicit appConfig: AppConfig, executionContext: ExecutionContext) extends FrontendBaseController with I18nSupport {

  val form: Form[Boolean] = formProvider()

  def onPageLoad(uploadId: String, assessmentId: AssessmentId): Action[AnyContent] = (identify andThen getData andThen requireData).async {
    implicit request =>
      uploadProgressTracker.getUploadResult(UploadId(uploadId)).map {
        case Some(UploadStatus.UploadedSuccessfully(name, _, _, _)) =>
          Ok(view(form, assessmentId, request.property.addressFull, name, uploadId, createDefaultNavBar()))
        case _ =>
          Ok(view(form, assessmentId, request.property.addressFull, "", uploadId,createDefaultNavBar()))
      }
  }

  def onSubmit(uploadId: String, assessmentId: AssessmentId): Action[AnyContent] = (identify andThen getData andThen requireData).async {
    implicit request =>

      form.bindFromRequest().fold(
        formWithErrors =>
          uploadProgressTracker.getUploadResult(UploadId(uploadId)).map {
            case Some(UploadStatus.UploadedSuccessfully(name, _, _, _)) =>
              BadRequest(view(formWithErrors, assessmentId, request.property.addressFull, name, uploadId, createDefaultNavBar()))
            case _ =>
              BadRequest(view(formWithErrors, assessmentId, request.property.addressFull, "", uploadId, createDefaultNavBar()))
          },
        value =>
          if (value) {
            val updatedFiles = request.userAnswers
              .get(UploadDocumentsPage)
              .map(_.filterNot(_ == uploadId))
              .getOrElse(Seq.empty)
            request.userAnswers.set(UploadDocumentsPage, updatedFiles).map {
              updatedAnswers => sessionRepository.set(updatedAnswers)
            }
          }
          Future.successful(Redirect(routes.UploadedDocumentController.show(None, assessmentId)))
      )
  }
}
