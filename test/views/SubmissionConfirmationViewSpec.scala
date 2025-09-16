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
import views.html.SubmissionConfirmationView

class SubmissionConfirmationViewSpec extends ViewBaseSpec {
  val view: SubmissionConfirmationView = inject[SubmissionConfirmationView]
  val address = "123 street lane"
  val ref = "1234-1234-1234"

  object Selectors {
    val heading = "#main-content > div > div.govuk-grid-column-two-thirds > div > h1"
    val reference = "#main-content > div > div.govuk-grid-column-two-thirds > div > div"
    val print = "#printPage > a"
    val addressKey = "#main-content > div > div.govuk-grid-column-two-thirds > dl > div > dt"
    val addressValue = "#main-content > div > div.govuk-grid-column-two-thirds > dl > div > dd"
    val whatHappens = "#main-content > div > div.govuk-grid-column-two-thirds > h2"
    val p1 = "#main-content > div > div.govuk-grid-column-two-thirds > p:nth-child(5)"
    val p2 = "#main-content > div > div.govuk-grid-column-two-thirds > p:nth-child(6)"
    val p3 = "#main-content > div > div.govuk-grid-column-two-thirds > p:nth-child(7)"
    val p4 = "#main-content > div > div.govuk-grid-column-two-thirds > p:nth-child(8)"
    val goHome = "#main-content > div > div.govuk-grid-column-two-thirds > a"
  }

  "SubmissionConfirmationView" must {
    val dashboardView = view(address, ref, navBarContent())
    lazy implicit val document: Document = Jsoup.parse(dashboardView.body)

    "show correct text" in {
      elementText(Selectors.heading) mustBe "Property change details sent"
      elementText(Selectors.reference) mustBe s"Your reference is $ref"
      elementText(Selectors.print) mustBe "Print this page"
      elementText(Selectors.addressKey) mustBe "Address"
      elementText(Selectors.addressValue) mustBe address
      elementText(Selectors.whatHappens) mustBe "What happens next"
      elementText(Selectors.p1) mustBe "We will review the information you sent us. We will contact you if we need to clarify anything or need more details."
      elementText(Selectors.p2) mustBe "We will contact you with our decision about your rateable value - usually within 90 days. Your rateable value may go up, down or stay the same."
      elementText(Selectors.p3) mustBe "We will update the details we hold about your property when we let you know about the rateable value."
      elementText(Selectors.p4) mustBe "We will tell your local council about your new rateable value. A change in rateable value may affect the business rates you pay."
      elementText(Selectors.goHome) mustBe "Go to your account home"
    }

  }
}
