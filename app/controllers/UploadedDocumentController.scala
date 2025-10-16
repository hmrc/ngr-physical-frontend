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
import models.NavBarPageContents.createDefaultNavBar
import models.UserAnswers
import models.registration.CredId
import models.requests.{DataRequest, OptionalDataRequest}
import models.upscan.UploadStatus.{InProgress, UploadedSuccessfully}
import models.upscan.{UploadId, UploadStatus}
import pages.UploadDocumentsPage
import play.api.i18n.{I18nSupport, Messages}
import play.api.mvc.{Action, AnyContent, Call, MessagesControllerComponents}
import repositories.SessionRepository
import services.UploadProgressTracker
import uk.gov.hmrc.govukfrontend.views.Aliases.Value
import uk.gov.hmrc.govukfrontend.views.viewmodels.content.HtmlContent
import uk.gov.hmrc.govukfrontend.views.viewmodels.summarylist.{Key, SummaryList, SummaryListRow}
import uk.gov.hmrc.http.NotFoundException
import uk.gov.hmrc.play.bootstrap.frontend.controller.{FrontendBaseController, FrontendController}
import viewmodels.govuk.all.{ActionItemViewModel, SummaryListRowViewModel}
import viewmodels.govuk.summarylist.*
import viewmodels.implicits.*
import views.html
import views.html.UploadedDocumentView

import javax.inject.{Inject, Singleton}
import scala.:+
import scala.concurrent.{ExecutionContext, Future, blocking}

@Singleton
class UploadedDocumentController @Inject()(uploadProgressTracker: UploadProgressTracker,
                                           view: UploadedDocumentView,
                                           identify: IdentifierAction,
                                           requireData: DataRequiredAction,
                                           getData: DataRetrievalAction,
                                           sessionRepository: SessionRepository,
                                           val controllerComponents: MessagesControllerComponents)(implicit appConfig: AppConfig, ec: ExecutionContext)
  extends FrontendBaseController with I18nSupport {

  private def showUploadProgress(allUploadStatus: Map[String, UploadStatus])(implicit messages: Messages): SummaryList = {
    SummaryList(allUploadStatus.map { case (uploadId, uploadStatus) =>
      createRow(uploadId, uploadStatus)
    }.toSeq)
  }

  private def createRow(uploadId: String, uploadStatus: UploadStatus)(implicit messages: Messages): SummaryListRow = {
    uploadStatus match
      case UploadStatus.UploadedSuccessfully(name, mimeType, downloadUrl, size) =>
            SummaryListRowViewModel(
              key = name,
              value = Value(
                HtmlContent(s"""<span class="govuk-tag govuk-tag--green govuk-!-margin-0">${messages("uploadedDocument.uploaded").mkString} </span> """)
              ).withCssClass("govuk-!-text-align-right"),
              actions = Seq(ActionItemViewModel(messages("site.remove"), routes.RemoveFileController.onPageLoad(uploadId).url))
            )
      case UploadStatus.InProgress =>
          SummaryListRowViewModel(
            key = "",
            value = ValueViewModel(
              HtmlContent(s"""<span class="govuk-tag govuk-tag--yellow govuk-!-margin-0">${messages("uploadedDocument.uploading").mkString} </span> """)
            ).withCssClass("govuk-!-text-align-right"),
            actions = Seq(ActionItemViewModel(messages("site.remove"), routes.RemoveFileController.onPageLoad(uploadId).url))
      )
      case UploadStatus.Failed =>
        SummaryListRowViewModel(
          key = "",
          value =  ValueViewModel(
            HtmlContent(s"""<span class="govuk-tag govuk-tag--red govuk-!-margin-0">${messages("uploadedDocument.failed").mkString} </span> """)
          ).withCssClass("govuk-!-text-align-right"),
          actions = Seq(ActionItemViewModel(messages("site.remove"), routes.RemoveFileController.onPageLoad(uploadId).url).withCssClass("govuk-grid-column-one-quarter"))
      )
  }

  private def containsInProgress(allUploadStatus: Seq[UploadStatus]): Boolean = {
    allUploadStatus.contains(UploadStatus.InProgress)
  }



  def show(uploadId: Option[UploadId]): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>

    val currentAnswers: Seq[String] = (request.userAnswers.get(UploadDocumentsPage), uploadId) match {
      case (None, None) => Seq.empty[String]
      case (None, Some(newUploadId)) => Seq(newUploadId.value)
      case (Some(value), Some(newUploadId)) if value.contains(newUploadId.value) => value
      case (Some(value), Some(newUploadId)) => value :+ newUploadId.value
      case (Some(value), None) => value
    }

    if (currentAnswers.isEmpty) {
      Future.successful(Redirect(routes.UploadDocumentController.onPageLoad(None)))
    } else {
      request.userAnswers.set(UploadDocumentsPage, currentAnswers).map {
        updatedAnswers => sessionRepository.set(updatedAnswers)
      }

      val uploadStatusCalls: Seq[Future[Option[(String, UploadStatus)]]] =
        currentAnswers.map { uploadId =>
          uploadProgressTracker.getUploadResult(UploadId(uploadId)).map {
            _.map(status => uploadId -> status)
          }
        }

      Future.sequence(uploadStatusCalls).map(_.flatten.toMap).map { uploadStatuses =>
        val inProgress = containsInProgress(uploadStatuses.values.toSeq)
        Ok(view(
          createDefaultNavBar,
          showUploadProgress(uploadStatuses),
          request.property.addressFull,
          inProgress,
          routes.UploadedDocumentController.onSubmit(uploadId, inProgress)
        ))
      }
    }
  }

  def onSubmit(uploadId: Option[UploadId], inProgress: Boolean): Action[AnyContent] = (identify andThen getData).async {
    implicit request =>
    if (inProgress)
      Future.successful(Redirect(routes.UploadedDocumentController.show(uploadId)))
    else Future.successful(Redirect(routes.CheckYourAnswersController.onPageLoad()))

  }
}
