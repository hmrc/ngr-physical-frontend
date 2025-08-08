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

import forms.HaveYouChangedFormProvider
import helpers.ControllerSpecSupport
import models.{CheckMode, External, HaveYouChangedControllerUse, Internal, Mode, NormalMode, Space, UserAnswers}
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.when
import org.scalatest.Assertion
import pages.HaveYouChangedSpacePage
import play.api.test.Helpers.*
import views.html.HaveYouChangedView

import scala.concurrent.Future

class HaveYouChangedControllerSpec extends ControllerSpecSupport {
  lazy val view: HaveYouChangedView = inject[HaveYouChangedView]
  lazy val controller: HaveYouChangedController =
    HaveYouChangedController(
      mockSessionRepository,
      navigator,
      fakeAuth,
      fakeData(None),
      HaveYouChangedFormProvider(),
      mcc,
      view
    )

  lazy val userAnswersFilled: Option[UserAnswers] = UserAnswers("id").set(HaveYouChangedSpacePage, true).toOption

  "HaveYouChangedController" must {
    "return 200 for space" in {
      checkForOkPageLoad(Space, NormalMode)
      checkForOkPageLoad(Space, CheckMode)
      checkForOkPageLoad(Internal, NormalMode)
      checkForOkPageLoad(Internal, CheckMode)
      checkForOkPageLoad(External, NormalMode)
      checkForOkPageLoad(External, CheckMode)
    }

    "return HTML" in {
      val result = controller.onPageLoad(Space, NormalMode)(authenticatedFakeRequest)
      contentType(result) mustBe Some("text/html")
      charset(result) mustBe Some("utf-8")
    }

    "pre-filled form" in {
      val filledController = HaveYouChangedController(
        mockSessionRepository,
        navigator,
        fakeAuth,
        fakeData(userAnswersFilled),
        HaveYouChangedFormProvider(),
        mcc,
        view
      )

      val result = filledController.onPageLoad(Space, NormalMode)(authenticatedFakeRequest)
      contentAsString(result) must include("checked")
    }

    "should redirect on successful submission" in {
      when(mockSessionRepository.set(any())).thenReturn(Future.successful(true))
      val formRequest = requestWithForm(Map("value" -> "true"))
      val result = controller.onSubmit(Space, NormalMode)(formRequest)
      status(result) mustBe 303
    }

    "should error if no selection" in {
      when(mockSessionRepository.set(any())).thenReturn(Future.successful(true))
      val result = controller.onSubmit(Space, NormalMode)(authenticatedFakeRequest)
      status(result) mustBe BAD_REQUEST
    }

  }

  def checkForOkPageLoad(use: HaveYouChangedControllerUse, mode: Mode): Assertion =
    val result = controller.onPageLoad(use, mode)(authenticatedFakeRequest)
    status(result) mustBe OK

}
