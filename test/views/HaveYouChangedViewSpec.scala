package views

import forms.HaveYouChangedFormProvider
import helpers.ViewBaseSpec
import models.NavBarPageContents.createDefaultNavBar
import models.{External, Internal, NormalMode, Space}
import org.jsoup.Jsoup
import play.api.data.Form
import views.html.HaveYouChangedView

class HaveYouChangedViewSpec extends ViewBaseSpec {
  val view: HaveYouChangedView = inject[HaveYouChangedView]
  val formProvider: HaveYouChangedFormProvider = inject[HaveYouChangedFormProvider]
  val form: Form[Boolean] = formProvider.apply()

  object Selectors {
    val address = "#main-content > div > div.govuk-grid-column-two-thirds > form > span"
    val heading = "#main-content > div > div.govuk-grid-column-two-thirds > form > div > fieldset > legend > h1"
    val hint = "#value-hint"
    val yes = "#main-content > div > div.govuk-grid-column-two-thirds > form > div > fieldset > div.govuk-radios > div:nth-child(1) > label"
    val no = "#main-content > div > div.govuk-grid-column-two-thirds > form > div > fieldset > div.govuk-radios > div:nth-child(2) > label"
    val button = "#main-content > div > div.govuk-grid-column-two-thirds > form > button"
  }

  "HaveYouChangedView" must {
    "show correct strings" when {
      "space mode" in {
        val document = Jsoup.parse(view("123 street", "haveYouChangedSpace.title", "haveYouChangedSpace.hint", form, Space, NormalMode, createDefaultNavBar()).body)
        elementText(Selectors.address)(document) mustBe "123 street"
        elementText(Selectors.heading)(document) mustBe "Have you changed use of space?"
        elementText(Selectors.hint)(document) mustBe "For example, you increased the size of the retail area by building an extension."
        elementText(Selectors.yes)(document) mustBe "Yes"
        elementText(Selectors.no)(document) mustBe "No"
        elementText(Selectors.button)(document) mustBe "Save and continue"
      }
      "internal mode" in {
        val document = Jsoup.parse(view("123 street", "haveYouChangedInternal.title", "haveYouChangedInternal.hint", form, Internal, NormalMode, createDefaultNavBar()).body)
        elementText(Selectors.address)(document) mustBe "123 street"
        elementText(Selectors.heading)(document) mustBe "Have you changed internal features?"
        elementText(Selectors.hint)(document) mustBe "For example you added air conditioning and removed sprinklers."
        elementText(Selectors.yes)(document) mustBe "Yes"
        elementText(Selectors.no)(document) mustBe "No"
        elementText(Selectors.button)(document) mustBe "Save and continue"
      }
      "external mode" in {
        val document = Jsoup.parse(view("123 street", "haveYouChangedExternal.title", "haveYouChangedExternal.hint", form, External, NormalMode, createDefaultNavBar()).body)
        elementText(Selectors.address)(document) mustBe "123 street"
        elementText(Selectors.heading)(document) mustBe "Have you changed external features?"
        elementText(Selectors.hint)(document) mustBe "For example you added parking and removed lock-up garages."
        elementText(Selectors.yes)(document) mustBe "Yes"
        elementText(Selectors.no)(document) mustBe "No"
        elementText(Selectors.button)(document) mustBe "Save and continue"
      }
    }
  }

}
