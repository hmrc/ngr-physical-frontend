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
import config.FrontendAppConfig
import helpers.{ControllerSpecSupport, TestData}
import models.NavBarPageContents.createDefaultNavBar
import models.registration.CredId
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.when
import pages.DeclarationPage
import play.api.mvc.Result
import play.api.test.FakeRequest
import play.api.test.Helpers.*
import views.html.DeclarationView

import scala.concurrent.Future

class DeclarationControllerSpec extends SpecBase with TestData {

  "Declaration Controller" - {
    "Return OK and the correct view" in {
      val application = applicationBuilder(userAnswers = Some(emptyUserAnswers)).build()

      implicit val appConfig: FrontendAppConfig = application.injector.instanceOf[FrontendAppConfig]
      running(application) {
        val request = FakeRequest(GET, routes.DeclarationController.show.url)
        val result = route(application, request).value
        val view = application.injector.instanceOf[DeclarationView]

        status(result) mustEqual OK
        contentAsString(result) mustEqual view(property.addressFull, createDefaultNavBar())(request, messages(application)).toString
      }
    }

    "redirect when accepted" in {
      when(mockSessionRepository.set(any())).thenReturn(Future.successful(true))

      val application = applicationBuilder(userAnswers = Some(emptyUserAnswers)).build()

      running(application) {
        val request = FakeRequest(POST, routes.DeclarationController.next.url)
        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual routes.SubmissionConfirmationController.onPageLoad().url
      }
    }
  }
}
