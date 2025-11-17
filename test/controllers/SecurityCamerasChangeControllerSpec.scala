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

import forms.SecurityCamerasChangeFormProvider
import helpers.ControllerSpecSupport
import models.{CheckMode, NormalMode, UserAnswers}
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.when
import org.scalatest.TryValues.convertTryToSuccessOrFailure
import pages.SecurityCamerasChangePage
import views.html.SecurityCamerasChangeView
import play.api.test.Helpers.*

import scala.concurrent.Future

class SecurityCamerasChangeControllerSpec extends ControllerSpecSupport {
  lazy val view: SecurityCamerasChangeView = inject[SecurityCamerasChangeView]
  lazy val formProvider: SecurityCamerasChangeFormProvider = inject[SecurityCamerasChangeFormProvider]

  private def controller(userAnswers: Option[UserAnswers]): SecurityCamerasChangeController = new SecurityCamerasChangeController(
    sessionRepository = mockSessionRepository, navigator = navigator, identify = fakeAuth, getData = fakeData(userAnswers), requireData = fakeRequireData(userAnswers), formProvider = formProvider, controllerComponents = mcc, view = view
  )

  when(mockSessionRepository.set(any())).thenReturn(Future.successful(true))

  "SecurityCamerasChangeController" should {

    Seq(NormalMode, CheckMode).foreach { mode =>
      s"onPageLoad in $mode" must {
        "return 200" in {
          val result = controller(Some(emptyUserAnswers)).onPageLoad(mode, assessmentId)(authenticatedFakeRequest)
          status(result) mustBe 200
          contentType(result) mustBe Some("text/html")
          charset(result) mustBe Some("utf-8")
        }

        "return 200 with pre-populated data" in {
          val userAnswers = emptyUserAnswers.set(SecurityCamerasChangePage, 10).success.value
          val result = controller(Some(userAnswers)).onPageLoad(mode, assessmentId)(authenticatedFakeRequest)
          status(result) mustBe 200
        }
        
        "redirect to journey recovery Expired for a GET if no existing data is found" in {
          val result = controller(None).onPageLoad(mode, assessmentId)(authenticatedFakeRequest)
          status(result) mustBe 303
          redirectLocation(result).value mustBe controllers.routes.JourneyRecoveryController.onPageLoad(assessmentId).url
        }
      }

      s"onSubmit in $mode" must {
        "redirect when valid entry" in {
          val formRequest = requestWithForm(Map("value" -> "10"))
          val result = controller(Some(emptyUserAnswers)).onSubmit(mode, assessmentId)(formRequest)
          status(result) mustBe 303
        }
        "invalid form data" in {
          val formRequest = requestWithForm(Map("value" -> "hello"))
          val result = controller(Some(emptyUserAnswers)).onSubmit(mode, assessmentId)(formRequest)
          status(result) mustBe 400
        }
        "BadRequest when no form data" in {
          val result = controller(Some(emptyUserAnswers)).onSubmit(mode, assessmentId)(authenticatedFakeRequest)
          status(result) mustBe 400
        }

        "redirect to journey recovery for a POST if no existing data is found" in {
          val formRequest = requestWithForm(Map("value" -> "10"))
          val result = controller(None).onSubmit(mode, assessmentId)(formRequest)
          status(result) mustBe 303
          redirectLocation(result).value mustBe controllers.routes.JourneyRecoveryController.onPageLoad(assessmentId).url
        }
      }
    }
  }

}

