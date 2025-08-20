package views

import forms.SmallCheckYourAnswersFormProvider
import helpers.ViewBaseSpec
import models.{CYAInternal, InternalFeature}
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import uk.gov.hmrc.govukfrontend.views.viewmodels.summarylist.SummaryList
import views.html.SmallCheckYourAnswersView

class SmallCheckYourAnswersViewSpec extends ViewBaseSpec {
  val view: SmallCheckYourAnswersView = app.injector.instanceOf[SmallCheckYourAnswersView]
  val formProvider: SmallCheckYourAnswersFormProvider = inject[SmallCheckYourAnswersFormProvider]
  val address: String = "123 Street Lane"

  object Selectors {
    val address = "#main-content > div > div.govuk-grid-column-two-thirds > form > span"
    val heading = "#main-content > div > div.govuk-grid-column-two-thirds > form > h1"
    val another = "#main-content > div > div.govuk-grid-column-two-thirds > form > div > fieldset > legend"
    val yes = "#main-content > div > div.govuk-grid-column-two-thirds > form > div > fieldset > div > div:nth-child(1) > label"
    val no = "#main-content > div > div.govuk-grid-column-two-thirds > form > div > fieldset > div > div:nth-child(2) > label"
    val continue = "#continue"
  }

  "SmallCheckYourAnswersView" must {
    "show correct text" in {
      val document: Document = Jsoup.parse(view(viewType = CYAInternal, address = address, rows = SummaryList(), form = formProvider(), navigationBarContent = navBarContent()).body)
      elementText(Selectors.address)(document) mustBe address
      elementText(Selectors.heading)(document) mustBe "Check and confirm changes to internal features"
      elementText(Selectors.another)(document) mustBe "Do you want to tell us about another internal feature?"
      elementText(Selectors.yes)(document) mustBe "Yes"
      elementText(Selectors.no)(document) mustBe "No"
      elementText(Selectors.continue)(document) mustBe "Continue"
    }
  }

}
