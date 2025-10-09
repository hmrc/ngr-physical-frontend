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
import models.InternalFeature.{Escalators, SecurityCamera}
import models.{CYAExternal, CYAInternal, CYAViewType, CheckMode, ExternalFeature, HowMuchOfProperty, NormalMode, UserAnswers, WhatHappenedTo}
import org.apache.pekko.http.scaladsl.model.HttpHeader.ParsingResult.Ok
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.when
import org.scalatest.TryValues.convertTryToSuccessOrFailure
import pages.{HaveYouChangedExternalPage, HowMuchOfPropertyAirConPage, WhatHappenedToLoadingBaysPage}
import play.api.test.Helpers.*
import views.html.SmallCheckYourAnswersView

import scala.concurrent.Future

class SmallCheckYourAnswersControllerSpec extends ControllerSpecSupport {
  lazy val view: SmallCheckYourAnswersView = inject[SmallCheckYourAnswersView]
  lazy val formProvider: SmallCheckYourAnswersFormProvider = SmallCheckYourAnswersFormProvider()
  private def controller(userAnswers: Option[UserAnswers]): SmallCheckYourAnswersController = new SmallCheckYourAnswersController(
    identify = fakeAuth, getData = fakeData(userAnswers), requireData= fakeRequireData(userAnswers),sessionRepository = mockSessionRepository, formProvider = formProvider, controllerComponents = mcc, view = view
  )

