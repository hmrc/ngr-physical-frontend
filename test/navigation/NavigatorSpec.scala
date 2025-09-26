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
import models.*
import models.HowMuchOfProperty.SomeOf
import pages.*

import java.time.LocalDate

class NavigatorSpec extends SpecBase {

  val navigator = new Navigator

  "Navigator" - {

    "in Normal mode" - {

      "must go from a page that doesn't exist in the route map to Index" in {

        case object UnknownPage extends Page
        val result = intercept[RuntimeException](navigator.nextPage(UnknownPage, NormalMode, UserAnswers("id")))
        result.getMessage mustBe "No selection - should be caught by form validation"
      }

      "must go to HaveYouChangedSpacePage from WhenCompleteChangePage" in {
        val userAnswers = UserAnswers("id").set(WhenCompleteChangePage, LocalDate.of(20, 2, 2)).success.value
        navigator.nextPage(WhenCompleteChangePage, NormalMode, userAnswers) mustBe routes.HaveYouChangedController.loadSpace(NormalMode)
      }

      "must go to HaveYouChangedInternalPage from HaveYouChangedSpacePage when 'No' is selected as an option" in {
        val userAnswers = UserAnswers("id")
          .set(WhenCompleteChangePage, LocalDate.of(20, 2, 2)).success.value
          .set(HaveYouChangedSpacePage, false).success.value
        navigator.nextPage(HaveYouChangedSpacePage, NormalMode, userAnswers) mustBe routes.HaveYouChangedController.loadInternal(NormalMode)
      }

      "must go to ChangeToUseOfSpacePage from HaveYouChangedSpacePage when 'Yes' is selected as an option" in {
        val userAnswers = UserAnswers("id")
          .set(WhenCompleteChangePage, LocalDate.of(20, 2, 2)).success.value
          .set(HaveYouChangedSpacePage, true).success.value
        navigator.nextPage(HaveYouChangedSpacePage, NormalMode, userAnswers) mustBe routes.ChangeToUseOfSpaceController.onPageLoad(NormalMode)
      }

      "must go to HaveYouChangedInternalPage from ChangeToUseOfSpacePage" in {
        val userAnswers = UserAnswers("id")
        navigator.nextPage(ChangeToUseOfSpacePage, NormalMode, userAnswers) mustBe routes.HaveYouChangedController.loadInternal(NormalMode)
      }

      "must go to HaveYouChangedInternalPage from HaveYouChangedInternalPage when 'No' is selected as an option" in {
        val userAnswers = UserAnswers("id")
          .set(WhenCompleteChangePage, LocalDate.of(20, 2, 2)).success.value
          .set(HaveYouChangedInternalPage, false).success.value
        navigator.nextPage(HaveYouChangedInternalPage, NormalMode, userAnswers) mustBe routes.HaveYouChangedController.loadExternal(NormalMode)
      }

      "must go to WhichInternalFeaturePage from HaveYouChangedInternalPage when 'Yes' is selected as an option" in {
        val userAnswers = UserAnswers("id")
          .set(HaveYouChangedInternalPage, true).success.value
        navigator.nextPage(HaveYouChangedInternalPage, NormalMode, userAnswers) mustBe routes.WhichInternalFeatureController.onPageLoad(NormalMode)
      }

      "must go to HaveYouChangedExternalPage from HaveYouChangedExternalPage when 'No' is selected as an option" in {
        val userAnswers = UserAnswers("id")
          .set(HaveYouChangedSpacePage, true).success.value
          .set(HaveYouChangedInternalPage, true).success.value
          .set(HaveYouChangedExternalPage, false).success.value
        navigator.nextPage(HaveYouChangedExternalPage, NormalMode, userAnswers) mustBe routes.AnythingElseController.onPageLoad(NormalMode)
      }

      "must go from a HaveYouChangedExternalPage to CheckYourAnswersPage when 'No' is selected as an option" in {
        val userAnswers = UserAnswers("id").set(HaveYouChangedExternalPage, false).success.value
        navigator.nextPage(HaveYouChangedExternalPage, CheckMode, userAnswers) mustBe routes.CheckYourAnswersController.onPageLoad()
      }

      "must go to WhichExternalFeaturePage from HaveYouChangedExternalPage when 'Yes' is selected as an option" in {
        val userAnswers = UserAnswers("id")
          .set(HaveYouChangedExternalPage, true).success.value
        navigator.nextPage(HaveYouChangedExternalPage, NormalMode, userAnswers) mustBe routes.WhichExternalFeatureController.onPageLoad(NormalMode)
      }

      "must go to not told us about any changes page when all have you changed pages are set to no" in {
        val userAnswers = UserAnswers("id").set(HaveYouChangedSpacePage, false).success.value
          .set(HaveYouChangedInternalPage, false).success.value
          .set(HaveYouChangedExternalPage, false).success.value
        navigator.nextPage(HaveYouChangedExternalPage, NormalMode, userAnswers) mustBe routes.NotToldAnyChangesController.show
      }

      "must go SupportingDocuments from AnythingElsePage" in {
        val userAnswers = UserAnswers("id")
        navigator.nextPage(AnythingElsePage, NormalMode, userAnswers) mustBe routes.SupportingDocumentsController.onPageLoad()
      }

      InternalFeature.pageSet.filter(_ != SecurityCamerasChangePage) foreach { page =>
        s"must go to SmallCheckYourAnswers from $page" in {
          val userAnswers = UserAnswers("id").set(page.asInstanceOf[queries.Settable[HowMuchOfProperty]], HowMuchOfProperty.AllOf).success.value
          navigator.nextPage(page, NormalMode, userAnswers) mustBe routes.SmallCheckYourAnswersController.onPageLoad(CYAInternal, NormalMode)
        }
      }

      ExternalFeature.pageSet foreach { page =>
        s"must go to SmallCheckYourAnswers from $page" in {
          val userAnswers = UserAnswers("id").set(page, WhatHappenedTo.Added).success.value
          navigator.nextPage(page, NormalMode, userAnswers) mustBe routes.SmallCheckYourAnswersController.onPageLoad(CYAExternal, NormalMode)
        }
      }
    }

    "in Check mode" - {

      "must go from a page that doesn't exist in the edit route map to CheckYourAnswers" in {

        case object UnknownPage extends Page
        navigator.nextPage(UnknownPage, CheckMode, UserAnswers("id")) mustBe routes.CheckYourAnswersController.onPageLoad()
      }

      "must go from a HaveYouChangedSpacePage` to ChangeToUseOfSpacePage when 'Yes' is selected an an option" in {
        val userAnswers = UserAnswers("id").set(HaveYouChangedSpacePage, true).success.value
        navigator.nextPage(HaveYouChangedSpacePage, CheckMode, userAnswers) mustBe routes.ChangeToUseOfSpaceController.onPageLoad(CheckMode)
      }

      "must go from a HaveYouChangedSpacePage` to CheckYourAnswersPage when 'No' is selected an an option" in {
        val userAnswers = UserAnswers("id").set(HaveYouChangedSpacePage, false).success.value
        navigator.nextPage(HaveYouChangedSpacePage, CheckMode, userAnswers) mustBe routes.CheckYourAnswersController.onPageLoad()
      }

      "must go from a HaveYouChangedInternalPage` to WhichInternalFeaturePage when 'Yes' is selected an an option" in {
        val userAnswers = UserAnswers("id").set(HaveYouChangedInternalPage, true).success.value
        navigator.nextPage(HaveYouChangedInternalPage, CheckMode, userAnswers) mustBe routes.WhichInternalFeatureController.onPageLoad(CheckMode)
      }

      "must go from a HaveYouChangedInternalPage` to WhichInternalFeaturePage when 'No' is selected an an option" in {
        val userAnswers = UserAnswers("id").set(HaveYouChangedInternalPage, false).success.value
        navigator.nextPage(HaveYouChangedInternalPage, CheckMode, userAnswers) mustBe routes.HaveYouChangedController.loadExternal(CheckMode)
      }

      "should throw RuntimeException when no selection is made" in {
        val userAnswers = UserAnswers("id").set(HaveYouChangedInternalPage, true).success.value
          .remove(HaveYouChangedInternalPage).success.value
        
        val ex = intercept[RuntimeException] {
          navigator.nextPage(HaveYouChangedInternalPage, CheckMode, userAnswers)
        }
        ex.getMessage mustBe "No selection - should be caught by form validation"
      }

      "must go from a HowMuchOfPropertyAirConPage` to SmallCheckYourAnswers when 'AllOf' is selected an an option" in {
        val userAnswers = UserAnswers("id").set(HaveYouChangedInternalPage, true).success.value
          .set(WhichInternalFeaturePage, InternalFeature.AirConditioning).success.value
          .set(HowMuchOfPropertyAirConPage, models.HowMuchOfProperty.AllOf).success.value
        navigator.nextPage(HowMuchOfPropertyAirConPage, CheckMode, userAnswers) mustBe routes.SmallCheckYourAnswersController.onPageLoad(CYAInternal, CheckMode)
      }

      "must go from a HaveYouChangedExternalPage` to WhichExternalFeaturePage when 'Yes' is selected an an option" in {
        val userAnswers = UserAnswers("id").set(HaveYouChangedExternalPage, true).success.value
        navigator.nextPage(HaveYouChangedExternalPage, CheckMode, userAnswers) mustBe routes.WhichExternalFeatureController.onPageLoad(CheckMode)
      }

      "must go from a WhatHappenedToSolarPanelsPage` to SmallCheckYourAnswers when 'added' is selected an an option" in {
        val userAnswers = UserAnswers("id").set(HaveYouChangedExternalPage, true).success.value
          .set(WhichExternalFeaturePage, ExternalFeature.SolarPanels).success.value
          .set(WhatHappenedToSolarPanelsPage, WhatHappenedTo.Added).success.value
        navigator.nextPage(WhatHappenedToSolarPanelsPage, CheckMode, userAnswers) mustBe routes.SmallCheckYourAnswersController.onPageLoad(CYAExternal, CheckMode)
      }


      "should go to SmallCheckYourAnswersController.onPageLoad(CYAInternal, CheckMode) for InternalFeature page" in {
        InternalFeature.pageSet.foreach { page =>
          val userAnswers = UserAnswers("id")
          navigator.nextPage(page, CheckMode, userAnswers) mustBe routes.SmallCheckYourAnswersController.onPageLoad(CYAInternal, CheckMode)
        }
      }

      "should go to SmallCheckYourAnswersController.onPageLoad(CYAExternal, CheckMode) for ExternalFeature page" in {
        ExternalFeature.pageSet.foreach { page =>
          val userAnswers = UserAnswers("id")
          navigator.nextPage(page, CheckMode, userAnswers) mustBe routes.SmallCheckYourAnswersController.onPageLoad(CYAExternal, CheckMode)
        }
      }

      "should go to CheckYourAnswersController.onPageLoad for unknown page" in {
        case object UnknownPage extends Page
        val userAnswers = UserAnswers("id")
        navigator.nextPage(UnknownPage, CheckMode, userAnswers) mustBe routes.CheckYourAnswersController.onPageLoad()
      }
    }
  }
}
