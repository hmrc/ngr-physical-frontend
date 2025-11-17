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
import forms.HowMuchOfPropertyFormProvider
import helpers.ControllerSpecSupport
import models.InternalFeature.{AirConditioning, CompressedAir, Escalators, GoodsLift, Heating, PassengerLift, Sprinklers}
import models.requests.IdentifierRequest
import models.{HowMuchOfProperty, InternalFeatureGroup1, Mode, NormalMode, UserAnswers}
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.when
import org.scalatest.TryValues.convertTryToSuccessOrFailure
import play.api.mvc.{AnyContentAsFormUrlEncoded, Result}
import play.api.test.Helpers.*
import views.html.HowMuchOfPropertyView

import scala.concurrent.Future

class HowMuchOfPropertyControllerSpec extends ControllerSpecSupport {
  lazy val view: HowMuchOfPropertyView = inject[HowMuchOfPropertyView]
  lazy val formProvider: HowMuchOfPropertyFormProvider = HowMuchOfPropertyFormProvider()
  private def controller(userAnswers: Option[UserAnswers]): HowMuchOfPropertyController = new HowMuchOfPropertyController(
    sessionRepository = mockSessionRepository, navigator = navigator, identify = fakeAuth, getData = fakeData(None), requireData = fakeRequireData(userAnswers), formProvider = formProvider, controllerComponents = mcc, view = view
  )

  "HowMuchOfPropertyController" should {
    InternalFeatureGroup1.values.foreach { feature =>
      "onPageLoad" must {
        s"return 200: ${feature.toString}" in {
          val result = onPageLoad(feature, Some(emptyUserAnswers), NormalMode)
          status(result) mustBe 200
          contentType(result) mustBe Some("text/html")
          charset(result) mustBe Some("utf-8")
        }

        s"return 200 with prepopulated data: ${feature.toString}" in {
          val userAnswers = emptyUserAnswers.set(HowMuchOfProperty.page(feature), HowMuchOfProperty.values.head).success.value
          val result = onPageLoad(feature, Some(userAnswers), NormalMode)
          status(result) mustBe 200
        }
      }
      "onSubmit" must {
        s"redirect with valid form submission: ${feature.toString}" in {
          when(mockSessionRepository.set(any())).thenReturn(Future.successful(true))
          val formRequest = requestWithForm(Map("value" -> "some"))
          val result = onSubmit(feature, Some(emptyUserAnswers), NormalMode)(formRequest)
          status(result) mustBe 303
        }

        s"Bad request with invalid form: ${feature.toString}" in {
          when(mockSessionRepository.set(any())).thenReturn(Future.successful(true))
          val formRequest: IdentifierRequest[AnyContentAsFormUrlEncoded] = requestWithForm(Map("value" -> "wow"))
          val result = onSubmit(feature, Some(emptyUserAnswers), NormalMode)(formRequest)
          status(result) mustBe 400
        }

        s"redirect to journey recovery Expired for a GET if no existing data is found for $feature" in {
          val result = onPageLoad(feature, None, NormalMode)
          status(result) mustBe 303
          redirectLocation(result).value mustBe controllers.routes.JourneyRecoveryController.onPageLoad(assessmentId).url
        }

        s"redirect to journey recovery for a POST if no existing data is found for $feature" in {
          val formRequest = requestWithForm(Map("value" -> "some"))
          val result = onSubmit(feature, None, NormalMode)(formRequest)
          status(result) mustBe 303
          redirectLocation(result).value mustBe controllers.routes.JourneyRecoveryController.onPageLoad(assessmentId).url
        }
      }
    }
  }

  private def onPageLoad(feature: InternalFeatureGroup1, userAnswers: Option[UserAnswers], mode: Mode): Future[Result] = {
    feature match {
      case AirConditioning => controller(userAnswers).onPageLoadAirCon(mode, assessmentId)(authenticatedFakeRequest)
      case Escalators => controller(userAnswers).onPageLoadEscalator(mode, assessmentId)(authenticatedFakeRequest)
      case GoodsLift => controller(userAnswers).onPageLoadGoodsLift(mode, assessmentId)(authenticatedFakeRequest)
      case PassengerLift => controller(userAnswers).onPageLoadPassengerLift(mode, assessmentId)(authenticatedFakeRequest)
      case CompressedAir => controller(userAnswers).onPageLoadCompressedAir(mode, assessmentId)(authenticatedFakeRequest)
      case Heating => controller(userAnswers).onPageLoadHeating(mode, assessmentId)(authenticatedFakeRequest)
      case Sprinklers => controller(userAnswers).onPageLoadSprinklers(mode, assessmentId)(authenticatedFakeRequest)
    }
  }

  private def onSubmit(feature: InternalFeatureGroup1, userAnswers: Option[UserAnswers], mode: Mode)(formRequest: IdentifierRequest[AnyContentAsFormUrlEncoded]): Future[Result] = {
    feature match {
      case AirConditioning => controller(userAnswers).onSubmitAirCon(mode, assessmentId)(formRequest)
      case Escalators => controller(userAnswers).onSubmitEscalator(mode, assessmentId)(formRequest)
      case GoodsLift => controller(userAnswers).onSubmitGoodsLift(mode, assessmentId)(formRequest)
      case PassengerLift => controller(userAnswers).onSubmitPassengerLift(mode, assessmentId)(formRequest)
      case CompressedAir => controller(userAnswers).onSubmitCompressedAir(mode, assessmentId)(formRequest)
      case Heating => controller(userAnswers).onSubmitHeating(mode, assessmentId)(formRequest)
      case Sprinklers => controller(userAnswers).onSubmitSprinklers(mode, assessmentId)(formRequest)
    }
  }
}