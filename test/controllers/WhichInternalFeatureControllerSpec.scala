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
import models.InternalFeature
import views.html.WhichInternalFeatureView
import play.api.test.Helpers.*

class WhichInternalFeatureControllerSpec extends ControllerSpecSupport {
  lazy val view: WhichInternalFeatureView = inject[WhichInternalFeatureView]
  lazy val formProvider: WhichInternalFeatureFormProvider = WhichInternalFeatureFormProvider()
  private val controller: WhichInternalFeatureController = new WhichInternalFeatureController(
    identify = fakeAuth, getData = fakeData(None), formProvider = formProvider, controllerComponents = mcc, view = view
  )

  "WhichInternalFeatureController" should {
    "onPageLoad" must {
      "return 200" in {
        val result = controller.onPageLoad(authenticatedFakeRequest)
        status(result) mustBe 200
      }

      "return HTML" in {
        val result = controller.onPageLoad(authenticatedFakeRequest)
        contentType(result) mustBe Some("text/html")
        charset(result) mustBe Some("utf-8")
      }
    }
    "onSubmit" must {
      "redirect" in {
        InternalFeature.values.map { feature =>
          val formRequest = requestWithForm(Map("value" -> feature.toString))
          val result = controller.onSubmit(formRequest)
          status(result) mustBe 303
        }
      }

      "get other value" in {
        val formRequest = requestWithForm(Map("value" -> "other", "otherSelect" -> "sprinklers"))
        val result = controller.onSubmit(formRequest)
        status(result) mustBe 303
      }

      "BadRequest when no form data" in {
        val result = controller.onSubmit(authenticatedFakeRequest)
        status(result) mustBe 400
      }
    }

  }
}
