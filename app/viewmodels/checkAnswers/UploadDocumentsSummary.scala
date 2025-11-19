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

package viewmodels.checkAnswers

import controllers.routes
import models.upscan.UploadStatus
import models.{AssessmentId, CheckMode, UserAnswers}
import pages.UploadDocumentsPage
import play.api.i18n.Messages
import services.UploadProgressTracker
import uk.gov.hmrc.govukfrontend.views.viewmodels.summarylist.{SummaryListRow, Value}
import viewmodels.govuk.summarylist.*
import viewmodels.implicits.*


object UploadDocumentsSummary  {
  def rows(uploadStatuses: Seq[UploadStatus], assessmentId: AssessmentId)(implicit messages: Messages): Option[Seq[SummaryListRow]] = {
    val documents = uploadStatuses.collect {
      case UploadStatus.UploadedSuccessfully(name, mimeType, downloadUrl, size) => SummaryListRowViewModel(
        key = name,
        value = Value(),
        actions = Seq(
          ActionItemViewModel("site.change", routes.UploadedDocumentController.show(None, assessmentId).url)
        )
      )
    }
    if (documents.nonEmpty) Some(documents) else None
  }
}
