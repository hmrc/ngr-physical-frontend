package views

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import helpers.ViewBaseSpec
import views.html.ChangedFeaturesOrSpaceView


class ChangedFeaturesOrSpaceViewSpec extends ViewBaseSpec {
  val view: ChangedFeaturesOrSpaceView = injector.instanceOf[ChangedFeaturesOrSpaceView]
  val address: String = "123 Street Lane"
  implicit val document: Document = Jsoup.parse(view(address, navBarContent()).body)
  val title = "Tell us you changed property features or use of space"
  val p1 = "We use information about certain things in your property to work out the rateable value. A change to them could affect the rateable value."
  val p2 = "You must tell us within 60 days of completing a change."
  val div1 = "If you made a change that is not shown on this page, you do not need to tell us about it."
  val h21 = "Use of space"
  val p3 = "You only need to tell us if you:"
  val ul1li1 = "changed the internal layout"
  val ul1li2 = "moved or removed internal walls"
  val ul1li3 = "built an extension"
  val ul1li4 = "demolished part of the property"
  val h22 = "Internal features"
  val p4 = "You only need to tell us you changed:"
  val ul2li1 = "air conditioning"
  val ul2li2 = "compressed air systems"
  val ul2li3 = "escalators"
  val ul2li4 = "heating"
  val ul2li5 = "lifts"
  val ul2li6 = "security cameras"
  val ul2li7 = "sprinklers"
  val h23 = "External features"
  val p5 = "You only need to tell us you changed:"
  val ul3li1 = "advertising displays on your property"
  val ul3li2 = "bike sheds"
  val ul3li3 = "canopies"
  val ul3li4 = "land"
  val ul3li5 = "loading bays"
  val ul3li6 = "lock-up garages"
  val ul3li7 = "outdoor seating"
  val ul3li8 = "parking"
  val ul3li9 = "portable buildings"
  val ul3li10 = "shipping containers"
  val ul3li11 = "solar panels"

  object Selectors {
    val title = "#main-content > div > div.govuk-grid-column-two-thirds > form > h1"
    val p1 = "#main-content > div > div.govuk-grid-column-two-thirds > form > p:nth-child(3)"
    val p2 = "#main-content > div > div.govuk-grid-column-two-thirds > form > p:nth-child(4)"
    val div1 = "#main-content > div > div.govuk-grid-column-two-thirds > form > div"
    val h21 = "#main-content > div > div.govuk-grid-column-two-thirds > form > h2:nth-child(6)"
    val p3 = "#main-content > div > div.govuk-grid-column-two-thirds > form > p:nth-child(7)"
    val ul1li1 = "#main-content > div > div.govuk-grid-column-two-thirds > form > ul:nth-child(8) > li:nth-child(1)"
    val ul1li2 = "#main-content > div > div.govuk-grid-column-two-thirds > form > ul:nth-child(8) > li:nth-child(2)"
    val ul1li3 = "#main-content > div > div.govuk-grid-column-two-thirds > form > ul:nth-child(8) > li:nth-child(3)"
    val ul1li4 = "#main-content > div > div.govuk-grid-column-two-thirds > form > ul:nth-child(8) > li:nth-child(4)"
    val h22 = "#main-content > div > div.govuk-grid-column-two-thirds > form > h2:nth-child(9)"
    val p4 = "#main-content > div > div.govuk-grid-column-two-thirds > form > p:nth-child(10)"
    val ul2li1 = "#main-content > div > div.govuk-grid-column-two-thirds > form > ul:nth-child(11) > li:nth-child(1)"
    val ul2li2 = "#main-content > div > div.govuk-grid-column-two-thirds > form > ul:nth-child(11) > li:nth-child(2)"
    val ul2li3 = "#main-content > div > div.govuk-grid-column-two-thirds > form > ul:nth-child(11) > li:nth-child(3)"
    val ul2li4 = "#main-content > div > div.govuk-grid-column-two-thirds > form > ul:nth-child(11) > li:nth-child(4)"
    val ul2li5 = "#main-content > div > div.govuk-grid-column-two-thirds > form > ul:nth-child(11) > li:nth-child(5)"
    val ul2li6 = "#main-content > div > div.govuk-grid-column-two-thirds > form > ul:nth-child(11) > li:nth-child(6)"
    val ul2li7 = "#main-content > div > div.govuk-grid-column-two-thirds > form > ul:nth-child(11) > li:nth-child(7)"
    val h23 = "#main-content > div > div.govuk-grid-column-two-thirds > form > h2:nth-child(12)"
    val p5 = "#main-content > div > div.govuk-grid-column-two-thirds > form > p:nth-child(13)"
    val ul3li1 = "#main-content > div > div.govuk-grid-column-two-thirds > form > ul:nth-child(14) > li:nth-child(1)"
    val ul3li2 = "#main-content > div > div.govuk-grid-column-two-thirds > form > ul:nth-child(14) > li:nth-child(2)"
    val ul3li3 = "#main-content > div > div.govuk-grid-column-two-thirds > form > ul:nth-child(14) > li:nth-child(3)"
    val ul3li4 = "#main-content > div > div.govuk-grid-column-two-thirds > form > ul:nth-child(14) > li:nth-child(4)"
    val ul3li5 = "#main-content > div > div.govuk-grid-column-two-thirds > form > ul:nth-child(14) > li:nth-child(5)"
    val ul3li6 = "#main-content > div > div.govuk-grid-column-two-thirds > form > ul:nth-child(14) > li:nth-child(6)"
    val ul3li7 = "#main-content > div > div.govuk-grid-column-two-thirds > form > ul:nth-child(14) > li:nth-child(7)"
    val ul3li8 = "#main-content > div > div.govuk-grid-column-two-thirds > form > ul:nth-child(14) > li:nth-child(8)"
    val ul3li9 = "#main-content > div > div.govuk-grid-column-two-thirds > form > ul:nth-child(14) > li:nth-child(9)"
    val ul3li10 = "#main-content > div > div.govuk-grid-column-two-thirds > form > ul:nth-child(14) > li:nth-child(10)"
    val ul3li11 = "#main-content > div > div.govuk-grid-column-two-thirds > form > ul:nth-child(14) > li:nth-child(11)"
  }

