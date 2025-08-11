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

import forms.HaveYouChangedFormProvider
import helpers.ViewBaseSpec
import models.NavBarPageContents.createDefaultNavBar
import models.{External, HaveYouChangedControllerUse, Internal, NormalMode, Space}
import org.jsoup.Jsoup
import play.api.data.Form
import views.html.HaveYouChangedView
import play.api.mvc.Call

class HaveYouChangedViewSpec extends ViewBaseSpec {
  val view: HaveYouChangedView = inject[HaveYouChangedView]
  val formProvider: HaveYouChangedFormProvider = inject[HaveYouChangedFormProvider]
  val call: Call = Call("", "")

  def formWithError(use: HaveYouChangedControllerUse): Form[Boolean] = formProvider.apply(use).withError("value", use match {
    case Space => "haveYouChangedSpace.error.required"
    case Internal => "haveYouChangedInternal.error.required"
    case External => "haveYouChangedExternal.error.required"
  })

  object Selectors {
    val address = "#main-content > div > div.govuk-grid-column-two-thirds > form > span"
    val heading = "#main-content > div > div.govuk-grid-column-two-thirds > form > div > fieldset > legend"
    val hint = "#value-hint"
    val yes = "#main-content > div > div.govuk-grid-column-two-thirds > form > div > fieldset > div.govuk-radios > div:nth-child(1) > label"
    val no = "#main-content > div > div.govuk-grid-column-two-thirds > form > div > fieldset > div.govuk-radios > div:nth-child(2) > label"
    val button = "#main-content > div > div.govuk-grid-column-two-thirds > form > button"
    val topError = "#main-content > div > div.govuk-grid-column-two-thirds > form > div.govuk-error-summary > div > div > ul > li > a"
    val bottomError = "#value-error"
  }

  "HaveYouChangedView" must {
    "show correct strings" when {
      "space mode" in {
        val form = formWithError(Space)
        val document = Jsoup.parse(view("123 street", "haveYouChangedSpace.title", "haveYouChangedSpace.hint", form, Space, NormalMode, call, createDefaultNavBar()).body)
        elementText(Selectors.address)(document) mustBe "123 street"
        elementText(Selectors.heading)(document) mustBe "Have you changed use of space?"
        elementText(Selectors.hint)(document) mustBe "For example, you increased the size of the retail area by building an extension."
        elementText(Selectors.yes)(document) mustBe "Yes"
        elementText(Selectors.no)(document) mustBe "No"
        elementText(Selectors.button)(document) mustBe "Save and continue"
        elementText(Selectors.topError)(document) mustBe "Select yes if you have changed use of space"
        elementText(Selectors.bottomError)(document) mustBe "Error: Select yes if you have changed use of space"
      }
      "internal mode" in {
        val form = formWithError(Internal)
        val document = Jsoup.parse(view("123 street", "haveYouChangedInternal.title", "haveYouChangedInternal.hint", form, Internal, NormalMode, call, createDefaultNavBar()).body)
        elementText(Selectors.address)(document) mustBe "123 street"
        elementText(Selectors.heading)(document) mustBe "Have you changed internal features?"
        elementText(Selectors.hint)(document) mustBe "For example you added air conditioning and removed sprinklers."
        elementText(Selectors.yes)(document) mustBe "Yes"
        elementText(Selectors.no)(document) mustBe "No"
        elementText(Selectors.button)(document) mustBe "Save and continue"
        elementText(Selectors.topError)(document) mustBe "Select yes if you have changed internal features"
        elementText(Selectors.bottomError)(document) mustBe "Error: Select yes if you have changed internal features"
      }
      "external mode" in {
        val form = formWithError(External)
        val document = Jsoup.parse(view("123 street", "haveYouChangedExternal.title", "haveYouChangedExternal.hint", form, External, NormalMode, call, createDefaultNavBar()).body)
        elementText(Selectors.address)(document) mustBe "123 street"
        elementText(Selectors.heading)(document) mustBe "Have you changed external features?"
        elementText(Selectors.hint)(document) mustBe "For example you added parking and removed lock-up garages."
        elementText(Selectors.yes)(document) mustBe "Yes"
        elementText(Selectors.no)(document) mustBe "No"
        elementText(Selectors.button)(document) mustBe "Save and continue"
        elementText(Selectors.topError)(document) mustBe "Select yes if you have changed external features"
        elementText(Selectors.bottomError)(document) mustBe "Error: Select yes if you have changed external features"
      }
    }
  }

}
