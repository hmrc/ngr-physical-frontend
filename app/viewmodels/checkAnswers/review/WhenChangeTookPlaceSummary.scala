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

package viewmodels.checkAnswers.review

import controllers.review.routes
import models.{AssessmentId, CheckMode, UserAnswers}
import pages.WhenCompleteChangePage
import pages.review.WhenChangeTookPlacePage
import play.api.i18n.{Lang, Messages}
import uk.gov.hmrc.govukfrontend.views.viewmodels.summarylist.SummaryListRow
import utils.DateTimeFormats.dateTimeFormat
import viewmodels.govuk.summarylist.*
import viewmodels.implicits.*

object WhenChangeTookPlaceSummary {

  def row(answers: UserAnswers, assessmentId: AssessmentId)(implicit messages: Messages): Option[SummaryListRow] =
    answers.get(WhenChangeTookPlacePage(assessmentId)).flatMap {
      answer =>
       if(answer.value) {
         implicit val lang: Lang = messages.lang
         answer.date.map { date =>
           SummaryListRowViewModel(
             key = "whenChangeTookPlace.date.heading",
             value = ValueViewModel(date.format(dateTimeFormat())),
             actions = Seq(
               ActionItemViewModel("site.change", routes.WhenChangeTookPlaceController.onPageLoad(CheckMode, assessmentId).url)
                 .withVisuallyHiddenText(messages("whenCompleteChange.change.hidden"))
             )
           )
         }
       } else {
          Some(
            SummaryListRowViewModel(
              key = "whenChangeTookPlace.heading",
              value = ValueViewModel(messages("site.no")),
              actions = Seq(
                ActionItemViewModel("site.change", routes.WhenChangeTookPlaceController.onPageLoad(CheckMode, assessmentId).url)
                  .withVisuallyHiddenText(messages("whenCompleteChange.change.hidden"))
              )
            )
          )
       }
    }
}