  "ChangedFeaturesOfSpaceView" must {
    "Show correct title" in {
      elementText(Selectors.title) mustBe title
    }
    "Show correct p1" in {
      elementText(Selectors.p1) mustBe p1
    }
    "Show correct p2" in {
      elementText(Selectors.p2) mustBe p2
    }
    "Show correct div1" in {
      elementText(Selectors.div1) mustBe div1
    }
    "Show correct h21" in {
      elementText(Selectors.h21) mustBe h21
    }
    "Show correct p3" in {
      elementText(Selectors.p3) mustBe p3
    }
    "Show correct ul1" in {
      elementText(Selectors.ul1li1) mustBe ul1li1
      elementText(Selectors.ul1li2) mustBe ul1li2
      elementText(Selectors.ul1li3) mustBe ul1li3
      elementText(Selectors.ul1li4) mustBe ul1li4
    }
    "Show correct h22" in {
      elementText(Selectors.h22) mustBe h22
    }
    "Show correct p4" in {
      elementText(Selectors.p4) mustBe p4
    }
    "Show correct ul2" in {
      elementText(Selectors.ul2li1) mustBe ul2li1
      elementText(Selectors.ul2li2) mustBe ul2li2
      elementText(Selectors.ul2li3) mustBe ul2li3
      elementText(Selectors.ul2li4) mustBe ul2li4
      elementText(Selectors.ul2li5) mustBe ul2li5
      elementText(Selectors.ul2li6) mustBe ul2li6
      elementText(Selectors.ul2li7) mustBe ul2li7
    }
    "Show correct h23" in {
      elementText(Selectors.h23) mustBe h23
    }
    "Show correct p5" in {
      elementText(Selectors.p5) mustBe p5
    }
    "Show correct ul3" in {
      elementText(Selectors.ul3li1) mustBe ul3li1
      elementText(Selectors.ul3li2) mustBe ul3li2
      elementText(Selectors.ul3li3) mustBe ul3li3
      elementText(Selectors.ul3li4) mustBe ul3li4
      elementText(Selectors.ul3li5) mustBe ul3li5
      elementText(Selectors.ul3li6) mustBe ul3li6
      elementText(Selectors.ul3li7) mustBe ul3li7
      elementText(Selectors.ul3li8) mustBe ul3li8
      elementText(Selectors.ul3li9) mustBe ul3li9
      elementText(Selectors.ul3li10) mustBe ul3li10
      elementText(Selectors.ul3li11) mustBe ul3li11
    }
  }
}
