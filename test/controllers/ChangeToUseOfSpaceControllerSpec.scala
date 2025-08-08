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

import forms.ChangeToUseOfSpaceFormProvider
import helpers.ControllerSpecSupport
import models.UseOfSpaces.Rearrangedtheuseofspace
import models.{ChangeToUseOfSpace, CheckMode, Mode, NormalMode, UseOfSpaces, UserAnswers}
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.when
import pages.ChangeToUseOfSpacePage
import play.api.test.FakeRequest
import play.api.test.Helpers.*
import views.html.ChangeToUseOfSpaceView
import org.scalatest.Assertion

import scala.concurrent.Future

class ChangeToUseOfSpaceControllerSpec extends ControllerSpecSupport{

  lazy val view: ChangeToUseOfSpaceView = inject[ChangeToUseOfSpaceView]
  lazy val controller: ChangeToUseOfSpaceController =
    ChangeToUseOfSpaceController(
      mockSessionRepository,
      navigator,
      fakeAuth,
      fakeData(None),
      ChangeToUseOfSpaceFormProvider(),
      mcc,
      view
    )

  lazy val prefillAnswers: ChangeToUseOfSpace =
    ChangeToUseOfSpace(
      selectUseOfSpace = Set(Rearrangedtheuseofspace),
      hasPlanningPermission = true,
      permissionReference = Some("AB1234FRXYXYX")
    )

  lazy val userAnswersFilled: Option[UserAnswers] = UserAnswers("id").set(ChangeToUseOfSpacePage, prefillAnswers).toOption

  "ChangeToUseOfSpace Controller" should {

    "return 200 for space" in {
      checkForOkPageLoad(NormalMode)
      checkForOkPageLoad(CheckMode)
    }

    "return HTML" in {
      val result = controller.onPageLoad(NormalMode)(authenticatedFakeRequest)
      contentType(result) mustBe Some("text/html")
      charset(result) mustBe Some("utf-8")
    }

    "pre-filled form" in {
      lazy val filledController: ChangeToUseOfSpaceController =
        ChangeToUseOfSpaceController(
          mockSessionRepository,
          navigator,
          fakeAuth,
          fakeData(userAnswersFilled),
          ChangeToUseOfSpaceFormProvider(),
          mcc,
          view
        )

      val result = filledController.onPageLoad(NormalMode)(authenticatedFakeRequest)
      contentAsString(result) must include("checked")
      contentAsString(result) must include("AB1234FRXYXYX")
    }

    "should redirect on successful submission" in {
      when(mockSessionRepository.set(any())).thenReturn(Future.successful(true))
      val formRequest = FakeRequest()
        .withFormUrlEncodedBody(
                  "selectUseOfSpace[0]" -> "rearrangedTheUseOfSpace",
                  "hasPlanningPermission" -> "false"
                )
      val result = controller.onSubmit(NormalMode)(formRequest)
      status(result) mustBe 303
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

}
