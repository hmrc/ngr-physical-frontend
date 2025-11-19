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
import pages.ChangeToUseOfSpacePage
import play.api.i18n.Messages
import play.twirl.api.HtmlFormat
import uk.gov.hmrc.govukfrontend.views.viewmodels.content.HtmlContent
import uk.gov.hmrc.govukfrontend.views.viewmodels.summarylist.{SummaryListRow, Value}
import viewmodels.govuk.summarylist.*
import viewmodels.implicits.*

object ChangeToUseOfSpaceSummary  {

  def rows(answers: UserAnswers, assessmentId: AssessmentId)(implicit messages: Messages): Option[Seq[SummaryListRow]] =
    answers.get(ChangeToUseOfSpacePage(assessmentId)).map {
      answers =>

        val value = ValueViewModel(
          HtmlContent(
            answers.selectUseOfSpace.map {
                answer => HtmlFormat.escape(messages(s"changeToUseOfSpace.$answer")).toString
              }
              .mkString(",<br>")
          )
        )

        val booleanValue: String = if (answers.hasPlanningPermission) "site.yes" else "site.no"

        Seq(
          Some(SummaryListRowViewModel(
            key = "changeToUseOfSpace.useOfSpace.h2",
            value = value,
            actions = Seq(
              ActionItemViewModel("site.change", routes.ChangeToUseOfSpaceController.onPageLoad(CheckMode, assessmentId).url)
                .withVisuallyHiddenText(messages("changeToUseOfSpace.useOfSpace.h2"))
            )
          )),
          Some(SummaryListRowViewModel(
            key = "changeToUseOfSpace.permission.h2",
            value = ValueViewModel(messages(booleanValue)),
            actions = Seq(
              ActionItemViewModel("site.change", routes.ChangeToUseOfSpaceController.onPageLoad(CheckMode, assessmentId).url)
                .withVisuallyHiddenText(messages("changeToUseOfSpace.permission.h2"))
            )
          )),
          answers.permissionReference map {referenceValue =>
            SummaryListRowViewModel(
              key = "changeToUseOfSpace.permissionReference",
              value = ValueViewModel(HtmlContent(referenceValue)),
              actions = Seq(
                ActionItemViewModel("site.change", routes.ChangeToUseOfSpaceController.onPageLoad(CheckMode, assessmentId).url)
                  .withVisuallyHiddenText(messages("changeToUseOfSpace.permissionReference"))
              )
            )
          }
        ).flatten
    }

}
