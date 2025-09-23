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

import play.api.i18n.{I18nSupport, Messages}
import play.api.mvc.{Action, AnyContent, Call, MessagesControllerComponents}
import uk.gov.hmrc.govukfrontend.views.viewmodels.summarylist.{Key, SummaryList, SummaryListRow}
import uk.gov.hmrc.http.NotFoundException
import actions.{AuthRetrievals, DataRequiredAction, DataRetrievalAction, IdentifierAction, RegistrationAction}
import config.AppConfig
import models.NavBarPageContents.createDefaultNavBar
import models.registration.CredId
import models.requests.OptionalDataRequest
import models.upscan.UploadStatus.{InProgress, UploadedSuccessfully}
import models.upscan.{UploadId, UploadStatus}
import pages.UploadDocumentsPage
import repositories.SessionRepository
import services.UploadProgressTracker
import uk.gov.hmrc.govukfrontend.views.Aliases.Value
import uk.gov.hmrc.govukfrontend.views.viewmodels.content.HtmlContent
import views.html
import views.html.UploadedDocumentView
import uk.gov.hmrc.play.bootstrap.frontend.controller.{FrontendBaseController, FrontendController}
import viewmodels.govuk.all.{ActionItemViewModel, SummaryListRowViewModel}
import viewmodels.govuk.summarylist.*
import viewmodels.implicits.*
import models.UserAnswers

import javax.inject.{Inject, Singleton}
import scala.:+
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class UploadedDocumentController @Inject()(uploadProgressTracker: UploadProgressTracker,
                                           view: UploadedDocumentView,
                                           identify: IdentifierAction,
                                           requireData: DataRequiredAction,
                                           getData: DataRetrievalAction,
                                           sessionRepository: SessionRepository,
                                           val controllerComponents: MessagesControllerComponents)(implicit appConfig: AppConfig, ec: ExecutionContext)
  extends FrontendBaseController with I18nSupport {

  def showUploadProgress(allUploadStatus: Seq[UploadStatus])(implicit messages: Messages): SummaryList = {
    SummaryList(allUploadStatus.map(uploadStatus => createRow(uploadStatus)))
  }

  def createRow(uploadStatus: UploadStatus)(implicit messages: Messages): SummaryListRow = {
    uploadStatus match
      case UploadStatus.UploadedSuccessfully(name, mimeType, downloadUrl, size) =>
            SummaryListRowViewModel(
              key = name,
              value = Value(
                HtmlContent(s"""<span class="govuk-tag govuk-tag--green govuk-!-margin-0">${messages("uploadedDocument.uploaded").mkString} </span> """)
              ).withCssClass("govuk-!-text-align-right"),
              actions = Seq(ActionItemViewModel(messages("site.remove"), ""))
            )
      case UploadStatus.InProgress =>
          SummaryListRowViewModel(
            key = "",
            value = ValueViewModel(
              HtmlContent(s"""<span class="govuk-tag govuk-tag--yellow govuk-!-margin-0">${messages("uploadedDocument.uploading").mkString} </span> """)
            ).withCssClass("govuk-!-text-align-right"),
            actions = Seq(ActionItemViewModel(messages("site.remove"), ""))
      )
      case UploadStatus.Failed =>
        SummaryListRowViewModel(
          key = "",
          value =  ValueViewModel(
            HtmlContent(s"""<span class="govuk-tag govuk-tag--red govuk-!-margin-0">${messages("uploadedDocument.failed").mkString} </span> """)
          ).withCssClass("govuk-!-text-align-right"),
          actions = Seq(ActionItemViewModel(messages("site.remove"), "").withCssClass("govuk-grid-column-one-quarter"))
      )
  }

  def containsInProgress(allUploadStatus: Seq[UploadStatus]): Boolean = {
    allUploadStatus.contains(UploadStatus.InProgress)
  }

  def show(uploadId: UploadId): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>

    val currentAnswers = request.userAnswers.get(UploadDocumentsPage) match {
      case None => Seq(uploadId.value)
      case Some(value) if value.contains(uploadId.value) => value
      case Some(value) => value :+ uploadId.value
    }

    request.userAnswers.set(UploadDocumentsPage, currentAnswers).map {
      updatedAnswers => sessionRepository.set(updatedAnswers)
    }

    val uploadStatusesCall: Seq[Future[Option[UploadStatus]]] = currentAnswers.map(value => uploadProgressTracker.getUploadResult(UploadId(value)))

    Future.sequence(uploadStatusesCall).map(_.flatten) map {
      UploadStatuses =>
        val inProgress = containsInProgress(UploadStatuses)

        Ok(view(
          createDefaultNavBar,
          showUploadProgress(UploadStatuses),
          request.property.addressFull,
          inProgress,
          routes.UploadedDocumentController.onSubmit(uploadId, inProgress),
        ))
      }

  }

  def onSubmit(uploadId: UploadId, inProgress: Boolean): Action[AnyContent] = (identify andThen getData).async {
    implicit request =>
    if (inProgress)
      Future.successful(Redirect(routes.UploadedDocumentController.show(uploadId)))
    else Future.successful(Redirect(routes.CheckYourAnswersController.onPageLoad()))

  }
}
