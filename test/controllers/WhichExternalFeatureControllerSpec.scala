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

import forms.WhichExternalFeatureFormProvider
import helpers.ControllerSpecSupport
import models.{ExternalFeature, NormalMode, UserAnswers}
import views.html.WhichExternalFeatureView
import play.api.test.Helpers.*

class WhichExternalFeatureControllerSpec extends ControllerSpecSupport {
  lazy val view: WhichExternalFeatureView = inject[WhichExternalFeatureView]
  lazy val formProvider: WhichExternalFeatureFormProvider = WhichExternalFeatureFormProvider()
  private def controller(userAnswers: Option[UserAnswers]): WhichExternalFeatureController = new WhichExternalFeatureController(
    identify = fakeAuth, getData = fakeData(userAnswers), requireData =  fakeRequireData(userAnswers), formProvider = formProvider, controllerComponents = mcc, view = view
  )


  "WhichExternalFeatureController" should {
    "onPageLoad" must {
      "return 200" in {
        val result = controller(Some(emptyUserAnswers)).onPageLoad(NormalMode, assessmentId)(authenticatedFakeRequest)
        status(result) mustBe 200
        contentType(result) mustBe Some("text/html")
        charset(result) mustBe Some("utf-8")
      }

      "redirect to journey recovery for a GET if no existing data is found" in {
        val result = controller(None).onPageLoad(NormalMode, assessmentId)(authenticatedFakeRequest)
        status(result) mustBe 303
        redirectLocation(result).value mustBe controllers.routes.JourneyRecoveryController.onPageLoad(assessmentId).url
      }
    }

    "onSubmit" must {
      "redirect" in {
        ExternalFeature.values.map { feature =>
          val formRequest = requestWithForm(Map("value" -> feature.toString))
          val result = controller(Some(emptyUserAnswers)).onSubmit(NormalMode, assessmentId)(formRequest)
          status(result) mustBe 303
        }
      }

      "get other value" in {
        val formRequest = requestWithForm(Map("value" -> "other", "otherSelect" -> "shippingContainers"))
        val result = controller(Some(emptyUserAnswers)).onSubmit(NormalMode, assessmentId)(formRequest)
        status(result) mustBe 303
      }

      "BadRequest when no form data" in {
        val result = controller(Some(emptyUserAnswers)).onSubmit(NormalMode, assessmentId)(authenticatedFakeRequest)
        status(result) mustBe 400
      }

      "redirect to journey recovery for a POST if no existing data is found" in {
        val formRequest = requestWithForm(Map("value" -> ExternalFeature.values.head.toString))
        val result = controller(None).onSubmit(NormalMode, assessmentId)(formRequest)
        status(result) mustBe 303
        redirectLocation(result).value mustBe controllers.routes.JourneyRecoveryController.onPageLoad(assessmentId).url
      }
    }
  }

}
