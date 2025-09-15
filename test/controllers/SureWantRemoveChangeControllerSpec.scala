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

import forms.SureWantRemoveChangeFormProvider
import helpers.ControllerSpecSupport
import models.ExternalFeature.SolarPanels
import models.InternalFeature.{AirConditioning, PassengerLift, SecurityCamera}
import models.{CheckMode, NormalMode}
import play.api.test.Helpers.*
import uk.gov.hmrc.http.NotFoundException
import views.html.SureWantRemoveChangeView


class SureWantRemoveChangeControllerSpec extends ControllerSpecSupport {
  lazy val view: SureWantRemoveChangeView = inject[SureWantRemoveChangeView]
  lazy val formProvider: SureWantRemoveChangeFormProvider = SureWantRemoveChangeFormProvider()
  private val controller: SureWantRemoveChangeController = new SureWantRemoveChangeController(
    identify = fakeAuth, getData = fakeData(None), formProvider = formProvider, controllerComponents = mcc, view = view
  )

  "SureWantRemoveChangeController" should {
    "onPageLoad" must {
      "return 200" in {
        val result = controller.onPageLoad(PassengerLift.toString, NormalMode)(authenticatedFakeRequest)
        status(result) mustBe OK
      }
      "return HTML" in {
        val result = controller.onPageLoad(PassengerLift.toString, NormalMode)(authenticatedFakeRequest)
        contentType(result) mustBe Some("text/html")
        charset(result) mustBe Some("utf-8")
      }
      "throw an exception for an unknown feature" in {
        val ex = intercept[NotFoundException] {
          await(controller.onPageLoad ("unknown feature", NormalMode) (authenticatedFakeRequest) )
        }
        ex.getMessage must include("unable to determine CYAViewType")
      }
    }

    "onSubmit" must {
      "redirect to remove an internal feature when the mode if NormalMode" in {
        val internalFeature = AirConditioning
        val result = controller.onSubmit(internalFeature.toString, NormalMode)(requestWithForm(Map("value" -> "true")))
        redirectLocation(result) mustBe Some(routes.SmallCheckYourAnswersController.removeInternal(internalFeature.toString, NormalMode, false).url)
      }

      "redirect to remove an external feature" in {
        val externalFeature = SolarPanels
        val result = controller.onSubmit(externalFeature.toString, NormalMode)(requestWithForm(Map("value" -> "true")))
        redirectLocation(result) mustBe Some(routes.SmallCheckYourAnswersController.removeExternal(externalFeature.toString, NormalMode, false).url)
      }
      "bad request when no selection" in {
        val result = controller.onSubmit(SecurityCamera.toString, NormalMode)(authenticatedFakeRequest)
        status(result) mustBe BAD_REQUEST
      }
      "bad request with an invalid form" in {
        val result = controller.onSubmit(SolarPanels.toString, NormalMode)(requestWithForm(Map("value" -> "invalid")))
        status(result) mustBe BAD_REQUEST
      }
    }
  }

}
