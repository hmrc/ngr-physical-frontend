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

import forms.SecurityCamerasChangeFormProvider
import helpers.ViewBaseSpec
import models.NormalMode
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import views.html.SecurityCamerasChangeView

class SecurityCamerasChangeViewSpec extends ViewBaseSpec {
  val view: SecurityCamerasChangeView = inject[SecurityCamerasChangeView]
  val address: String = "123 Street Lane"
  val formProvider: SecurityCamerasChangeFormProvider = inject[SecurityCamerasChangeFormProvider]
  implicit val document: Document = Jsoup.parse(view(address, formProvider(), NormalMode, navBarContent()).body)

  object Selectors {
    val address = "#main-content > div > div.govuk-grid-column-two-thirds > form > span"
    val heading = "#main-content > div > div.govuk-grid-column-two-thirds > form > div > h1 > label"
    val input = "#value"
    val button = "#continue"
  }

  "SecurityCamerasChangeView" must {
    "show correct content" in {
      elementText(Selectors.address) mustBe "123 Street Lane"
      elementText(Selectors.heading) mustBe "How many security cameras are there inside the property?"
    }
  }
}
