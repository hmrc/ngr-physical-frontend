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

import controllers.routes
import helpers.ViewBaseSpec
import models.upscan.UploadId
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import play.api.mvc.Call
import uk.gov.hmrc.govukfrontend.views.viewmodels.content.HtmlContent
import uk.gov.hmrc.govukfrontend.views.viewmodels.summarylist.{Key, SummaryList, SummaryListRow, Value}
import views.html.UploadedDocumentView

class UploadedDocumentViewSpec extends ViewBaseSpec {

  lazy val view: UploadedDocumentView = inject[UploadedDocumentView]
  val address: String = "123 Street Lane"


  object Selectors {
    val firstParagraph = "#para-1"
    val secondParagraph = "#main-content > div > div.govuk-grid-column-two-thirds > form > p:nth-child(4)"
    val uploadAnotherFileButton = "#main-content > div > div.govuk-grid-column-two-thirds > form > button"
    val continueButton = "#continue"
    val rowKey = "#uploadStatusTable > dl > div:nth-child(1) > dt"
  }


  lazy val summaryListRow: SummaryListRow = SummaryListRow(
    Key(HtmlContent(messages("exampleFile"))),
    Value(HtmlContent(messages("uploadedDocument.uploaded"))),
    classes = "",
    actions = None
  )

  "UploadedDocument view" must {
    val pageView = view(
      navBarContent(),
      SummaryList(Seq(summaryListRow)),
      address,
      false,
      routes.UploadedDocumentController.onSubmit(Some(UploadId("12345")), false)
    )

    lazy implicit val document: Document = Jsoup.parse(pageView.body)

    "show correct header" in {
      elementText("h1") mustBe "Uploaded files"
    }

    "show correct first paragraph" in {
      elementText(Selectors.firstParagraph) mustBe "Files must be PDF, JPG or PNG and must be smaller than 25MB."
    }

    "show correct second paragraph" in {
      elementText(Selectors.secondParagraph) mustBe "You can send other file types or files larger than 25MB to files@voa.gov.uk ."
    }

    "contain upload another file button" in {
      elementText(Selectors.uploadAnotherFileButton) mustBe "Upload another file"
    }

    "contain continue button" in {
      elementText(Selectors.continueButton) mustBe "Continue"
    }

    "contain list section of files" in {
      elementText(Selectors.rowKey) mustBe "exampleFile"
    }

  }
}
