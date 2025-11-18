/*
 * Copyright 2023 HM Revenue & Customs
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

package utils

import models.upscan.UploadId
import models.{AssessmentId, CheckMode, External, ExternalFeature, HaveYouChangedControllerUse, Internal, InternalFeature, Space, UserAnswers}
import pages.UploadDocumentsPage
import play.api.i18n.Messages
import services.UploadProgressTracker
import uk.gov.hmrc.govukfrontend.views.viewmodels.summarylist.SummaryListRow
import viewmodels.Section
import viewmodels.checkAnswers.*
import viewmodels.govuk.summarylist.*

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class CheckYourAnswersHelper @Inject(uploadProgressTracker: UploadProgressTracker) {


  def createSectionList(userAnswers: UserAnswers, assessmentId: AssessmentId)(implicit messages: Messages, ec: ExecutionContext): Future[Seq[Section]] = {
    val staticSections: Seq[Option[Section]] = Seq(
      createDateOfChangeSection(userAnswers, assessmentId),
      createUseOfSpaceSection(userAnswers, assessmentId),
      createInternalFeaturesSection(userAnswers, assessmentId),
      createExternalFeaturesSection(userAnswers, assessmentId),
      createAdditionalInformationSection(userAnswers, assessmentId)
    )

    val supportingDocumentsFuture: Future[Option[Section]] = createSupportingDocuments(userAnswers, assessmentId)

    supportingDocumentsFuture.map { supportingDocumentsOpt =>
      (staticSections :+ supportingDocumentsOpt).flatten
    }
  }
  
  private def createDateOfChangeSection(userAnswers: UserAnswers, assessmentId: AssessmentId)(implicit
                                                                                              messages: Messages
  ): Option[Section] =
    buildSection(
      "checkYourAnswers.dateOfChange.heading",
      Seq(
        WhenCompleteChangeSummary.row(userAnswers, assessmentId),
      )
    )

  private def createUseOfSpaceSection(userAnswers: UserAnswers, assessmentId: AssessmentId)(implicit
                                                                  messages: Messages
  ): Option[Section] =

    buildSection(
      "checkYourAnswers.useOfSpace.heading",
      (HaveYouChangedSummary.row(userAnswers, Space, assessmentId).toSeq ++
        ChangeToUseOfSpaceSummary.rows(userAnswers, assessmentId).getOrElse(Seq.empty)).map(Some(_))
    )

  private def createInternalFeaturesSection(userAnswers: UserAnswers, assessmentId: AssessmentId)(implicit
    messages: Messages
  ): Option[Section] =
    val rowsSeq = InternalFeature.getAnswers(userAnswers, CheckMode, fromMiniCYA = false, assessmentId)
    val rows  = if (rowsSeq.nonEmpty) rowsSeq.map(Some(_)) else Seq(
      HaveYouChangedSummary.row(userAnswers, Internal, assessmentId)
    )

    buildSection(
      "checkYourAnswers.internalFeature.heading",
      rows
    )

  private def createExternalFeaturesSection(userAnswers: UserAnswers, assessmentId: AssessmentId)(implicit
                                                                      messages: Messages
  ): Option[Section] =

    val rowsSeq = ExternalFeature.getAnswers(userAnswers, CheckMode, fromMiniCYA = false, assessmentId)
    val rows  = if (rowsSeq.nonEmpty) rowsSeq.map(Some(_)) else Seq(
      HaveYouChangedSummary.row(userAnswers, External, assessmentId)
    )
    buildSection(
      "checkYourAnswers.externalFeature.heading",
      rows
    )

  private def createAdditionalInformationSection(userAnswers: UserAnswers, assessmentId: AssessmentId)(implicit
                                                                messages: Messages
  ): Option[Section] =

    buildSection(
      "checkYourAnswers.additionalInformation.heading",
      AnythingElseSummary.rows(userAnswers, assessmentId).getOrElse(Seq.empty).map(Some(_))
    )

  private def createSupportingDocuments(userAnswers: UserAnswers, assessmentId: AssessmentId)(implicit messages: Messages, ec: ExecutionContext
  ): Future[Option[Section]] =
    val uploadResults = userAnswers.get(UploadDocumentsPage(assessmentId)).map(value => value.map {
      id => uploadProgressTracker.getUploadResult(UploadId(id))
    })
    uploadResults match {
      case Some(futureUploadStatuses) =>
        for {
          uploadStatuses <- Future.sequence(futureUploadStatuses).map(_.flatten)
        } yield {
          buildSection(
            "checkYourAnswers.supportingDocuments.heading",
            UploadDocumentsSummary.rows(uploadStatuses, assessmentId).getOrElse(Seq.empty).map(Some(_))
          )
        }
      case None => Future.successful(None)
    }

  private def buildSection(heading: String, rows: Seq[Option[SummaryListRow]]): Option[Section] = {
    val nonEmptyRows: Seq[SummaryListRow] = rows.flatten
    if (nonEmptyRows.nonEmpty) Some(Section(Some(heading), SummaryListViewModel(nonEmptyRows).withCssClass("govuk-!-margin-bottom-9"))) else None
  }

}
