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
import models.{AssessmentId, CheckMode, UserAnswers}
import pages.AnythingElsePage
import play.api.i18n.Messages
import uk.gov.hmrc.govukfrontend.views.viewmodels.summarylist.SummaryListRow
import viewmodels.govuk.summarylist.*
import viewmodels.implicits.*

object AnythingElseSummary {

  def rows(answers: UserAnswers, assessmentId: AssessmentId)(implicit messages: Messages): Option[Seq[SummaryListRow]] =
    answers.get(AnythingElsePage(assessmentId)).map {
      answer =>

        val value = if (answer.value) "site.yes" else "site.no"

        Seq(
          Some(SummaryListRowViewModel(
            key = "anythingElse.heading",
            value = ValueViewModel(value),
            actions = Seq(
              ActionItemViewModel("site.change", routes.AnythingElseController.onPageLoad(CheckMode, assessmentId).url)
                .withVisuallyHiddenText(messages("anythingElse.heading"))
            ))),
          answer.text map { info =>
            SummaryListRowViewModel(
              key = "anythingElse.inputTitle",
              value = ValueViewModel(info),
              actions = Seq(
                ActionItemViewModel("site.change", routes.AnythingElseController.onPageLoad(CheckMode, assessmentId).url)
                  .withVisuallyHiddenText(messages("anythingElse.inputTitle"))
              )
            )
          }
        ).flatten
    }
}
