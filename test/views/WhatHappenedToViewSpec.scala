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

import forms.WhatHappenedToFormProvider
import helpers.ViewBaseSpec
import models.{ExternalFeatureGroup1, WhatHappenedTo, NormalMode}
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import views.html.WhatHappenedToView

class WhatHappenedToViewSpec extends ViewBaseSpec {

  val address = "123 street lane"
  val view: WhatHappenedToView = inject[WhatHappenedToView]
  val formProvider: WhatHappenedToFormProvider = inject[WhatHappenedToFormProvider]

  object Selectors {
    val address = "#main-content > div > div.govuk-grid-column-two-thirds > form > span"
    val heading = "#main-content > div > div.govuk-grid-column-two-thirds > form > div > fieldset > legend"
    val add = "#main-content > div > div.govuk-grid-column-two-thirds > form > div > fieldset > div > div:nth-child(1) > label"
    val removeSome = "#main-content > div > div.govuk-grid-column-two-thirds > form > div > fieldset > div > div:nth-child(2) > label"
    val removeAll =  "#main-content > div > div.govuk-grid-column-two-thirds > form > div > fieldset > div > div:nth-child(3) > label"
  }

  "WhatHappenedToView" must {

    ExternalFeatureGroup1.values.foreach { feature =>

      val strings = WhatHappenedTo.messageKeys(feature)
      val action = WhatHappenedTo.submitAction(feature, NormalMode)
      val radioItems = WhatHappenedTo.options(feature)
      implicit val document: Document = Jsoup.parse(view(address, strings, action, radioItems, formProvider(feature), NormalMode, navBarContent()).body)

      s"show correct header: ${feature.toString}" in {
        elementText(Selectors.heading) mustBe messages(strings("title"))
      }
      s"show correct address: ${feature.toString}" in {
        elementText(Selectors.address) mustBe address
      }
      s"show correct add option: ${feature.toString}" in {
        elementText(Selectors.add) mustBe messages(strings("added"))
      }
      s"show correct some remove option: ${feature.toString}" in {
        elementText(Selectors.removeSome) mustBe messages(strings("removedSome"))
      }
      s"show correct all remove option: ${feature.toString}" in {
        elementText(Selectors.removeAll) mustBe messages(strings("removedAll"))
      }
    }

  }
}
