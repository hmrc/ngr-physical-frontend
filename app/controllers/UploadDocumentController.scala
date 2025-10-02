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


import config.AppConfig
import models.NavBarPageContents.createDefaultNavBar
import models.upscan.{Reference, UploadId}
import services.UploadProgressTracker
import connectors.UpscanConnector
import actions.*

import javax.inject.Inject
import play.api.i18n.{I18nSupport, Messages, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import views.html.UploadDocumentView
import models.registration.CredId
import forms.UploadForm
import pages.{SecurityCamerasChangePage, UploadDocumentsPage}
import repositories.SessionRepository
import models.UserAnswers
import viewmodels.govuk.all.ButtonViewModel

import scala.concurrent.{ExecutionContext, Future}

class UploadDocumentController @Inject()(
                                          identify: IdentifierAction,
                                          getData: DataRetrievalAction,
                                          requireData: DataRequiredAction,
                                          upScanConnector: UpscanConnector,
                                          uploadProgressTracker: UploadProgressTracker,
                                          uploadForm: UploadForm,
                                          val controllerComponents: MessagesControllerComponents,
                                          view: UploadDocumentView)(implicit appConfig: AppConfig, ec: ExecutionContext)  extends FrontendBaseController with I18nSupport {

  val attributes: Map[String, String] = Map(
    "accept" -> ".pdf,.png,.jpg,.jpeg",
    "data-max-file-size" -> "100000000",
    "data-min-file-size" -> "1000"
  )

  private def renderError(errorCode: Option[String])(implicit messages: Messages): Option[String] = {
    errorCode match {
      case Some("InvalidArgument") => Some(Messages("uploadDocument.error.required"))
      case Some("EntityTooLarge") => Some(Messages("uploadDocument.error.size"))
      case Some("EntityTooSmall") => Some(Messages("uploadDocument.error.emptyFile"))
      case Some("InvalidFileType") => Some(Messages("uploadDocument.error.format"))
      case Some("QUARANTINE") => Some(Messages("uploadDocument.error.virus"))
      case Some(reason) if reason.startsWith("UNKNOWN") => Some(Messages("uploadDocument.error.upscanUnknownError"))
      case Some(reason) => throw new RuntimeException(s"Error in errorToDisplay: unrecognisable error from upscan '$reason'")
      case None => None
    }
  }

    def onPageLoad(errorCode: Option[String]): Action[AnyContent] = (identify andThen getData andThen requireData).async {
      implicit request =>

        val errorToDisplay: Option[String] = renderError(errorCode)
        val uploadId = UploadId.generate()
        val successRedirectUrl = s"${appConfig.uploadRedirectTargetBase}${routes.UploadedDocumentController.show(Some(uploadId)).url}"
        val errorRedirectUrl = s"${appConfig.ngrPhysicalFrontendUrl}/supporting-document-upload"


        val currentAnswers = request.userAnswers.get(UploadDocumentsPage) match {
          case None => Seq.empty[String]
          case Some(value) => value
        }
        
        for
          upscanInitiateResponse <- upScanConnector.initiate(Some(successRedirectUrl), Some(errorRedirectUrl))
          _ <- uploadProgressTracker.requestUpload(uploadId, Reference(upscanInitiateResponse.fileReference.reference))
        yield Ok(
          view(
              uploadForm(),
              upscanInitiateResponse,
              errorToDisplay,
              attributes,
              request.property.addressFull,
              createDefaultNavBar(),
              !currentAnswers.isEmpty
            )
          )
    }

  def onCancel(uploadId: Option[UploadId]): Action[AnyContent] = (identify andThen getData).async {
    implicit request =>
      Future.successful(Redirect(routes.UploadedDocumentController.show(None)))
  }
}
