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

package models

import controllers.routes
import helpers.{TestData, TestSupport}
import models.{InternalFeature, NormalMode}
import org.scalatest.TryValues.convertTryToSuccessOrFailure
import org.scalatest.matchers.should.Matchers
import org.scalatest.matchers.should.Matchers.{should, shouldBe}
import org.scalatest.wordspec.AnyWordSpec
import pages.{HowMuchOfPropertyAirConPage, SecurityCamerasChangePage}
import play.api.i18n.Messages.implicitMessagesProviderToMessages
import play.api.mvc.Call
import uk.gov.hmrc.govukfrontend.views.viewmodels.summarylist.SummaryListRow

class InternalFeatureSpec extends TestSupport with TestData {

  "changeLink" should {

    "return correct Call for AirConditioning" in {
      val result: Call = InternalFeature.changeLink(InternalFeature.AirConditioning, NormalMode, assessmentId)
      result shouldBe routes.HowMuchOfPropertyController.onPageLoadAirCon(NormalMode, assessmentId)
    }

    "return correct Call for Escalators" in {
      val result: Call = InternalFeature.changeLink(InternalFeature.Escalators, NormalMode, assessmentId)
      result shouldBe routes.HowMuchOfPropertyController.onPageLoadEscalator(NormalMode, assessmentId)
    }

    "return correct Call for GoodsLift" in {
      val result: Call = InternalFeature.changeLink(InternalFeature.GoodsLift, NormalMode, assessmentId)
      result shouldBe routes.HowMuchOfPropertyController.onPageLoadGoodsLift(NormalMode, assessmentId)
    }

    "return correct Call for PassengerLift" in {
      val result: Call = InternalFeature.changeLink(InternalFeature.PassengerLift, NormalMode, assessmentId)
      result shouldBe routes.HowMuchOfPropertyController.onPageLoadPassengerLift(NormalMode, assessmentId)
    }

    "return correct Call for SecurityCamera" in {
      val result: Call = InternalFeature.changeLink(InternalFeature.SecurityCamera, NormalMode, assessmentId)
      result shouldBe routes.SecurityCamerasChangeController.onPageLoad(NormalMode, assessmentId)
    }

    "return correct Call for CompressedAir" in {
      val result: Call = InternalFeature.changeLink(InternalFeature.CompressedAir, NormalMode, assessmentId)
      result shouldBe routes.HowMuchOfPropertyController.onPageLoadCompressedAir(NormalMode, assessmentId)
    }

    "return correct Call for Heating" in {
      val result: Call = InternalFeature.changeLink(InternalFeature.Heating, NormalMode, assessmentId)
      result shouldBe routes.HowMuchOfPropertyController.onPageLoadHeating(NormalMode, assessmentId)
    }

    "return correct Call for Sprinklers" in {
      val result: Call = InternalFeature.changeLink(InternalFeature.Sprinklers, NormalMode, assessmentId)
      result shouldBe routes.HowMuchOfPropertyController.onPageLoadSprinklers(NormalMode, assessmentId)
    }
  }


  "getAnswers" should {
    
    "return a SummaryListRow for SecurityCamera when present in UserAnswers" in {
      val userAnswers = UserAnswers("id").set(SecurityCamerasChangePage, 50).success.value

      val result: Seq[SummaryListRow] =
        InternalFeature.getAnswers(userAnswers, NormalMode, assessmentId = assessmentId)

      result.size shouldBe 1
      val row = result.head
      
      row.value.content.asHtml.body should include("50")
      row.actions.get.items.map(_.href) should contain(routes.SecurityCamerasChangeController.onPageLoad(NormalMode, assessmentId).url)
    }
    
  }

}