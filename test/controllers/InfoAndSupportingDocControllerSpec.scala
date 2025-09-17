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
import models.{NormalMode, UserAnswers}
import models.registration.CredId
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.when
import play.api.mvc.*
import play.api.test.FakeRequest
import play.api.test.Helpers.*
import views.html.{DeclarationView, InfoAndSupportingDocView}

import scala.concurrent.Future

class InfoAndSupportingDocControllerSpec extends ControllerSpecSupport with TestData {

  lazy val view: InfoAndSupportingDocView = inject[InfoAndSupportingDocView]
  private val fakeRequest = FakeRequest("GET", "/information-and-supporting-documents-need")

  def controller(userAnswers: Option[UserAnswers]) = new InfoAndSupportingDocController(
    mcc,
    view,
    fakeAuth,
    fakeReg,
    fakeData(userAnswers),
    fakeRequireData(userAnswers)
  )(mockConfig)

  val pageTitle = "Information and supporting documents you need"
  val contentP = "You need information about the things you changed and what the property is like after the change."

  "Info and supporting Controller" must {
    "method show" must {
      "Return OK and the correct view" in {
        val result: Future[Result] = controller(Some(emptyUserAnswers)).show(fakeRequest)
        status(result) mustBe OK
        val content = contentAsString(result)
        content must include(pageTitle)
        content must include(contentP)
      }

      "redirect to Journey Recovery when no user data" in {
        val result: Future[Result] = controller(None).show(fakeRequest)
        status(result) mustBe SEE_OTHER
        redirectLocation(result).value mustBe controllers.routes.JourneyRecoveryController.onPageLoad().url
      }
    }

    "next" must {
      "redirect to Changed Features or Space page" in {
        when(mockSessionRepository.set(any())).thenReturn(Future.successful(true))
        val result: Future[Result] = controller(Some(emptyUserAnswers)).next(fakeRequest)
        status(result) mustBe SEE_OTHER
        redirectLocation(result).value mustBe routes.WhenCompleteChangeController.onPageLoad(NormalMode).url
      }

      "redirect to Journey Recovery when no user data" in {
        val result: Future[Result] = controller(None).next(fakeRequest)
        status(result) mustBe SEE_OTHER
        redirectLocation(result).value mustBe controllers.routes.JourneyRecoveryController.onPageLoad().url
      }
    }
  }
}
