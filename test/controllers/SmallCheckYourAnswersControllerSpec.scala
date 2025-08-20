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

import forms.SmallCheckYourAnswersFormProvider
import helpers.ControllerSpecSupport
import models.{CYAInternal, NormalMode}
import org.apache.pekko.http.scaladsl.model.HttpHeader.ParsingResult.Ok
import play.api.test.Helpers.*
import views.html.SmallCheckYourAnswersView

class SmallCheckYourAnswersControllerSpec extends ControllerSpecSupport {
  lazy val view: SmallCheckYourAnswersView = inject[SmallCheckYourAnswersView]
  lazy val formProvider: SmallCheckYourAnswersFormProvider = SmallCheckYourAnswersFormProvider()
  private val controller: SmallCheckYourAnswersController = new SmallCheckYourAnswersController(
    identify = fakeAuth, getData = fakeData(None), sessionRepository = mockSessionRepository, formProvider = formProvider, controllerComponents = mcc, view = view
  )

  "SmallCheckYourAnswersController" should {
    "onPageLoad" must {
      "return 200" in {
        val result = controller.onPageLoad(CYAInternal)(authenticatedFakeRequest)
        status(result) mustBe OK
      }

      "return HTML" in {
        val result = controller.onPageLoad(CYAInternal)(authenticatedFakeRequest)
        contentType(result) mustBe Some("text/html")
        charset(result) mustBe Some("utf-8")
      }
    }

    "onSubmit" must {
      "redirect to have you changed Internal" in {
        val formRequest = requestWithForm(Map("value" -> "true"))
        val result = controller.onSubmit(CYAInternal)(formRequest)
        redirectLocation(result) mustBe Some(routes.HaveYouChangedController.loadInternal(NormalMode).url)
      }
      "redirect to have you changed External" in {
        val formRequest = requestWithForm(Map("value" -> "false"))
        val result = controller.onSubmit(CYAInternal)(formRequest)
        redirectLocation(result) mustBe Some(routes.HaveYouChangedController.loadExternal(NormalMode).url)
      }
      "bad request when no selection" in {
        val result = controller.onSubmit(CYAInternal)(authenticatedFakeRequest)
        status(result) mustBe 400
      }
    }
  }

}
