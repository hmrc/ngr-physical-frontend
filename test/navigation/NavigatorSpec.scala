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
import models.InternalFeature.AirConditioning
import pages.*

class NavigatorSpec extends SpecBase {

  val navigator = new Navigator

  "Navigator" - {

    "in Normal mode" - {

      "must go from a page that doesn't exist in the route map to Index" in {

        case object UnknownPage extends Page
        navigator.nextPage(UnknownPage, NormalMode, UserAnswers("id")) mustBe routes.IndexController.onPageLoad()
      }
    }

    "in Check mode" - {

      "must go from a page that doesn't exist in the edit route map to CheckYourAnswers" in {

        case object UnknownPage extends Page
        navigator.nextPage(UnknownPage, CheckMode, UserAnswers("id")) mustBe routes.CheckYourAnswersController.onPageLoad()
      }

      "must go from a HaveYouChangedInternalPage` to WhichInternalFeaturePage when 'Yes' is selected an an option" in {
        val userAnswers = UserAnswers("id").set(HaveYouChangedInternalPage, true).success.value
        navigator.nextPage(HaveYouChangedInternalPage, CheckMode, userAnswers) mustBe routes.WhichInternalFeatureController.onPageLoad(CheckMode)
      }

      "must go from a HaveYouChangedInternalPage` to WhichInternalFeaturePage when 'No' is selected an an option" in {
        val userAnswers = UserAnswers("id").set(HaveYouChangedInternalPage, false).success.value
        navigator.nextPage(HaveYouChangedInternalPage, CheckMode, userAnswers) mustBe routes.HaveYouChangedController.loadExternal(CheckMode)
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

      "must go from a HaveYouChangedExternalPage` to WhichExternalFeaturePage when 'No' is selected an an option" ignore  { // go to anything else page
        val userAnswers = UserAnswers("id").set(HaveYouChangedExternalPage, false).success.value
        navigator.nextPage(HaveYouChangedExternalPage, CheckMode, userAnswers) mustBe ???
      }

      "must go from a WhatHappenedToSolarPanelsPage` to SmallCheckYourAnswers when 'added' is selected an an option" in {
        val userAnswers = UserAnswers("id").set(HaveYouChangedExternalPage, true).success.value
          .set(WhichExternalFeaturePage, ExternalFeature.SolarPanels).success.value
          .set(WhatHappenedToSolarPanelsPage, WhatHappenedTo.Added).success.value
        navigator.nextPage(WhatHappenedToSolarPanelsPage, CheckMode, userAnswers) mustBe routes.SmallCheckYourAnswersController.onPageLoad(CYAExternal, CheckMode)
      }
    }
  }
}
