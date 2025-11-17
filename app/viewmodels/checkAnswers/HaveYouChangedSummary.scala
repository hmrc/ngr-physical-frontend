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
import models.{AssessmentId, CheckMode, External, HaveYouChangedControllerUse, Internal, Space, UserAnswers}
import pages.{HaveYouChangedExternalPage, HaveYouChangedInternalPage, HaveYouChangedSpacePage, QuestionPage}
import play.api.i18n.Messages
import uk.gov.hmrc.govukfrontend.views.viewmodels.summarylist.SummaryListRow
import viewmodels.govuk.summarylist.*
import viewmodels.implicits.*

object HaveYouChangedSummary  {
  def row(answers: UserAnswers, use: HaveYouChangedControllerUse, assessmentId: AssessmentId)(implicit messages: Messages): Option[SummaryListRow] = {

    val (page: QuestionPage[Boolean], key: String) = use match {
      case Space => (HaveYouChangedSpacePage, "haveYouChangedSpace")
      case Internal => (HaveYouChangedInternalPage, "haveYouChangedInternal")
      case External => (HaveYouChangedExternalPage, "haveYouChangedExternal")
    }

    answers.get(page).map {
      answer =>
        val value = if (answer) "site.yes" else "site.no"
        val url = use match {
          case Space => routes.HaveYouChangedController.loadSpace(CheckMode, assessmentId).url
          case Internal => routes.HaveYouChangedController.loadInternal(CheckMode, assessmentId).url
          case External => routes.HaveYouChangedController.loadExternal(CheckMode, assessmentId).url
        }
        SummaryListRowViewModel(
          key     = s"$key.title",
          value   = ValueViewModel(value),
          actions = Seq(
            ActionItemViewModel("site.change", url)
              .withVisuallyHiddenText(messages(s"$key.title"))
          )
        )
    }
  }
}