  "SmallCheckYourAnswersController" should {
    Seq(CYAInternal, CYAExternal).foreach { case (viewType: CYAViewType) =>
      s"onPageLoad for $viewType" must {
        "return 200" in {
          val result = controller(Some(emptyUserAnswers)).onPageLoad(viewType, NormalMode)(authenticatedFakeRequest)
          status(result) mustBe OK
          contentType(result) mustBe Some("text/html")
          charset(result) mustBe Some("utf-8")
        }
        "redirect to journey recovery Expired for a GET if no existing data is found" in {
          val result = controller(None).onPageLoad(viewType, NormalMode)(authenticatedFakeRequest)
          status(result) mustBe 303
          redirectLocation(result).value mustBe controllers.routes.JourneyRecoveryController.onPageLoad().url
        }
      }

      s"onSubmit for $viewType" must {
        "redirect to check your answers page when the mode is CheckMode" in {
          val formRequest = requestWithForm(Map("value" -> "false"))
          val result = controller(Some(emptyUserAnswers)).onSubmit(viewType, CheckMode)(formRequest)
          redirectLocation(result) mustBe Some(routes.CheckYourAnswersController.onPageLoad().url)
        }
        "bad request when no selection" in {
          val result = controller(Some(emptyUserAnswers)).onSubmit(viewType, NormalMode)(authenticatedFakeRequest)
          status(result) mustBe 400
        }
        "redirect to journey recovery Expired for a POST if no existing data is found" in {
          val formRequest = requestWithForm(Map("value" -> "true"))
          val result = controller(None).onSubmit(viewType, NormalMode)(formRequest)
          status(result) mustBe 303
          redirectLocation(result).value mustBe controllers.routes.JourneyRecoveryController.onPageLoad().url
        }
      }
    }

    "onSubmit redirect to have you changed Internal when the viewType is CYAInternal" in {
      val formRequest = requestWithForm(Map("value" -> "true"))
      val result = controller(Some(emptyUserAnswers)).onSubmit(CYAInternal, NormalMode)(formRequest)
      redirectLocation(result) mustBe Some(routes.WhichInternalFeatureController.onPageLoad(NormalMode).url)
    }

    "onSubmit redirect to have you changed External when the viewType is CYAInternal" in {
      val formRequest = requestWithForm(Map("value" -> "false"))
      val result = controller(Some(emptyUserAnswers)).onSubmit(CYAInternal, NormalMode)(formRequest)
      redirectLocation(result) mustBe Some(routes.HaveYouChangedController.loadExternal(NormalMode).url)
    }

    "onSubmit redirect to have you changed External when the viewType is CYAExternal" in {
      val formRequest = requestWithForm(Map("value" -> "true"))
      val result = controller(Some(emptyUserAnswers)).onSubmit(CYAExternal, NormalMode)(formRequest)
      redirectLocation(result) mustBe Some(routes.WhichExternalFeatureController.onPageLoad(NormalMode).url)
    }

    "onSubmit redirect to not tole any changes page when the viewType is External and HaveYouChangedInternal and HaveYouChangedExternal and HaveYouChangedSpace option is 'No'" in {
      val formRequest = requestWithForm(Map("value" -> "false"))
      val result = controller(Some(emptyUserAnswers)).onSubmit(CYAExternal, NormalMode)(formRequest)
      redirectLocation(result) mustBe Some(routes.NotToldAnyChangesController.show.url)
    }

    "onSubmit redirect to anything else page when the viewType is CYAExternal" in {
      val formRequest = requestWithForm(Map("value" -> "false"))
      val userAnswers = emptyUserAnswers.set(HaveYouChangedExternalPage, true).success.value
      val result = controller(Some(userAnswers)).onSubmit(CYAExternal, NormalMode)(formRequest)
      redirectLocation(result) mustBe Some(routes.AnythingElseController.onPageLoad(NormalMode).url)
    }

    "removeInternal" must {
      "remove data and redirect to mini check your answers page when fromMiniCYA flag is true" in {
        when(mockSessionRepository.set(any())).thenReturn(Future.successful(true))
        val result = controller(Some(emptyUserAnswers)).removeInternal(Escalators.toString, NormalMode, fromMiniCYA = true)(authenticatedFakeRequest)
          status(result) mustBe SEE_OTHER
          redirectLocation(result) mustBe Some(routes.SmallCheckYourAnswersController.onPageLoad(CYAInternal, NormalMode).url)
        }

      "remove data for SecurityCamera and redirect to mini check your answers page when fromMiniCYA flag is true" in {
        when(mockSessionRepository.set(any())).thenReturn(Future.successful(true))
        val result = controller(Some(emptyUserAnswers)).removeInternal(SecurityCamera.toString, NormalMode, fromMiniCYA = true)(authenticatedFakeRequest)
        status(result) mustBe SEE_OTHER
        redirectLocation(result) mustBe Some(routes.SmallCheckYourAnswersController.onPageLoad(CYAInternal, NormalMode).url)
      }

      "remove data for invalid InternalFeature and redirect to mini check your answers page when fromMiniCYA flag is true" in {
        val result = intercept[Exception](
          await(controller(Some(emptyUserAnswers)).removeInternal("Random", NormalMode, fromMiniCYA = true)(authenticatedFakeRequest))
        )
        result.getMessage mustBe "no internal feature chosen to remove"
      }

      "remove data and redirect to check your answers page when fromMiniCYA flag is false and userAnswers is not empty" in {
        when(mockSessionRepository.set(any())).thenReturn(Future.successful(true))
        val userAnswers = emptyUserAnswers.set(HowMuchOfPropertyAirConPage, HowMuchOfProperty.AllOf).success.value
        val result = controller(Some(userAnswers)).removeInternal(Escalators.toString, NormalMode, fromMiniCYA = false)(authenticatedFakeRequest)
        status(result) mustBe SEE_OTHER
        redirectLocation(result) mustBe Some(routes.CheckYourAnswersController.onPageLoad().url)
      }

      "remove data and redirect to check your answers page when fromMiniCYA flag is false" in {
        when(mockSessionRepository.set(any())).thenReturn(Future.successful(true))
        val result = controller(Some(emptyUserAnswers)).removeInternal(Escalators.toString, NormalMode, fromMiniCYA = false)(authenticatedFakeRequest)
        status(result) mustBe SEE_OTHER
        redirectLocation(result) mustBe Some(routes.CheckYourAnswersController.onPageLoad().url)
      }

      "redirect to journey recovery Expired for a POST if no existing data is found" in {
        val result = controller(None).removeInternal(Escalators.toString, NormalMode, fromMiniCYA = false)(authenticatedFakeRequest)
        status(result) mustBe 303
        redirectLocation(result).value mustBe controllers.routes.JourneyRecoveryController.onPageLoad().url
      }
    }

    "removeExternal" must {
      "remove data and redirect to mini check your answers page when fromMiniCYA flag is true" in {
        when(mockSessionRepository.set(any())).thenReturn(Future.successful(true))
        val result = controller(Some(emptyUserAnswers)).removeExternal(ExternalFeature.SolarPanels.toString, NormalMode, fromMiniCYA = true)(authenticatedFakeRequest)
        status(result) mustBe SEE_OTHER
        redirectLocation(result) mustBe Some(routes.SmallCheckYourAnswersController.onPageLoad(CYAExternal, NormalMode).url)
      }

      "remove data and redirect to check your answers page when fromMiniCYA flag is false" in {
        when(mockSessionRepository.set(any())).thenReturn(Future.successful(true))
        val result = controller(Some(emptyUserAnswers)).removeExternal(ExternalFeature.SolarPanels.toString, NormalMode, fromMiniCYA = false)(authenticatedFakeRequest)
        status(result) mustBe SEE_OTHER
        redirectLocation(result) mustBe Some(routes.CheckYourAnswersController.onPageLoad().url)
      }

      "remove data and redirect to check your answers page when fromMiniCYA flag is false and userAnswers is not empty" in {
        when(mockSessionRepository.set(any())).thenReturn(Future.successful(true))
        val userAnswers = emptyUserAnswers.set(WhatHappenedToLoadingBaysPage, WhatHappenedTo.Added).success.value
        val result = controller(Some(userAnswers)).removeExternal(ExternalFeature.SolarPanels.toString, NormalMode, fromMiniCYA = false)(authenticatedFakeRequest)
        status(result) mustBe SEE_OTHER
        redirectLocation(result) mustBe Some(routes.CheckYourAnswersController.onPageLoad().url)
      }

      "remove data for invalid ExternalFeature and redirect to mini check your answers page when fromMiniCYA flag is true" in {
        val result = intercept[Exception](
          await(controller(Some(emptyUserAnswers)).removeExternal("Random", NormalMode, fromMiniCYA = true)(authenticatedFakeRequest))
        )
        result.getMessage mustBe "no external feature chosen to remove"
      }
      
      "redirect to journey recovery Expired for a POST if no existing data is found" in {
        val result = controller(None).removeExternal(ExternalFeature.SolarPanels.toString, NormalMode, fromMiniCYA = false)(authenticatedFakeRequest)
        status(result) mustBe 303
        redirectLocation(result).value mustBe controllers.routes.JourneyRecoveryController.onPageLoad().url
      }
    }
  }

}
