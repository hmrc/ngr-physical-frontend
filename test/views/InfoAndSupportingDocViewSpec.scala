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
import views.html.InfoAndSupportingDocView

class InfoAndSupportingDocViewSpec extends ViewBaseSpec {

  val view: InfoAndSupportingDocView = app.injector.instanceOf[InfoAndSupportingDocView]
  val address: String = "123 Street Lane"

  object Selectors {
    val firstSubHeading = "#sub-heading-1"
    val secondSubHeading = "#sub-heading-2"
    val thirdSubHeading = "#sub-heading-3"
    val firstParagraph = "#para-1"
    val secondParagraph = "#para-2"
    val thirdParagraph = "#para-4"
    val fourthParagraph = "#para-6"
    val firstBulletPointSection = "#main-content > div > div > ul:nth-child(7) >"
    val firstBulletPointSection_1 = s"$firstBulletPointSection li:nth-child(1)"
    val firstBulletPointSection_2 = s"$firstBulletPointSection li:nth-child(2)"
    val firstBulletPointSection_3 = s"$firstBulletPointSection li:nth-child(3)"
    val secondBulletPointSection = "#main-content > div > div > ul:nth-child(11)  >"
    val secondBulletPointSection_1 = s"$secondBulletPointSection li:nth-child(1)"
    val secondBulletPointSection_2 = s"$secondBulletPointSection li:nth-child(2)"
    val secondBulletPointSection_3 = s"$secondBulletPointSection li:nth-child(3)"
    val secondBulletPointSection_4 = s"$secondBulletPointSection li:nth-child(4)"
    val helpDetailTitle = "#main-content > div > div > details > summary > span"
    val helpDetail_1 = "#main-content > div > div > details > div > p:nth-child(1)"
    val helpDetail_2 = "#main-content > div > div > details > div > p:nth-child(2)"
    val helpDetail_3 = "#main-content > div > div > details > div > p:nth-child(3)"
    val helpDetail_4 = "#main-content > div > div > details > div > p:nth-child(4)"


  }

  "Dashboard view" must {
    val dashboardView = view(address, navBarContent())
    lazy implicit val document: Document = Jsoup.parse(dashboardView.body)


    "show correct header" in {
      elementText("h1") mustBe "Information and supporting documents you need"
    }

    "show correct first sub heading" in {
      elementText(Selectors.firstSubHeading) mustBe "Information you need"
    }

    "show correct first paragraph" in {
      elementText(Selectors.firstParagraph) mustBe "You need information about the things you changed and what the property is like after the change."
    }

    "show correct second paragraph" in {
      elementText(Selectors.secondParagraph) mustBe "You need the date you completed the change."
    }

    "show correct first bullet point section" in {
      elementText(Selectors.firstBulletPointSection_1) mustBe "the planning application reference, if you got planning permission"
      elementText(Selectors.firstBulletPointSection_2) mustBe "whether you added or removed features"
      elementText(Selectors.firstBulletPointSection_3) mustBe "how much of the property has air conditioning, heating, sprinklers, lifts or escalators"
    }

    "show correct second sub heading" in {
      elementText(Selectors.secondSubHeading) mustBe "Supporting documents you need to upload"
    }

    "show correct third paragraph" in {
      elementText(Selectors.thirdParagraph) mustBe "You must upload at least one document that supports all the changes you tell us about."
    }

    "show correct second bullet point section" in {
      elementText(Selectors.secondBulletPointSection_1) mustBe "plans"
      elementText(Selectors.secondBulletPointSection_2) mustBe "photos"
      elementText(Selectors.secondBulletPointSection_3) mustBe "letters or emails"
      elementText(Selectors.secondBulletPointSection_4) mustBe "project plans or a schedule of works"
    }

    "show correct third sub heading" in {
      elementText(Selectors.thirdSubHeading) mustBe "How your rateable value will change"
    }

    "show correct fourth paragraph" in {
      elementText(Selectors.thirdParagraph) mustBe "You must upload at least one document that supports all the changes you tell us about."
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
