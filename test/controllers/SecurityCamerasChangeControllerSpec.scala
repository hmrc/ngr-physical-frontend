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
import models.NormalMode
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.when
import views.html.SecurityCamerasChangeView
import play.api.test.Helpers.*
import scala.concurrent.Future

class SecurityCamerasChangeControllerSpec extends ControllerSpecSupport {
  lazy val view: SecurityCamerasChangeView = inject[SecurityCamerasChangeView]
  lazy val formProvider: SecurityCamerasChangeFormProvider = inject[SecurityCamerasChangeFormProvider]
  private val controller: SecurityCamerasChangeController = new SecurityCamerasChangeController(
    sessionRepository = mockSessionRepository, navigator = navigator, identify = fakeAuth, getData = fakeData(None), formProvider = formProvider, controllerComponents = mcc, view = view
  )

  when(mockSessionRepository.set(any())).thenReturn(Future.successful(true))
  
  "SecurityCamerasChangeController" should {

    "onPageLoad" must {
      "return 200" in {
        val result = controller.onPageLoad(NormalMode)(authenticatedFakeRequest)
        status(result) mustBe 200
      }
      "return HTML" in {
        val result = controller.onPageLoad(NormalMode)(authenticatedFakeRequest)
        contentType(result) mustBe Some("text/html")
        charset(result) mustBe Some("utf-8")
      }
    }

    "onSubmit" must {
      "redirect when valid entry" in {
        val formRequest = requestWithForm(Map("value" -> "10"))
        val result = controller.onSubmit(NormalMode)(formRequest)
        status(result) mustBe 303
      }
      "invalid form data" in {
        val formRequest = requestWithForm(Map("value" -> "hello"))
        val result = controller.onSubmit(NormalMode)(formRequest)
        status(result) mustBe 400
      }
      "BadRequest when no form data" in {
        val result = controller.onSubmit(NormalMode)(authenticatedFakeRequest)
        status(result) mustBe 400
      }
    }

  }

}

