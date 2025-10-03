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
import org.scalatest.{Assertion, TryValues}
import pages.{HaveYouChangedExternalPage, HaveYouChangedInternalPage, HaveYouChangedSpacePage}
import play.api.test.Helpers.{contentType, *}
import views.html.HaveYouChangedView

import scala.concurrent.Future

class HaveYouChangedControllerSpec extends ControllerSpecSupport with TryValues {
  lazy val view: HaveYouChangedView = inject[HaveYouChangedView]

  def controllerWithUserAnswers(userAnswers: Option[UserAnswers]) = HaveYouChangedController(
    mockSessionRepository,
    navigator,
    fakeAuth,
    fakeData(userAnswers),
    fakeRequireData(userAnswers),
    HaveYouChangedFormProvider(),
    mcc,
    view
  )

  lazy val userAnswersFilled: UserAnswers = UserAnswers("id")
    .set(HaveYouChangedSpacePage, true).success.value
    .set(HaveYouChangedInternalPage, true).success.value
    .set(HaveYouChangedExternalPage, true).success.value

  "HaveYouChangedController" must {
    "Space type" should {
      Seq(NormalMode, CheckMode).foreach { mode =>
        s"return 200 and mode $mode" in {
          val result = controllerWithUserAnswers(Some(emptyUserAnswers)).loadSpace(mode)(authenticatedFakeRequest)
          status(result) mustBe OK
          contentType(result) mustBe Some("text/html")
          charset(result) mustBe Some("utf-8")
        }

        s"pre-filled form and mode $mode" in {
          val result = controllerWithUserAnswers(Some(userAnswersFilled)).loadSpace(mode)(authenticatedFakeRequest)
          contentAsString(result) must include("checked")
        }

        s"should redirect on successful submission and mode $mode" in {
          when(mockSessionRepository.set(any())).thenReturn(Future.successful(true))
          val formRequest = requestWithForm(Map("value" -> "true"))
          val result = controllerWithUserAnswers(Some(emptyUserAnswers)).submitSpace(mode)(formRequest)
          status(result) mustBe 303
        }

        s"should error if no selection and mode $mode" in {
          when(mockSessionRepository.set(any())).thenReturn(Future.successful(true))
          val result = controllerWithUserAnswers(Some(emptyUserAnswers)).submitSpace(mode)(authenticatedFakeRequest)
          status(result) mustBe BAD_REQUEST
        }

        s"redirect to Journey Recovery for a GET if no existing data is found and mode $mode" in {

          val result = controllerWithUserAnswers(None).loadSpace(mode)(authenticatedFakeRequest)
          status(result) mustBe SEE_OTHER
          redirectLocation(result).value mustBe routes.JourneyRecoveryController.onPageLoad().url
        }

        s"redirect to Journey Recovery for a POST if no existing data is found and mode $mode" in {
          val formRequest = requestWithForm(Map("value" -> "true"))
          val result = controllerWithUserAnswers(None).submitSpace(mode)(formRequest)
          status(result) mustBe SEE_OTHER
          redirectLocation(result).value mustBe routes.JourneyRecoveryController.onPageLoad().url
        }
      }
    }
    "Internal type" should {
      Seq(NormalMode, CheckMode).foreach { mode =>
        s"return 200 and mode $mode" in {
          val result = controllerWithUserAnswers(Some(emptyUserAnswers)).loadInternal(mode)(authenticatedFakeRequest)
          status(result) mustBe OK
          contentType(result) mustBe Some("text/html")
          charset(result) mustBe Some("utf-8")
        }

        s"pre-filled form and mode $mode" in {
          val result = controllerWithUserAnswers(Some(userAnswersFilled)).loadInternal(mode)(authenticatedFakeRequest)
          contentAsString(result) must include("checked")
        }

        s"redirect to Journey Recovery for a GET if no existing data is found and mode $mode" in {

          val result = controllerWithUserAnswers(None).loadInternal(mode)(authenticatedFakeRequest)
          status(result) mustBe SEE_OTHER
          redirectLocation(result).value mustBe routes.JourneyRecoveryController.onPageLoad().url
        }

        s"should redirect on successful submission and mode $mode" in {
          when(mockSessionRepository.set(any())).thenReturn(Future.successful(true))
          val formRequest = requestWithForm(Map("value" -> "true"))
          val result = controllerWithUserAnswers(Some(emptyUserAnswers)).submitInternal(mode)(formRequest)
          status(result) mustBe 303
        }

        s"should error if no selection and mode $mode" in {
          when(mockSessionRepository.set(any())).thenReturn(Future.successful(true))
          val result = controllerWithUserAnswers(Some(emptyUserAnswers)).submitInternal(mode)(authenticatedFakeRequest)
          status(result) mustBe BAD_REQUEST
        }

        s"redirect to Journey Recovery for a POST if no existing data is found and mode $mode" in {
          val formRequest = requestWithForm(Map("value" -> "true"))
          val result = controllerWithUserAnswers(None).submitInternal(mode)(formRequest)
          status(result) mustBe SEE_OTHER
          redirectLocation(result).value mustBe routes.JourneyRecoveryController.onPageLoad().url
        }
      }
    }

    "External type" should {
      Seq(NormalMode, CheckMode).foreach { mode =>
        s"return 200 and mode $mode" in {
          val result = controllerWithUserAnswers(Some(emptyUserAnswers)).loadExternal(mode)(authenticatedFakeRequest)
          status(result) mustBe OK
          contentType(result) mustBe Some("text/html")
          charset(result) mustBe Some("utf-8")
        }

        s"pre-filled form and mode $mode" in {
          val result = controllerWithUserAnswers(Some(userAnswersFilled)).loadExternal(mode)(authenticatedFakeRequest)
          contentAsString(result) must include("checked")
        }

        s"should redirect on successful submission and mode $mode" in {
          when(mockSessionRepository.set(any())).thenReturn(Future.successful(true))
          val formRequest = requestWithForm(Map("value" -> "true"))
          val result = controllerWithUserAnswers(Some(emptyUserAnswers)).submitExternal(mode)(formRequest)
          status(result) mustBe 303
        }

        s"redirect to Journey Recovery for a GET if no existing data is found and mode $mode" in {

          val result = controllerWithUserAnswers(None).loadExternal(mode)(authenticatedFakeRequest)
          status(result) mustBe SEE_OTHER
          redirectLocation(result).value mustBe routes.JourneyRecoveryController.onPageLoad().url
        }

        s"should error if no selection and mode $mode" in {
          when(mockSessionRepository.set(any())).thenReturn(Future.successful(true))
          val result = controllerWithUserAnswers(Some(emptyUserAnswers)).submitExternal(mode)(authenticatedFakeRequest)
          status(result) mustBe BAD_REQUEST
        }

        s"redirect to Journey Recovery for a POST if no existing data is found and mode $mode" in {
          val formRequest = requestWithForm(Map("value" -> "true"))
          val result = controllerWithUserAnswers(None).submitExternal(mode)(formRequest)
          status(result) mustBe SEE_OTHER
          redirectLocation(result).value mustBe routes.JourneyRecoveryController.onPageLoad().url
        }
      }
    }
  }


}
