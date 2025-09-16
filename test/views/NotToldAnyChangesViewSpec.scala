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
import views.html.NotToldAnyChangesView

class NotToldAnyChangesViewSpec extends ViewBaseSpec {
  val view: NotToldAnyChangesView = inject[NotToldAnyChangesView]

  object Selectors {
    val address = "#main-content > div > div.govuk-grid-column-two-thirds > form > span"
    val heading = "#main-content > div > div.govuk-grid-column-two-thirds > form > h1"
    val p1Text = "#main-content > div > div.govuk-grid-column-two-thirds > form > p:nth-child(3)"
    val ul1li1Text = "#main-content > div > div.govuk-grid-column-two-thirds > form > ul > li:nth-child(1) > a"
    val ul1li2Text = "#main-content > div > div.govuk-grid-column-two-thirds > form > ul > li:nth-child(2) > a"
    val ul1li3Text = "#main-content > div > div.govuk-grid-column-two-thirds > form > ul > li:nth-child(3) > a"
    val p2Text = "#main-content > div > div.govuk-grid-column-two-thirds > form > p:nth-child(5)"
    val p3Text = "#main-content > div > div.govuk-grid-column-two-thirds > form > p:nth-child(6)"
  }

  "NotToldAnyChangesView" must {
    "render correctly" in {
      val document = Jsoup.parse(view("123 street", createDefaultNavBar(), NormalMode).body)

      elementText(Selectors.address)(document) mustBe "123 street"
      elementText(Selectors.heading)(document) mustBe "You have not told us about any changes to your property"
      elementText(Selectors.p1Text)(document) mustBe "To continue, you need to tell us about a change to at least one of the following:"
      elementText(Selectors.ul1li1Text)(document) mustBe "use of space"
      elementText(Selectors.ul1li2Text)(document) mustBe "internal features"
      elementText(Selectors.ul1li3Text)(document) mustBe "external features"
      elementText(Selectors.p2Text)(document) mustBe "If you do not have any changes to tell us about, you can go to your account home. We will not save any information or documents you have provided."
      elementText(Selectors.p3Text)(document) mustBe "Go to your account home"
    }

  }

}
