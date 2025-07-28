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

package controllers


import helpers.{ControllerSpecSupport, TestData}
import play.api.mvc.*
import play.api.test.FakeRequest
import play.api.test.Helpers.*
import views.html.InfoAndSupportingDocView

import scala.concurrent.Future

class InfoAndSupportingDocControllerSpec extends ControllerSpecSupport with TestData {

  lazy val view: InfoAndSupportingDocView = inject[InfoAndSupportingDocView]
  private val fakeRequest = FakeRequest("GET", "/information-and-supporting-documents-need")

  def controller() = new InfoAndSupportingDocController(
    mcc,
    view,
    mockAuthJourney, 
    mockIsRegisteredCheck
  )(mockConfig)

  val pageTitle = "Information and supporting documents you need"
  val contentP = "You need information about the things you changed and what the property is like after the change."

  "Dashboard Controller" must {
    "method show" must {
      "Return OK and the correct view" in {
        val result: Future[Result] = controller().show(authenticatedFakeRequest)
        status(result) mustBe OK
        val content = contentAsString(result)
        content must include(pageTitle)
        content must include(contentP)
      }
    }
  }
}
