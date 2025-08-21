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

import forms.HowMuchOfPropertyFormProvider
import helpers.ViewBaseSpec
import models.{HowMuchOfProperty, InternalFeatureGroup1, NormalMode}
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import views.html.HowMuchOfPropertyView

class HowMuchOfPropertyViewSpec extends ViewBaseSpec {
  val view: HowMuchOfPropertyView = inject[HowMuchOfPropertyView]
  val address = "123 street lane"
  val formProvider = HowMuchOfPropertyFormProvider()

  object Selectors {
    val address = "#main-content > div > div.govuk-grid-column-two-thirds > form > span"
    val heading = "#main-content > div > div.govuk-grid-column-two-thirds > form > div > fieldset > legend"
    val hint = "#value-hint"
    val allOf =  "#main-content > div > div.govuk-grid-column-two-thirds > form > div > fieldset > div.govuk-radios > div:nth-child(1) > label"
    val someOf = "#main-content > div > div.govuk-grid-column-two-thirds > form > div > fieldset > div.govuk-radios > div:nth-child(2) > label"
    val noneOf = "#main-content > div > div.govuk-grid-column-two-thirds > form > div > fieldset > div.govuk-radios > div:nth-child(3) > label"
    val button = "#main-content > div > div.govuk-grid-column-two-thirds > form > button"
  }

  
  "HowMuchOfPropertyView" must {
    
    InternalFeatureGroup1.values.foreach { feature =>
      
      val strings = HowMuchOfProperty.messageKeys(feature)
      val action = HowMuchOfProperty.submitAction(feature, NormalMode)
      val radioItems = HowMuchOfProperty.options(feature)
      implicit val document: Document = Jsoup.parse(view(address, strings, action, radioItems, formProvider(feature), NormalMode, navBarContent()).body)
      
      s"show correct header: ${feature.toString}" in {
        elementText(Selectors.heading) mustBe messages(strings("title"))
      }
      s"show correct address: ${feature.toString}" in {
        elementText(Selectors.address) mustBe address
      }
      s"show correct hiny: ${feature.toString}" in {
        elementText(Selectors.hint) mustBe messages(strings("hint"))
      }
      s"show correct all: ${feature.toString}" in {
        elementText(Selectors.allOf) mustBe messages(strings("all"))
      }
      s"show correct some: ${feature.toString}" in {
        elementText(Selectors.someOf) mustBe messages(strings("some"))
      }
      s"show correct none: ${feature.toString}" in {
        elementText(Selectors.noneOf) mustBe messages(strings("none"))
      }

    }

  }
}
