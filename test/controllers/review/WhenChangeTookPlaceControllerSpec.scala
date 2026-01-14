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

package controllers.review

import forms.review.WhenChangeTookPlaceFormProvider
import forms.review.WhenChangeTookPlaceFormProvider
import helpers.ControllerSpecSupport
import models.{NormalMode, UserAnswers}
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.when
import play.api.test.Helpers.*
import views.html.review.WhenChangeTookPlaceView
import views.html.review.WhenChangeTookPlaceView

import scala.concurrent.Future

class WhenChangeTookPlaceControllerSpec extends ControllerSpecSupport {
  lazy val view: WhenChangeTookPlaceView                 = inject[WhenChangeTookPlaceView]
  lazy val formProvider: WhenChangeTookPlaceFormProvider = WhenChangeTookPlaceFormProvider()

  private val controller: WhenChangeTookPlaceController = new WhenChangeTookPlaceController(
    sessionRepository = mockSessionRepository,
    identify = fakeAuth,
    getData = fakeData(Some(UserAnswers("id"))),
    formProvider = formProvider,
    controllerComponents = mcc,
    view = view
  )

  "AnythingElseController" should {
    "onPageLoad" must {
      "return 200" in {
        val result = controller.onPageLoad(assessmentId)(authenticatedFakeRequest)
        status(result) mustBe OK
      }
    }
    "onSubmit"   must {
      "redirect with valid form" in {
        when(mockSessionRepository.set(any())).thenReturn(Future.successful(true))
        val formRequest = requestWithForm(Map("value" -> "false"))
        val result      = controller.onSubmit(assessmentId)(formRequest)
        status(result) mustBe SEE_OTHER
      }
      "bad request if no date and yes selected" in {
        val formRequest = requestWithForm(Map("value" -> "true"))
        val result      = controller.onSubmit(assessmentId)(formRequest)
        status(result) mustBe BAD_REQUEST
      }
      "bad request with invalid form" in {
        val result = controller.onSubmit(assessmentId)(authenticatedFakeRequest)
        status(result) mustBe BAD_REQUEST
      }
    }
  }

}
