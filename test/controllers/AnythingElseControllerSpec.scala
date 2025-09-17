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

import forms.AnythingElseFormProvider
import helpers.ControllerSpecSupport
import models.{AnythingElseData, NormalMode, UserAnswers}
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.when
import pages.AnythingElsePage
import play.api.test.Helpers.*
import views.html.AnythingElseView

import scala.concurrent.Future

class AnythingElseControllerSpec extends ControllerSpecSupport {
  lazy val view: AnythingElseView = inject[AnythingElseView]
  lazy val formProvider: AnythingElseFormProvider = AnythingElseFormProvider()
  private def controller(userAnswers: Option[UserAnswers]): AnythingElseController = new AnythingElseController(
    sessionRepository = mockSessionRepository,
    navigator = navigator,
    identify = fakeAuth,
    getData = fakeData(userAnswers),
    requireData = fakeRequireData(userAnswers),
    formProvider = formProvider,
    controllerComponents = mcc,
    view = view
  )

  "AnythingElseController" should {
    "onPageLoad" must {
      "return 200" in {
        val result = controller(Some(emptyUserAnswers)).onPageLoad(NormalMode)(authenticatedFakeRequest)
        status(result) mustBe OK
        contentType(result) mustBe Some("text/html")
        charset(result) mustBe Some("utf-8")
      }
      
      "redirect to journey recovery for a GET if no existing data is found" in {
        val result = controller(None).onPageLoad(NormalMode)(authenticatedFakeRequest)
        status(result) mustBe 303
        redirectLocation(result).value mustBe controllers.routes.JourneyRecoveryController.onPageLoad().url
      }
    }
    
    "onSubmit" must {
      "redirect with valid form" in {
        when(mockSessionRepository.set(any())).thenReturn(Future.successful(true))
        val formRequest = requestWithForm(Map("value" -> "false"))
        val result = controller(Some(emptyUserAnswers)).onSubmit(NormalMode)(formRequest)
        status(result) mustBe 303
      }
      "bad request if no text and yes selected" in {
        val formRequest = requestWithForm(Map("value" -> "true"))
        val result = controller(Some(emptyUserAnswers)).onSubmit(NormalMode)(formRequest)
        status(result) mustBe 400
      }
      "bad request with invalid form" in {
        val result = controller(Some(emptyUserAnswers)).onSubmit(NormalMode)(authenticatedFakeRequest)
        status(result) mustBe 400
      }
      
      "redirect to journey recovery for a POST if no existing data is found" in {
        val formRequest = requestWithForm(Map("value" -> "false"))
        val result = controller(None).onSubmit(NormalMode)(formRequest)
        status(result) mustBe 303
        redirectLocation(result).value mustBe controllers.routes.JourneyRecoveryController.onPageLoad().url
      }
    }
  }

}