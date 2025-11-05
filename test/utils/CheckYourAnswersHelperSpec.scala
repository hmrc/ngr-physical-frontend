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
import helpers.TestData
import models.{AnythingElseData, ChangeToUseOfSpace}
import models.HowMuchOfProperty.{AllOf, SomeOf}
import models.UseOfSpaces.Rearrangedtheuseofspace
import models.WhatHappenedTo.{Added, RemovedSome}
import models.upscan.{UploadId, UploadStatus}
import org.mockito.Mockito.when
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.funsuite.AnyFunSuiteLike
import org.scalatest.matchers.must.Matchers
import pages.{AnythingElsePage, ChangeToUseOfSpacePage, HaveYouChangedExternalPage, HaveYouChangedInternalPage, HaveYouChangedSpacePage, HowMuchOfPropertyAirConPage, HowMuchOfPropertyEscalatorsPage, HowMuchOfPropertyGoodsLiftPage, UploadDocumentsPage, WhatHappenedToLandHardSurfacedFencedPage, WhatHappenedToLandHardSurfacedOpenPage, WhenCompleteChangePage}
import play.api.i18n.Messages
import play.api.i18n.Messages.implicitMessagesProviderToMessages
import play.api.test.{FakeRequest, Helpers}
import services.UploadProgressTracker
import uk.gov.hmrc.govukfrontend.views.Aliases.{SummaryList, Text, Value}
import uk.gov.hmrc.govukfrontend.views.viewmodels.content.{Content, Text}
import uk.gov.hmrc.govukfrontend.views.viewmodels.summarylist.{ActionItem, Actions, Key, SummaryListRow}
import viewmodels.Section

import java.time.LocalDate
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.mockito.MockitoSugar.mock
import org.mockito.ArgumentMatchers.any
import uk.gov.hmrc.http.StringContextOps

import java.net.URL
import scala.concurrent.{Await, Future}
import scala.concurrent.duration.DurationInt

class CheckYourAnswersHelperSpec extends SpecBase with Matchers with TestData {

  implicit val messages: Messages = Helpers.stubMessagesApi().preferred(FakeRequest())

  val mockUploadProgressTracker: UploadProgressTracker = mock[UploadProgressTracker]

  when(mockUploadProgressTracker.getUploadResult(UploadId("1111")))
    .thenReturn(Future.successful(Some(UploadStatus.UploadedSuccessfully("file.pdf", "application/pdf", url"http:localhost:8080", Some(1234)))))
  when(mockUploadProgressTracker.getUploadResult(UploadId("1112")))
    .thenReturn(Future.successful(Some(UploadStatus.UploadedSuccessfully("file2.pdf", "application/pdf", url"http:localhost:8080", Some(1234)))))

  "CheckYourAnswersHelper" - {

    "must display all the answers of physical on the page" in {
      val helper = new CheckYourAnswersHelper(mockUploadProgressTracker)
      val userAnswers = emptyUserAnswers.set(WhenCompleteChangePage, LocalDate.of(20, 2, 2)).success.value
        .set(HaveYouChangedSpacePage, true).success.value
        .set(ChangeToUseOfSpacePage,  ChangeToUseOfSpace(Set(Rearrangedtheuseofspace), true, Some("ref") )).success.value
        .set(HowMuchOfPropertyEscalatorsPage, AllOf).success.value
        .set(HowMuchOfPropertyGoodsLiftPage, SomeOf).success.value
        .set(WhatHappenedToLandHardSurfacedFencedPage, Added).success.value
        .set(WhatHappenedToLandHardSurfacedOpenPage, RemovedSome).success.value
        .set(AnythingElsePage, AnythingElseData(true, Some("info"))).success.value
        .set(UploadDocumentsPage, Seq("1111", "1112")).success.value

      val sections: Seq[Section] = Await.result(helper.createSectionList(userAnswers), 5.seconds)
      sections.size mustBe 6
      sections.head.title mustBe Some("checkYourAnswers.dateOfChange.heading")
      sections.head.rows.rows.size mustBe 1
      sections(1).title mustBe Some("checkYourAnswers.useOfSpace.heading")
      sections(1).rows.rows.size mustBe 4
      sections(2).title mustBe Some("checkYourAnswers.internalFeature.heading")
      sections(2).rows.rows.size mustBe 2
      sections(3).title mustBe Some("checkYourAnswers.externalFeature.heading")
      sections(3).rows.rows.size mustBe 2
      sections(4).title mustBe Some("checkYourAnswers.additionalInformation.heading")
      sections(4).rows.rows.size mustBe 2
      sections(4).rows.rows.tail.head.key.content mustBe Text("anythingElse.inputTitle")
      sections(5).title mustBe Some("checkYourAnswers.supportingDocuments.heading")
      sections(5).rows.rows.size mustBe 2
    }

    "must display external and internal features details as No when user has removed all the internal and external features" in {

      val helper = new CheckYourAnswersHelper(mockUploadProgressTracker)
      val userAnswers = emptyUserAnswers.set(WhenCompleteChangePage, LocalDate.of(20, 2, 2)).success.value
        .set(HaveYouChangedSpacePage, true).success.value
        .set(ChangeToUseOfSpacePage, ChangeToUseOfSpace(Set(Rearrangedtheuseofspace), true, Some("ref"))).success.value
        .set(HaveYouChangedInternalPage, false).success.value
        .set(HaveYouChangedExternalPage, false).success.value
        .set(UploadDocumentsPage, Seq("1111")).success.value

      val sections: Seq[Section] = Await.result(helper.createSectionList(userAnswers), 5.seconds)
      sections.size mustBe 5
      sections(1).title mustBe Some("checkYourAnswers.useOfSpace.heading")
      sections(2).title mustBe Some("checkYourAnswers.internalFeature.heading")
      sections(2).rows.rows.size mustBe 1
      sections(2).rows.rows.head.key.content mustBe Text("haveYouChangedInternal.title")
      sections(2).rows.rows.head.value.content mustBe Text("site.no")
      sections(3).title mustBe Some("checkYourAnswers.externalFeature.heading")
      sections(3).rows.rows.size mustBe 1
      sections(3).rows.rows.head.key.content mustBe Text("haveYouChangedExternal.title")
      sections(3).rows.rows.head.value.content mustBe Text("site.no")
      sections(4).title mustBe Some("checkYourAnswers.supportingDocuments.heading")
      sections(4).rows.rows.size mustBe 1
    }
  }
}
