package views

import forms.AnythingElseFormProvider
import helpers.ViewBaseSpec
import models.NormalMode
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import views.html.AnythingElseView

class AnythingElseViewSpec extends ViewBaseSpec{
  val view: AnythingElseView = inject[AnythingElseView]
  val address = "123 street lane"
  val formProvider: AnythingElseFormProvider = inject[AnythingElseFormProvider]
  implicit val document: Document = Jsoup.parse(view(address = address, form = formProvider(), navigationBarContent = navBarContent(), mode = NormalMode).body)

  object Selectors {
    val address = "#main-content > div > div.govuk-grid-column-two-thirds > form > span"
    val heading = "#main-content > div > div.govuk-grid-column-two-thirds > form > h1"
    val yes = "#main-content > div > div.govuk-grid-column-two-thirds > form > div > fieldset > div > div:nth-child(1) > label"
    val no = "#main-content > div > div.govuk-grid-column-two-thirds > form > div > fieldset > div > div:nth-child(3) > label"
    val inputTitle = "#conditional-value > div > label"
    val continue = "#continue"
  }

  "AnythingElseView" must {
    "show correct text" in {
      elementText(Selectors.address) mustBe address
      elementText(Selectors.heading) mustBe "Is there anything else you want to tell us about the changes?"
      elementText(Selectors.yes) mustBe "Yes"
      elementText(Selectors.no) mustBe "No"
      elementText(Selectors.inputTitle) mustBe "What do you want to tell us?"
      elementText(Selectors.continue) mustBe "Continue"
    }
  }
}
