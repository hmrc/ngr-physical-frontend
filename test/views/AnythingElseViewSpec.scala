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

import forms.AnythingElseFormProvider
import helpers.ViewBaseSpec
import models.NormalMode
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import views.html.AnythingElseView

class AnythingElseViewSpec extends ViewBaseSpec{
  val view: AnythingElseView = inject[AnythingElseView]
  val address = "123 street lane"
  val formProvider: AnythingElseFormProvider = inject[AnythingElseFormProvider]
  implicit val document: Document = Jsoup.parse(view(assessmentId, address = address, form = formProvider(), navigationBarContent = navBarContent(), mode = NormalMode).body)

  object Selectors {
    val address = "#main-content > div > div.govuk-grid-column-two-thirds > form > span"
    val heading = "#main-content > div > div.govuk-grid-column-two-thirds > form > h1"
    val yes = "#main-content > div > div.govuk-grid-column-two-thirds > form > div > fieldset > div > div:nth-child(1) > label"
    val no = "#main-content > div > div.govuk-grid-column-two-thirds > form > div > fieldset > div > div:nth-child(3) > label"
    val inputTitle = "#conditional-value > div > label"
    val continue = "#continue"
  }

  "AnythingElseView" must {
    "show correct text" in {
      elementText(Selectors.address) mustBe address
      elementText(Selectors.heading) mustBe "Is there anything else you want to tell us about the changes?"
      elementText(Selectors.yes) mustBe "Yes"
      elementText(Selectors.no) mustBe "No"
      elementText(Selectors.inputTitle) mustBe "What do you want to tell us?"
      elementText(Selectors.continue) mustBe "Continue"
    }
  }
}
