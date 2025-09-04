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

package utils

import base.SpecBase
import models.ChangeToUseOfSpace
import models.HowMuchOfProperty.{AllOf, SomeOf}
import models.UseOfSpaces.Rearrangedtheuseofspace
import models.WhatHappenedTo.{Added, RemovedSome}
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.funsuite.AnyFunSuiteLike
import org.scalatest.matchers.must.Matchers
import pages.{ChangeToUseOfSpacePage, HaveYouChangedSpacePage, HowMuchOfPropertyAirConPage, HowMuchOfPropertyEscalatorsPage, HowMuchOfPropertyGoodsLiftPage, WhatHappenedToLandHardSurfacedFencedPage, WhatHappenedToLandHardSurfacedOpenPage, WhenCompleteChangePage}
import play.api.i18n.Messages
import play.api.i18n.Messages.implicitMessagesProviderToMessages
import play.api.test.{FakeRequest, Helpers}
import uk.gov.hmrc.govukfrontend.views.Aliases.{SummaryList, Text, Value}
import uk.gov.hmrc.govukfrontend.views.viewmodels.summarylist.{ActionItem, Actions, Key, SummaryListRow}
import viewmodels.Section

import java.time.LocalDate

class CheckYourAnswersHelperSpec extends SpecBase with Matchers {

  implicit val messages: Messages = Helpers.stubMessagesApi().preferred(FakeRequest())
  "CheckYourAnswersHelper" - {

    "must display completeDate and useOfSpace details on the page" in {
      val helper = new CheckYourAnswersHelper()
      val userAnswers = emptyUserAnswers.set(WhenCompleteChangePage, LocalDate.of(20, 2, 2)).success.value
        .set(HaveYouChangedSpacePage, true).success.value
        .set(ChangeToUseOfSpacePage,  ChangeToUseOfSpace(Set(Rearrangedtheuseofspace), true, Some("ref") )).success.value
        .set(HowMuchOfPropertyEscalatorsPage, AllOf).success.value
        .set(HowMuchOfPropertyGoodsLiftPage, SomeOf).success.value
        .set(WhatHappenedToLandHardSurfacedFencedPage, Added).success.value
        .set(WhatHappenedToLandHardSurfacedOpenPage, RemovedSome).success.value

      val sections: Seq[Section] = helper.createSectionList(userAnswers)
      sections.size mustBe 4
      sections.head.title mustBe Some("checkYourAnswers.dateOfChange.heading")
      sections.head.rows.rows.size mustBe 1
      sections(1).title mustBe Some("checkYourAnswers.useOfSpace.heading")
      sections(1).rows.rows.size mustBe 4
      sections(2).title mustBe Some("checkYourAnswers.internalFeature.heading")
      sections(2).rows.rows.size mustBe 2
      sections(3).title mustBe Some("checkYourAnswers.externalFeature.heading")
      sections(3).rows.rows.size mustBe 3
    }

    "must display external and internal features details as No when there is no data exists" in {
      val helper = new CheckYourAnswersHelper()
      val userAnswers = emptyUserAnswers.set(WhenCompleteChangePage, LocalDate.of(20, 2, 2)).success.value
        .set(HaveYouChangedSpacePage, true).success.value
        .set(ChangeToUseOfSpacePage, ChangeToUseOfSpace(Set(Rearrangedtheuseofspace), true, Some("ref"))).success.value

      val sections: Seq[Section] = helper.createSectionList(userAnswers)
      sections.size mustBe 2
      sections.head.title mustBe Some("checkYourAnswers.dateOfChange.heading")
      sections(1).title mustBe Some("checkYourAnswers.useOfSpace.heading")
      sections(1).title mustBe Some("checkYourAnswers.useOfSpace.heading")
      sections(1).title mustBe Some("checkYourAnswers.useOfSpace.heading")
    }
  }
}
