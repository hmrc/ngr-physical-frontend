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

package navigation

import base.SpecBase
import controllers.routes
import helpers.TestData
import models.*
import models.HowMuchOfProperty.SomeOf
import pages.*

import java.time.LocalDate

class NavigatorSpec extends SpecBase with TestData {

  val navigator = new Navigator

  "Navigator" - {

    "in Normal mode" - {

      "must go from a page that doesn't exist in the route map to Index" in {

        case object UnknownPage extends Page
        val result = intercept[RuntimeException](navigator.nextPage(UnknownPage, NormalMode, UserAnswers("id"), assessmentId))
        result.getMessage mustBe "No selection - should be caught by form validation"
      }

      "must go to HaveYouChangedSpacePage from WhenCompleteChangePage" in {
        val userAnswers = UserAnswers("id").set(WhenCompleteChangePage(assessmentId), LocalDate.of(20, 2, 2)).success.value
        navigator.nextPage(WhenCompleteChangePage(assessmentId), NormalMode, userAnswers, assessmentId) mustBe routes.HaveYouChangedController.loadSpace(NormalMode, assessmentId)
      }

      "must go to HaveYouChangedInternalPage from HaveYouChangedSpacePage when 'No' is selected as an option" in {
        val userAnswers = UserAnswers("id")
          .set(WhenCompleteChangePage(assessmentId), LocalDate.of(20, 2, 2)).success.value
          .set(HaveYouChangedSpacePage(assessmentId), false).success.value
        navigator.nextPage(HaveYouChangedSpacePage(assessmentId), NormalMode, userAnswers, assessmentId) mustBe routes.HaveYouChangedController.loadInternal(NormalMode, assessmentId)
      }

      "must go to ChangeToUseOfSpacePage from HaveYouChangedSpacePage when 'Yes' is selected as an option" in {
        val userAnswers = UserAnswers("id")
          .set(WhenCompleteChangePage(assessmentId), LocalDate.of(20, 2, 2)).success.value
          .set(HaveYouChangedSpacePage(assessmentId), true).success.value
        navigator.nextPage(HaveYouChangedSpacePage(assessmentId), NormalMode, userAnswers, assessmentId) mustBe routes.ChangeToUseOfSpaceController.onPageLoad(NormalMode, assessmentId)
      }

      "must go to HaveYouChangedInternalPage from ChangeToUseOfSpacePage" in {
        val userAnswers = UserAnswers("id")
        navigator.nextPage(ChangeToUseOfSpacePage(assessmentId), NormalMode, userAnswers, assessmentId) mustBe routes.HaveYouChangedController.loadInternal(NormalMode, assessmentId)
      }

      "must go to HaveYouChangedInternalPage from HaveYouChangedInternalPage when 'No' is selected as an option" in {
        val userAnswers = UserAnswers("id")
          .set(WhenCompleteChangePage(assessmentId), LocalDate.of(20, 2, 2)).success.value
          .set(HaveYouChangedInternalPage(assessmentId), false).success.value
        navigator.nextPage(HaveYouChangedInternalPage(assessmentId), NormalMode, userAnswers, assessmentId) mustBe routes.HaveYouChangedController.loadExternal(NormalMode, assessmentId)
      }

      "must go to WhichInternalFeaturePage from HaveYouChangedInternalPage when 'Yes' is selected as an option" in {
        val userAnswers = UserAnswers("id")
          .set(HaveYouChangedInternalPage(assessmentId), true).success.value
        navigator.nextPage(HaveYouChangedInternalPage(assessmentId), NormalMode, userAnswers, assessmentId) mustBe routes.WhichInternalFeatureController.onPageLoad(NormalMode, assessmentId)
      }

      "must go to HaveYouChangedExternalPage from HaveYouChangedExternalPage when 'No' is selected as an option" in {
        val userAnswers = UserAnswers("id")
          .set(HaveYouChangedSpacePage(assessmentId), true).success.value
          .set(HaveYouChangedInternalPage(assessmentId), true).success.value
          .set(HaveYouChangedExternalPage(assessmentId), false).success.value
        navigator.nextPage(HaveYouChangedExternalPage(assessmentId), NormalMode, userAnswers, assessmentId) mustBe routes.AnythingElseController.onPageLoad(NormalMode, assessmentId)
      }

      "must go from a HaveYouChangedExternalPage to CheckYourAnswersPage when 'No' is selected as an option" in {
        val userAnswers = UserAnswers("id").set(HaveYouChangedExternalPage(assessmentId), false).success.value
        navigator.nextPage(HaveYouChangedExternalPage(assessmentId), CheckMode, userAnswers, assessmentId) mustBe routes.CheckYourAnswersController.onPageLoad(assessmentId)
      }

      "must go to WhichExternalFeaturePage from HaveYouChangedExternalPage when 'Yes' is selected as an option" in {
        val userAnswers = UserAnswers("id")
          .set(HaveYouChangedExternalPage(assessmentId), true).success.value
        navigator.nextPage(HaveYouChangedExternalPage(assessmentId), NormalMode, userAnswers, assessmentId) mustBe routes.WhichExternalFeatureController.onPageLoad(NormalMode, assessmentId)
      }

      "must go to not told us about any changes page when all have you changed pages are set to no" in {
        val userAnswers = UserAnswers("id").set(HaveYouChangedSpacePage(assessmentId), false).success.value
          .set(HaveYouChangedInternalPage(assessmentId), false).success.value
          .set(HaveYouChangedExternalPage(assessmentId), false).success.value
        navigator.nextPage(HaveYouChangedExternalPage(assessmentId), NormalMode, userAnswers, assessmentId) mustBe routes.NotToldAnyChangesController.show(assessmentId)
      }

      "must go SupportingDocuments from AnythingElsePage" in {
        val userAnswers = UserAnswers("id")
        navigator.nextPage(AnythingElsePage(assessmentId), NormalMode, userAnswers, assessmentId) mustBe routes.SupportingDocumentsController.onPageLoad(assessmentId)
      }

      InternalFeature.pageSet(assessmentId).filter(_ != SecurityCamerasChangePage(assessmentId)) foreach { page =>
        s"must go to SmallCheckYourAnswers from $page" in {
          val userAnswers = UserAnswers("id").set(page.asInstanceOf[queries.Settable[HowMuchOfProperty]], HowMuchOfProperty.AllOf).success.value
          navigator.nextPage(page, NormalMode, userAnswers, assessmentId) mustBe routes.SmallCheckYourAnswersController.onPageLoad(CYAInternal, NormalMode, assessmentId)
        }
      }

      ExternalFeature.pageSet(assessmentId) foreach { page =>
        s"must go to SmallCheckYourAnswers from $page" in {
          val userAnswers = UserAnswers("id").set(page, WhatHappenedTo.Added).success.value
          navigator.nextPage(page, NormalMode, userAnswers, assessmentId) mustBe routes.SmallCheckYourAnswersController.onPageLoad(CYAExternal, NormalMode, assessmentId)
        }
      }
    }

    "in Check mode" - {

      "must go from a page that doesn't exist in the edit route map to CheckYourAnswers" in {

        case object UnknownPage extends Page
        navigator.nextPage(UnknownPage, CheckMode, UserAnswers("id"), assessmentId) mustBe routes.CheckYourAnswersController.onPageLoad(assessmentId)
      }

      "must go from a HaveYouChangedSpacePage` to ChangeToUseOfSpacePage when 'Yes' is selected an an option" in {
        val userAnswers = UserAnswers("id").set(HaveYouChangedSpacePage(assessmentId), true).success.value
        navigator.nextPage(HaveYouChangedSpacePage(assessmentId), CheckMode, userAnswers, assessmentId) mustBe routes.ChangeToUseOfSpaceController.onPageLoad(CheckMode, assessmentId)
      }

      "must go from a HaveYouChangedSpacePage` to CheckYourAnswersPage when 'No' is selected an an option" in {
        val userAnswers = UserAnswers("id").set(HaveYouChangedSpacePage(assessmentId), false).success.value
        navigator.nextPage(HaveYouChangedSpacePage(assessmentId), CheckMode, userAnswers, assessmentId) mustBe routes.CheckYourAnswersController.onPageLoad(assessmentId)
      }

      "must go from a HaveYouChangedInternalPage` to WhichInternalFeaturePage when 'Yes' is selected an an option" in {
        val userAnswers = UserAnswers("id").set(HaveYouChangedInternalPage(assessmentId), true).success.value
        navigator.nextPage(HaveYouChangedInternalPage(assessmentId), CheckMode, userAnswers, assessmentId) mustBe routes.WhichInternalFeatureController.onPageLoad(CheckMode, assessmentId)
      }

      "must go from a HaveYouChangedInternalPage` to WhichInternalFeaturePage when 'No' is selected an an option" in {
        val userAnswers = UserAnswers("id").set(HaveYouChangedInternalPage(assessmentId), false).success.value
        navigator.nextPage(HaveYouChangedInternalPage(assessmentId), CheckMode, userAnswers, assessmentId) mustBe routes.HaveYouChangedController.loadExternal(CheckMode, assessmentId)
      }

      "should throw RuntimeException when no selection is made" in {
        val userAnswers = UserAnswers("id").set(HaveYouChangedInternalPage(assessmentId), true).success.value
          .remove(HaveYouChangedInternalPage(assessmentId)).success.value

        val ex = intercept[RuntimeException] {
          navigator.nextPage(HaveYouChangedInternalPage(assessmentId), CheckMode, userAnswers, assessmentId)
        }
        ex.getMessage mustBe "No selection - should be caught by form validation"
      }

      "must go from a HowMuchOfPropertyAirConPage` to SmallCheckYourAnswers when 'AllOf' is selected an an option" in {
        val userAnswers = UserAnswers("id").set(HaveYouChangedInternalPage(assessmentId), true).success.value
          .set(WhichInternalFeaturePage(assessmentId), InternalFeature.AirConditioning).success.value
          .set(HowMuchOfPropertyAirConPage(assessmentId), models.HowMuchOfProperty.AllOf).success.value
        navigator.nextPage(HowMuchOfPropertyAirConPage(assessmentId), CheckMode, userAnswers, assessmentId) mustBe routes.SmallCheckYourAnswersController.onPageLoad(CYAInternal, CheckMode, assessmentId)
      }

      "must go from a HaveYouChangedExternalPage` to WhichExternalFeaturePage when 'Yes' is selected an an option" in {
        val userAnswers = UserAnswers("id").set(HaveYouChangedExternalPage(assessmentId), true).success.value
        navigator.nextPage(HaveYouChangedExternalPage(assessmentId), CheckMode, userAnswers, assessmentId) mustBe routes.WhichExternalFeatureController.onPageLoad(CheckMode, assessmentId)
      }

      "must go from a WhatHappenedToSolarPanelsPage` to SmallCheckYourAnswers when 'added' is selected an an option" in {
        val userAnswers = UserAnswers("id").set(HaveYouChangedExternalPage(assessmentId), true).success.value
          .set(WhichExternalFeaturePage(assessmentId), ExternalFeature.SolarPanels).success.value
          .set(WhatHappenedToSolarPanelsPage(assessmentId), WhatHappenedTo.Added).success.value
        navigator.nextPage(WhatHappenedToSolarPanelsPage(assessmentId), CheckMode, userAnswers, assessmentId) mustBe routes.SmallCheckYourAnswersController.onPageLoad(CYAExternal, CheckMode, assessmentId)
      }


      "should go to SmallCheckYourAnswersController.onPageLoad(CYAInternal, CheckMode) for InternalFeature page" in {
        InternalFeature.pageSet(assessmentId).foreach { page =>
          val userAnswers = UserAnswers("id")
          navigator.nextPage(page, CheckMode, userAnswers, assessmentId) mustBe routes.SmallCheckYourAnswersController.onPageLoad(CYAInternal, CheckMode, assessmentId)
        }
      }

      "should go to SmallCheckYourAnswersController.onPageLoad(CYAExternal, CheckMode) for ExternalFeature page" in {
        ExternalFeature.pageSet(assessmentId).foreach { page =>
          val userAnswers = UserAnswers("id")
          navigator.nextPage(page, CheckMode, userAnswers, assessmentId) mustBe routes.SmallCheckYourAnswersController.onPageLoad(CYAExternal, CheckMode, assessmentId)
        }
      }

      "should go to CheckYourAnswersController.onPageLoad for unknown page" in {
        case object UnknownPage extends Page
        val userAnswers = UserAnswers("id")
        navigator.nextPage(UnknownPage, CheckMode, userAnswers, assessmentId) mustBe routes.CheckYourAnswersController.onPageLoad(assessmentId)
      }
    }
  }
}
