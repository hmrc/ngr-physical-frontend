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

import forms.WhenCompleteChangeFormProvider
import helpers.ControllerSpecSupport
import models.{CheckMode, Mode, NormalMode, UserAnswers}
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.when
import org.scalatest.Assertion
import pages.WhenCompleteChangePage
import play.api.test.FakeRequest
import play.api.test.Helpers.*
import views.html.WhenCompleteChangeView

import java.time.LocalDate
import scala.concurrent.Future

class WhenCompleteChangeSpec extends ControllerSpecSupport {
  lazy val view: WhenCompleteChangeView = inject[WhenCompleteChangeView]
  lazy val controller: WhenCompleteChangeController =
    WhenCompleteChangeController(
      mockSessionRepository,
      navigator,
      fakeAuth,
      fakeData(None),
      WhenCompleteChangeFormProvider(),
      mcc,
      view
    )

  lazy val userAnswersFilled: Option[UserAnswers] = UserAnswers("id").set(WhenCompleteChangePage, LocalDate.of(2025, 8, 12)).toOption

  "HaveYouChangedController" must {
    "return 200 for space" in {
      Seq(NormalMode, CheckMode).foreach(checkForOkPageLoad)
    }
    
    "pre-filled form" in {
      val filledController = WhenCompleteChangeController(
        mockSessionRepository,
        navigator,
        fakeAuth,
        fakeData(userAnswersFilled),
        WhenCompleteChangeFormProvider(),
        mcc,
        view
      )

      val result = filledController.onPageLoad(NormalMode)(authenticatedFakeRequest)
      contentAsString(result) must include("12")
      contentAsString(result) must include("8")
      contentAsString(result) must include("2025")
    }

    "should redirect on successful submission" in {
      when(mockSessionRepository.set(any())).thenReturn(Future.successful(true))
      val formRequest = FakeRequest()
        .withFormUrlEncodedBody(
          "value.day" -> "31", 
          "value.month" -> "12", 
          "value.year" -> "2025"
        )
      val result = controller.onSubmit(NormalMode)(formRequest)
      status(result) mustBe 303
    }

    "should error if before 1900" in {
      when(mockSessionRepository.set(any())).thenReturn(Future.successful(true))
      val formRequest = FakeRequest()
        .withFormUrlEncodedBody(
          "value.day" -> "31",
          "value.month" -> "12",
          "value.year" -> "1888"
        )
      val result = controller.onSubmit(NormalMode)(formRequest)
      status(result) mustBe 400
    }

    "should error if no selection" in {
      when(mockSessionRepository.set(any())).thenReturn(Future.successful(true))
      val result = controller.onSubmit(NormalMode)(authenticatedFakeRequest)
      status(result) mustBe BAD_REQUEST
    }

  }

  def checkForOkPageLoad(mode: Mode): Assertion =
    val result = controller.onPageLoad(mode)(authenticatedFakeRequest)
    status(result) mustBe OK
    contentType(result) mustBe Some("text/html")
    charset(result) mustBe Some("utf-8")

}
