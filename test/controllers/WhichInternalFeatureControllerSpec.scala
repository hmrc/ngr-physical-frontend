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

import forms.WhichInternalFeatureFormProvider
import helpers.ControllerSpecSupport
import models.{InternalFeature, NormalMode, UserAnswers}
import views.html.WhichInternalFeatureView
import play.api.test.Helpers.*

class WhichInternalFeatureControllerSpec extends ControllerSpecSupport {
  lazy val view: WhichInternalFeatureView = inject[WhichInternalFeatureView]
  lazy val formProvider: WhichInternalFeatureFormProvider = WhichInternalFeatureFormProvider()
  private def controller(userAnswers: Option[UserAnswers]): WhichInternalFeatureController = new WhichInternalFeatureController(
    identify = fakeAuth, getData = fakeData(userAnswers), requireData=  fakeRequireData(userAnswers), formProvider = formProvider, controllerComponents = mcc, view = view
  )

  "WhichInternalFeatureController" should {
    "onPageLoad" must {
      "return 200" in {
        val result = controller(Some(emptyUserAnswers)).onPageLoad(NormalMode)(authenticatedFakeRequest)
        status(result) mustBe 200
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
      "redirect" in {
        InternalFeature.values.map { feature =>
          val formRequest = requestWithForm(Map("value" -> feature.toString))
          val result = controller(Some(emptyUserAnswers)).onSubmit(NormalMode)(formRequest)
          status(result) mustBe 303
        }
      }

      "get other value" in {
        val formRequest = requestWithForm(Map("value" -> "other", "otherSelect" -> "sprinklers"))
        val result = controller(Some(emptyUserAnswers)).onSubmit(NormalMode)(formRequest)
        status(result) mustBe 303
      }

      "BadRequest when no form data" in {
        val result = controller(Some(emptyUserAnswers)).onSubmit(NormalMode)(authenticatedFakeRequest)
        status(result) mustBe 400
      }

      "redirect to journey recovery for a POST if no existing data is found" in {
        val formRequest = requestWithForm(Map("value" -> InternalFeature.AirConditioning.toString))
        val result = controller(None).onSubmit(NormalMode)(formRequest)
        status(result) mustBe 303
        redirectLocation(result).value mustBe controllers.routes.JourneyRecoveryController.onPageLoad().url
      }
    }

  }
}
