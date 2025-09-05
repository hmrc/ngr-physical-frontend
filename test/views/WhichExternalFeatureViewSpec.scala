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

import forms.WhichExternalFeatureFormProvider
import helpers.ViewBaseSpec
import models.NormalMode
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import play.api.data.FormError
import views.html.WhichExternalFeatureView

class WhichExternalFeatureViewSpec extends ViewBaseSpec {
  val view: WhichExternalFeatureView = inject[WhichExternalFeatureView]
  val address: String = "123 Street Lane"
  val formProvider: WhichExternalFeatureFormProvider = inject[WhichExternalFeatureFormProvider]

  object Selectors {
    val address = "#main-content > div > div.govuk-grid-column-two-thirds > form > span"
    val heading = "#main-content > div > div.govuk-grid-column-two-thirds > form > h1"
    def bullet(child: Int): String = s"#main-content > div > div.govuk-grid-column-two-thirds > form > div > fieldset > div > div:nth-child($child) > label"
    val loadingBays: String = bullet(1)
    val lockupGarages: String = bullet(2)
    val outdoorSeating: String = bullet(3)
    val parking: String = bullet(4)
    val solarPanels: String = bullet(5)
    val other: String = bullet(6)
    val topError: String = "#main-content > div > div.govuk-grid-column-two-thirds > form > div.govuk-error-summary > div > div > ul > li > a"
  }

  "WhichExternalFeatureView" must {
    val document: Document = Jsoup.parse(view(address, formProvider(), navBarContent(), NormalMode).body)
    "show correct text" in {
      elementText(Selectors.address)(document) mustBe address
      elementText(Selectors.heading)(document) mustBe "Which external feature have you changed?"
      elementText(Selectors.loadingBays)(document) mustBe "Loading bays"
      elementText(Selectors.lockupGarages)(document) mustBe "Lock-up garages"
      elementText(Selectors.outdoorSeating)(document) mustBe "Outdoor seating"
      elementText(Selectors.parking)(document) mustBe "Parking"
      elementText(Selectors.solarPanels)(document) mustBe "Solar panels"
      elementText(Selectors.other)(document) mustBe "Other external feature"

      val formWithError = formProvider().withError(FormError("value", "whichExternalFeature.error.required"))
      val errorDocument: Document = Jsoup.parse(view(address, formWithError, navBarContent(), NormalMode).body)

      elementText(Selectors.topError)(errorDocument) mustBe "Select which external feature you have changed"
    }

  }

}