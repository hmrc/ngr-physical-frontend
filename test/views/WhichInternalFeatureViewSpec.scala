package views

import forms.WhichInternalFeatureFormProvider
import helpers.ViewBaseSpec
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import play.api.data.FormError
import views.html.WhichInternalFeatureView

class WhichInternalFeatureViewSpec extends ViewBaseSpec {
  val view: WhichInternalFeatureView = inject[WhichInternalFeatureView]
  val address: String = "123 Street Lane"
  val formProvider: WhichInternalFeatureFormProvider = inject[WhichInternalFeatureFormProvider]

  object Selectors {
    val address = "#main-content > div > div.govuk-grid-column-two-thirds > form > span"
    val heading = "#main-content > div > div.govuk-grid-column-two-thirds > form > h1"
    def bullet(child: Int): String = s"#main-content > div > div.govuk-grid-column-two-thirds > form > div > fieldset > div > div:nth-child($child) > label"
    val airCon: String = bullet(1)
    val escalator: String = bullet(2)
    val goodsLift: String = bullet(3)
    val passengerLift: String = bullet(4)
    val securityCamera: String = bullet(5)
    val other: String = bullet(6)
    val topError: String = "#main-content > div > div.govuk-grid-column-two-thirds > form > div.govuk-error-summary > div > div > ul > li > a"
  }

  "WhichInternalFeatureView" must {
    val document: Document = Jsoup.parse(view(address, formProvider(), navBarContent()).body)
    "show correct text" in {
      elementText(Selectors.address)(document) mustBe address
      elementText(Selectors.heading)(document) mustBe "Which internal feature have you changed?"
      elementText(Selectors.airCon)(document) mustBe "Air conditioning"
      elementText(Selectors.escalator)(document) mustBe "Escalators"
      elementText(Selectors.goodsLift)(document) mustBe "Goods lifts"
      elementText(Selectors.passengerLift)(document) mustBe "Passenger lifts"
      elementText(Selectors.securityCamera)(document) mustBe "Security cameras"
      elementText(Selectors.other)(document) mustBe "Other internal feature"

      val formWithError = formProvider().withError(FormError("value", "whichInternalFeature.error.required"))
      val errorDocument: Document = Jsoup.parse(view(address, formWithError, navBarContent()).body)

      elementText(Selectors.topError)(errorDocument) mustBe "Select which internal feature you have changed"
    }

  }

}
