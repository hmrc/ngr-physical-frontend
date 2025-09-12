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

import forms.WhenCompleteChangeFormProvider
import helpers.ViewBaseSpec
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import views.html.WhenCompleteChangeView
import models.NormalMode
import play.api.data.Form

import java.time.LocalDate

class WhenCompleteChangeViewSpec extends ViewBaseSpec {

  val view: WhenCompleteChangeView = app.injector.instanceOf[WhenCompleteChangeView]
  val formProvider: WhenCompleteChangeFormProvider = inject[WhenCompleteChangeFormProvider]
  val address: String = "123 Street Lane"

  object Selectors {
    val h1 = "#main-content > div > div.govuk-grid-column-two-thirds > form > h1"
    val dateHintText = """#value-hint"""
    val inputDay = """#value\.day"""
    val inputMonth = """#value\.month"""
    val inputYear = """#value\.day"""
    val topError = "#main-content > div > div.govuk-grid-column-two-thirds > form > div.govuk-error-summary > div > div > ul > li > a"
    val bottomError = "#value-error"
  }


  

  def formWithError(error: String): Form[LocalDate] = formProvider.apply().withError("value", error)

  "WhenCompleteChange with errors view" must {
    
    val whenCompleteChangeView = view(address, formWithError("whenCompleteChange.error.invalid"), NormalMode, navBarContent())
    lazy implicit val document: Document = Jsoup.parse(whenCompleteChangeView.body)
    
    
    
    "show correct header" in {
      elementText(Selectors.h1) mustBe "When did you complete the change?"
    }

    "show correct hint Text" in {
      elementText(Selectors.dateHintText) mustBe "For example, 27 6 2026."
    }

    "contain inputs" in {
      element(Selectors.inputDay).getAllElements.isEmpty mustBe false
      element(Selectors.inputMonth).getAllElements.isEmpty mustBe false
      element(Selectors.inputYear).getAllElements.isEmpty mustBe false
    }

    "contain errors" in {
      elementText(Selectors.topError) mustBe "When you completed the change must be a real date."
      elementText(Selectors.bottomError) mustBe "Error: When you completed the change must be a real date."
    }

  }

}
