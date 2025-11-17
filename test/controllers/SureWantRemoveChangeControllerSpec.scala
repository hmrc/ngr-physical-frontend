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
import models.{CYAInternal, CheckMode, NormalMode, UserAnswers}
import play.api.test.Helpers.*
import uk.gov.hmrc.http.NotFoundException
import views.html.SureWantRemoveChangeView


class SureWantRemoveChangeControllerSpec extends ControllerSpecSupport {
  lazy val view: SureWantRemoveChangeView = inject[SureWantRemoveChangeView]
  lazy val formProvider: SureWantRemoveChangeFormProvider = SureWantRemoveChangeFormProvider()

  private def controller(userAnswers: Option[UserAnswers]): SureWantRemoveChangeController = new SureWantRemoveChangeController(
    identify = fakeAuth, getData = fakeData(userAnswers), requireData = fakeRequireData(userAnswers), formProvider = formProvider, controllerComponents = mcc, view = view
  )

  "SureWantRemoveChangeController" should {
    Seq(NormalMode, CheckMode).foreach { mode =>
      s"onPageLoad in $mode" must {
        "return 200" in {
          val result = controller(Some(emptyUserAnswers)).onPageLoad(PassengerLift.toString, mode, assessmentId = assessmentId)(authenticatedFakeRequest)
          status(result) mustBe OK
          contentType(result) mustBe Some("text/html")
          charset(result) mustBe Some("utf-8")
        }

        "throw an exception for an unknown feature" in {
          val ex = intercept[NotFoundException] {
            await(controller(Some(emptyUserAnswers)).onPageLoad("unknown feature", mode, assessmentId = assessmentId)(authenticatedFakeRequest))
          }
          ex.getMessage must include("unable to determine CYAViewType")
        }

        "redirect to journey recovery Expired for a GET if no existing data is found" in {
          val result = controller(None).onPageLoad(PassengerLift.toString, mode, assessmentId = assessmentId)(authenticatedFakeRequest)
          status(result) mustBe 303
          redirectLocation(result).value mustBe controllers.routes.JourneyRecoveryController.onPageLoad(assessmentId).url
        }
      }

      s"onSubmit in $mode" must {
        "redirect to remove an internal feature when the mode is $mode" in {
          val internalFeature = AirConditioning
          val result = controller(Some(emptyUserAnswers)).onSubmit(internalFeature.toString, mode, assessmentId = assessmentId)(requestWithForm(Map("value" -> "true")))
          redirectLocation(result) mustBe Some(routes.SmallCheckYourAnswersController.removeInternal(internalFeature.toString, mode, false, assessmentId).url)
        }

        "redirect to check your answers page when the flag selected is 'false' and the mode is CheckMode for the internalFeature" in {
          val internalFeature = AirConditioning
          val result = controller(Some(emptyUserAnswers)).onSubmit(internalFeature.toString, NormalMode, assessmentId = assessmentId)(requestWithForm(Map("value" -> "false")))
          redirectLocation(result) mustBe Some(routes.CheckYourAnswersController.onPageLoad(assessmentId).url)
        }

        "redirect to check your answers page when the flag selected is 'false' and the mode is CheckMode for the internalFeature and fromMiniCYA is `true`" in {
          val internalFeature = AirConditioning
          val result = controller(Some(emptyUserAnswers)).onSubmit(internalFeature.toString, mode, true, assessmentId = assessmentId)(requestWithForm(Map("value" -> "false")))
          redirectLocation(result) mustBe Some(routes.SmallCheckYourAnswersController.onPageLoad(CYAInternal, mode, assessmentId = assessmentId).url)
        }

        "redirect to remove an external feature" in {
          val externalFeature = SolarPanels
          val result = controller(Some(emptyUserAnswers)).onSubmit(externalFeature.toString, mode, assessmentId = assessmentId)(requestWithForm(Map("value" -> "true")))
          redirectLocation(result) mustBe Some(routes.SmallCheckYourAnswersController.removeExternal(externalFeature.toString, mode, false, assessmentId).url)
        }

        "redirect to check your answers page when the flag selected is 'false' and the mode is CheckMode for the externalFeature" in {
          val externalFeature = SolarPanels
          val result = controller(Some(emptyUserAnswers)).onSubmit(externalFeature.toString, CheckMode, assessmentId = assessmentId)(requestWithForm(Map("value" -> "false")))
          redirectLocation(result) mustBe Some(routes.CheckYourAnswersController.onPageLoad(assessmentId).url)
        }

        "bad request when no selection" in {
          val result = controller(Some(emptyUserAnswers)).onSubmit(SecurityCamera.toString, mode, assessmentId = assessmentId)(authenticatedFakeRequest)
          status(result) mustBe BAD_REQUEST
        }

        "bad request with an invalid form" in {
          val result = controller(Some(emptyUserAnswers)).onSubmit(SolarPanels.toString, mode, assessmentId = assessmentId)(requestWithForm(Map("value" -> "invalid")))
          status(result) mustBe BAD_REQUEST
        }

        "redirect to journey recovery Expired for a POST if no existing data is found" in {
          val result = controller(None).onSubmit(SolarPanels.toString, mode, assessmentId = assessmentId)(requestWithForm(Map("value" -> "true")))
          status(result) mustBe 303
          redirectLocation(result).value mustBe controllers.routes.JourneyRecoveryController.onPageLoad(assessmentId).url
        }
      }
    }
  }

}
