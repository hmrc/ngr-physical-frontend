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

package views

import forms.SureWantRemoveChangeFormProvider
import helpers.ViewBaseSpec
import models.NavBarPageContents.createDefaultNavBar
import models.NormalMode
import org.jsoup.Jsoup
import play.api.data.Form
import views.html.SureWantRemoveChangeView

class SureWantRemoveChangeViewSpec extends ViewBaseSpec {
  val view: SureWantRemoveChangeView = inject[SureWantRemoveChangeView]
  val formProvider: SureWantRemoveChangeFormProvider = inject[SureWantRemoveChangeFormProvider]

  def formWithError(feature: String): Form[Boolean] = formProvider.apply(feature).withError("value", "sureWantRemoveChange.error.required", feature)

  object Selectors {
    val address = "#main-content > div > div.govuk-grid-column-two-thirds > form > span"
    val heading = "#main-content > div > div.govuk-grid-column-two-thirds > form > h1"
    val hint = "#value-hint"
    val yes = "#main-content > div > div.govuk-grid-column-two-thirds > form > div > fieldset > div.govuk-radios > div:nth-child(1) > label"
    val no = "#main-content > div > div.govuk-grid-column-two-thirds > form > div > fieldset > div.govuk-radios > div:nth-child(2) > label"
    val button = "#main-content > div > div.govuk-grid-column-two-thirds > form > button"
    val topError = "#main-content > div > div.govuk-grid-column-two-thirds > form > div.govuk-error-summary > div > div > ul > li > a"
    val bottomError = "#value-error"
  }


  private def testViewRendering(feature: String): Unit = {
    val form = formWithError(feature)
    val expectedHeading = s"Are you sure you want to remove the change to $feature"
    val document = Jsoup.parse(view("123 street", expectedHeading, feature, form, createDefaultNavBar(), NormalMode, false).body)

    elementText(Selectors.address)(document) mustBe "123 street"
    elementText(Selectors.heading)(document) mustBe expectedHeading
    elementText(Selectors.yes)(document) mustBe "Yes"
    elementText(Selectors.no)(document) mustBe "No"
    elementText(Selectors.button)(document) mustBe "Continue"
    elementText(Selectors.topError)(document) mustBe s"Select yes if you want to remove changes to $feature"
    elementText(Selectors.bottomError)(document) mustBe s"Error: Select yes if you want to remove changes to $feature"
  }


  "SureWantRemoveChangeView" must {
    "render correctly with internal feature and show validation errors" in {
      testViewRendering(
        feature = "passenger lifts"
      )
    }

    "render correctly with external feature and show validation errors" in {
      testViewRendering(
        feature = "advertising displays on your property"
      )
    }
  }

}
