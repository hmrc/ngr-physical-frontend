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

import helpers.ViewBaseSpec
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import views.html.SupportingDocumentsView

class SupportingDocumentsViewSpec extends ViewBaseSpec {

  val view: SupportingDocumentsView = app.injector.instanceOf[SupportingDocumentsView]
  val address: String = "123 Street Lane"

  object Selectors {
    val firstParagraph = "#para-1"
    val secondParagraph = "#para-2"
    val thirdParagraph = "#para-3"
    val bulletPointSection = "#main-content > div > div.govuk-grid-column-two-thirds > form > ul >"
    val bulletPointSection_1 = s"$bulletPointSection li:nth-child(1)"
    val bulletPointSection_2 = s"$bulletPointSection li:nth-child(2)"
    val bulletPointSection_3 = s"$bulletPointSection li:nth-child(3)"
    val bulletPointSection_4 = s"$bulletPointSection li:nth-child(4)"
    val helpDetailTitle = "#help-supporting-docs-dropdown > summary > span"
    val helpDetail_1 = "#detail-para-1"
    val helpDetail_2 = "#detail-para-2"
    val helpDetail_3 = "#detail-para-3"
    val helpDetail_4 = "#detail-para-4"

  }

  "SupportingDocuments view" must {
    val dashboardView = view(address, navBarContent())
    lazy implicit val document: Document = Jsoup.parse(dashboardView.body)


    "show correct header" in {
      elementText("h1") mustBe "Supporting documents"
    }

    "show correct first paragraph" in {
      elementText(Selectors.firstParagraph) mustBe "You should upload at least one document to support the changes you told us about. You can upload more documents if you have them."
    }

    "show correct second paragraph" in {
      elementText(Selectors.secondParagraph) mustBe "You can upload:"
    }

    "show correct first bullet point section" in {
      elementText(Selectors.bulletPointSection_1) mustBe "plans"
      elementText(Selectors.bulletPointSection_2) mustBe "photos"
      elementText(Selectors.bulletPointSection_3) mustBe "letters or emails"
      elementText(Selectors.bulletPointSection_4) mustBe "project plans or a schedule of works"
    }

    "show correct third paragraph" in {
      elementText(Selectors.thirdParagraph) mustBe "If you do not have any of the documents listed you can upload any other document that supports the changes."
    }

    "show correct help detail section" in {
      elementText(Selectors.helpDetailTitle) mustBe "Help with supporting documents"
      elementText(Selectors.helpDetail_1) mustBe "Plans can be drawings with measurements, plans drawn to scale or your own sketch. Your architect or builder may be able to give you a copy of your plans."
      elementText(Selectors.helpDetail_2) mustBe "Photos must show what the property was like after the changes."
      elementText(Selectors.helpDetail_3) mustBe "Letters or emails must be about the changes. For example, receipts from suppliers or confirmation from your builder about the work they have done."
      elementText(Selectors.helpDetail_4) mustBe "Project plans or a schedule of works need to show what work was done when."
    }

  }

}
