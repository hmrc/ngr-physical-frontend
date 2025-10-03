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
import models.upscan.{UpscanFileReference, UpscanInitiateResponse}
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import play.api.data.*
import play.api.data.Forms.*
import views.html.UploadDocumentView

class UploadDocumentViewSpec extends ViewBaseSpec {

  val view: UploadDocumentView = app.injector.instanceOf[UploadDocumentView]
  val address: String = "123 Street Lane"
  val form: Form[String] = {
    Form(single("text" -> nonEmptyText))
  }
  val upscanResponse: UpscanInitiateResponse = UpscanInitiateResponse(UpscanFileReference("ref"), "foo", Map("test" -> "test"))


  object Selectors {
    val firstParagraph = "#para-1"
    val secondParagraph = "#main-content > div > div.govuk-grid-column-two-thirds > p:nth-child(4)"
    val thirdParagraph = "#para-3"
    val fileUploadComponent = "#file"
  }

  "UploadDocument view" must {
    val pageView = view(
      form,
      upscanResponse,
      None,
      Map("accept" -> ".pdf,.png,.docx",
        "data-max-file-size" -> "100000000",
        "data-min-file-size" -> "1000"),
      address,
      navBarContent(),
      false,
      false
    )
    
    lazy implicit val document: Document = Jsoup.parse(pageView.body)


    "show correct header" in {
      elementText("h1") mustBe "Upload"
    }

    "show correct first paragraph" in {
      elementText(Selectors.firstParagraph) mustBe "Files must be PDF, JPG or PNG and must be smaller than 25MB."
    }

    "show correct second paragraph" in {
      elementText(Selectors.secondParagraph) mustBe "You can send other file types or files larger than 25MB to files@voa.gov.uk ."
    }

    "show correct third paragraph" in {
      elementText(Selectors.thirdParagraph) mustBe "Upload a file"
    }

    "contain upload section" in {
      element(Selectors.fileUploadComponent).getAllElements.isEmpty mustBe false
    }

    

  }

}
