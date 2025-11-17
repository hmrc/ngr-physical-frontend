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

import base.SpecBase
import helpers.{ControllerSpecSupport, TestData}
import models.UserAnswers
import models.registration.CredId
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.when
import play.api.mvc.Result
import play.api.test.FakeRequest
import play.api.test.Helpers.*
import views.html.{InfoAndSupportingDocView, SupportingDocumentsView}

import scala.concurrent.Future

class SupportingDocumentsControllerSpec extends ControllerSpecSupport with TestData {

  lazy val view: SupportingDocumentsView = inject[SupportingDocumentsView]
  private val fakeRequest = FakeRequest("GET", "/supporting-documents")

  def controller(userAnswers: Option[UserAnswers]) = new SupportingDocumentsController(
     identify = fakeAuth, getData = fakeData(userAnswers), requireData = fakeRequireData(userAnswers), controllerComponents = mcc, view = view
  )(mockConfig)

  val pageTitle = "Supporting documents"
  val contentP = "You should upload at least one document to support the changes you told us about. You can upload more documents if you have them."

  when(mockNGRConnector.getLinkedProperty(any[CredId])(any())).thenReturn(Future.successful(Some(property)))

  "Supporting Documents Controller" must {
    "method show" must {
      "Return OK and the correct view" in {
        val result: Future[Result] = controller(Some(emptyUserAnswers)).onPageLoad(assessmentId)(fakeRequest)
        status(result) mustBe OK
        val content = contentAsString(result)
        content must include(pageTitle)
        content must include(contentP)
      }

      "redirect to Journey Recovery for a GET if no existing data is found" in {
        val result = controller(None).onPageLoad(assessmentId)(fakeRequest)
        status(result) mustBe 303
        redirectLocation(result).value mustBe controllers.routes.JourneyRecoveryController.onPageLoad(assessmentId).url
      }
    }

    "method next" must {
      "Return SEE_OTHER and the correct view" in {
        val result: Future[Result] = controller(Some(emptyUserAnswers)).next(assessmentId)(fakeRequest)
        status(result) mustBe SEE_OTHER
        redirectLocation(result) mustBe Some(s"/ngr-physical-frontend/upload-supporting-document/$assessmentId")
      }
    }
  }
}
