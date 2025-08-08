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

import forms.{ChangeToUseOfSpaceFormProvider}
import helpers.ViewBaseSpec
import models.{ChangeToUseOfSpace, NormalMode}
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import play.api.data.Form
import views.html.ChangeToUseOfSpaceView

class ChangeToUseOfSpaceViewSpec extends ViewBaseSpec {

  val view: ChangeToUseOfSpaceView = app.injector.instanceOf[ChangeToUseOfSpaceView]
  val formProvider: ChangeToUseOfSpaceFormProvider = inject[ChangeToUseOfSpaceFormProvider]

  val address: String = "123 Street Lane"
  object Selectors {


    val selectSpaceHint = "#selectUseOfSpace-hint"
    val selectSpaceChangeSection = "#main-content > div > div.govuk-grid-column-two-thirds > form > div:nth-child(4) > fieldset >"
    val selectSpaceChangeQuestion = s"$selectSpaceChangeSection legend > h1"
    val selectSpaceChangeOption1 = s"$selectSpaceChangeSection div.govuk-checkboxes > div:nth-child(1) > label"
    val selectSpaceChangeOption2 = s"$selectSpaceChangeSection div.govuk-checkboxes > div:nth-child(2) > label"
    val selectSpaceChangeOption3 = s"$selectSpaceChangeSection div.govuk-checkboxes > div:nth-child(3) > label"

    val planningPermSection =   "#main-content > div > div.govuk-grid-column-two-thirds > form > div:nth-child(5) > fieldset"
    val planningPermQuestion = s"$planningPermSection > legend > h1"
    val planningPermOption1 = s"$planningPermSection div > div:nth-child(1) > label"
    val planningPermOption2 = s"$planningPermSection div > div:nth-child(3) > label"
    val planningPermLabel =  "#conditional-hasPlanningPermission > div > label"
    val planningPermHint = "#permissionReference-hint"


    val button = "#main-content > div > div.govuk-grid-column-two-thirds > form > button"

    val topError = "#main-content > div > div.govuk-grid-column-two-thirds > form > div.govuk-error-summary > div > div > ul > li > a"
    val bottomError = "#selectUseOfSpace-error"
  }


  

  def formWithError(error: String): Form[ChangeToUseOfSpace] = formProvider.apply().withError("selectUseOfSpace", error)

  "WhenCompleteChange with errors view" must {
    
    val changeToUseSpaceView = view(address, navBarContent(), formWithError("changeToUseOfSpace.selectUseOfSpace.error.required"), NormalMode)
    lazy implicit val document: Document = Jsoup.parse(changeToUseSpaceView.body)



    "show correct header" in {
      elementText("h1") mustBe "About the change to use of space"
    }

    "show correct section asking about space" in {
      elementText(Selectors.selectSpaceChangeQuestion) mustBe "How did you change the use of space?"
      elementText(Selectors.selectSpaceHint) mustBe "Select all that apply"
      elementText(Selectors.selectSpaceChangeOption1) mustBe "Rearranged the use of space in the property"
      elementText(Selectors.selectSpaceChangeOption2) mustBe "Built an extension"
      elementText(Selectors.selectSpaceChangeOption3) mustBe "Demolished part of the property"
    }

    "contain correct asking about planing permission" in {
      elementText(Selectors.planningPermQuestion) mustBe "Did you get planning permission?"
      elementText(Selectors.planningPermOption1) mustBe "Yes"
      elementText(Selectors.planningPermOption2) mustBe "No"
      elementText(Selectors.planningPermLabel) mustBe "What is the planning application reference?"
      elementText(Selectors.planningPermHint) mustBe "This is the reference your council gave your planning application"
    }

    "contain errors" in {
      elementText(Selectors.topError) mustBe "Select how you changed use of space"
      elementText(Selectors.bottomError) mustBe "Error: Select how you changed use of space"
    }

  }

}
