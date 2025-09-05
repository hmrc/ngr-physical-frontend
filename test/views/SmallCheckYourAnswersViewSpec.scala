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

import forms.SmallCheckYourAnswersFormProvider
import helpers.ViewBaseSpec
import models.{CYAInternal, InternalFeature}
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import uk.gov.hmrc.govukfrontend.views.viewmodels.summarylist.SummaryListRow
import uk.gov.hmrc.govukfrontend.views.viewmodels.summarylist.SummaryList
import uk.gov.hmrc.govukfrontend.views.viewmodels.summarylist.Key
import uk.gov.hmrc.govukfrontend.views.viewmodels.summarylist.Value
import uk.gov.hmrc.govukfrontend.views.viewmodels.content.HtmlContent
import utils.HtmlElementUtils.elementExists
import views.html.SmallCheckYourAnswersView

class SmallCheckYourAnswersViewSpec extends ViewBaseSpec {
  val view: SmallCheckYourAnswersView = app.injector.instanceOf[SmallCheckYourAnswersView]
  val formProvider: SmallCheckYourAnswersFormProvider = inject[SmallCheckYourAnswersFormProvider]
  val address: String = "123 Street Lane"

  lazy val summaryListRow: SummaryListRow = SummaryListRow(
    key = Key(HtmlContent(messages("externalFeature.landHardSurfacedFenced"))),
    value = Value(HtmlContent(messages("externalFeature.added"))),
    classes = "",
    actions = None
  )

  object Selectors {
    val address = "#main-content > div > div.govuk-grid-column-two-thirds > form > span"
    val heading = "#main-content > div > div.govuk-grid-column-two-thirds > form > h1"
    val another = "#main-content > div > div.govuk-grid-column-two-thirds > form > div > fieldset > legend"
    val yes = "#main-content > div > div.govuk-grid-column-two-thirds > form > div > fieldset > div > div:nth-child(1) > label"
    val no = "#main-content > div > div.govuk-grid-column-two-thirds > form > div > fieldset > div > div:nth-child(2) > label"
    val continue = "#continue"
    val rowKey = "#main-content > div > div.govuk-grid-column-two-thirds > form > dl > div:nth-child(1) > dt"
    val rowValue = "#main-content > div > div.govuk-grid-column-two-thirds > form > dl > div:nth-child(1) > dd.govuk-summary-list__value"
    val noChanges = "#main-content > div > div.govuk-grid-column-two-thirds > form > p"

  }

  "SmallCheckYourAnswersView" must {
    "show correct text for one row" in {
      val document: Document = Jsoup.parse(view(viewType = CYAInternal, address = address, list = SummaryList(Seq(summaryListRow)), form = formProvider(CYAInternal), navigationBarContent = navBarContent()).body)
      elementText(Selectors.address)(document) mustBe address
      elementText(Selectors.heading)(document) mustBe "Check and confirm changes to internal features"
      elementText(Selectors.another)(document) mustBe "Do you want to tell us about another internal feature?"
      elementText(Selectors.yes)(document) mustBe "Yes"
      elementText(Selectors.no)(document) mustBe "No"
      elementText(Selectors.continue)(document) mustBe "Continue"
      elementText(Selectors.rowKey)(document) mustBe "Hard-surfaced, fenced land"
      elementText(Selectors.rowValue)(document) mustBe "Added"
      elementExists(Selectors.noChanges)(document) mustBe false
    }

    "show correct text for no rows" in {
      val document: Document = Jsoup.parse(view(viewType = CYAInternal, address = address, list = SummaryList(Seq.empty), form = formProvider(CYAInternal), navigationBarContent = navBarContent()).body)
      elementText(Selectors.address)(document) mustBe address
      elementText(Selectors.heading)(document) mustBe "Check and confirm changes to internal features"
      elementText(Selectors.another)(document) mustBe "Do you want to tell us about another internal feature?"
      elementText(Selectors.yes)(document) mustBe "Yes"
      elementText(Selectors.no)(document) mustBe "No"
      elementText(Selectors.continue)(document) mustBe "Continue"
      elementExists(Selectors.rowKey)(document) mustBe false
      elementExists(Selectors.rowValue)(document) mustBe false
      elementText(Selectors.noChanges)(document) mustBe "You have no changes to internal features."
    }
  }

}
