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
import models.InternalFeature.Escalators
import models.{CYAExternal, CYAInternal, CheckMode, ExternalFeature, NormalMode, UserAnswers}
import org.apache.pekko.http.scaladsl.model.HttpHeader.ParsingResult.Ok
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.when
import play.api.test.Helpers.*
import views.html.SmallCheckYourAnswersView

import scala.concurrent.Future

class SmallCheckYourAnswersControllerSpec extends ControllerSpecSupport {
  lazy val view: SmallCheckYourAnswersView = inject[SmallCheckYourAnswersView]
  lazy val formProvider: SmallCheckYourAnswersFormProvider = SmallCheckYourAnswersFormProvider()
  private val controller: SmallCheckYourAnswersController = new SmallCheckYourAnswersController(
    identify = fakeAuth, getData = fakeData(Some(emptyUserAnswers)), sessionRepository = mockSessionRepository, formProvider = formProvider, controllerComponents = mcc, view = view
  )

  "SmallCheckYourAnswersController" should {
    "onPageLoad" must {
      "return 200" in {
        val result = controller.onPageLoad(CYAInternal, NormalMode)(authenticatedFakeRequest)
        status(result) mustBe OK
      }

      "return HTML" in {
        val result = controller.onPageLoad(CYAInternal, NormalMode)(authenticatedFakeRequest)
        contentType(result) mustBe Some("text/html")
        charset(result) mustBe Some("utf-8")
      }
    }

    "onSubmit" must {
      "redirect to have you changed Internal" in {
        val formRequest = requestWithForm(Map("value" -> "true"))
        val result = controller.onSubmit(CYAInternal, NormalMode)(formRequest)
        redirectLocation(result) mustBe Some(routes.WhichInternalFeatureController.onPageLoad(NormalMode).url)
      }
      "redirect to have you changed External" in {
        val formRequest = requestWithForm(Map("value" -> "false"))
        val result = controller.onSubmit(CYAInternal, NormalMode)(formRequest)
        redirectLocation(result) mustBe Some(routes.HaveYouChangedController.loadExternal(NormalMode).url)
      }
      "redirect to which external feature" in {
        val formRequest = requestWithForm(Map("value" -> "true"))
        val result = controller.onSubmit(CYAExternal, NormalMode)(formRequest)
        redirectLocation(result) mustBe Some(routes.WhichExternalFeatureController.onPageLoad(NormalMode).url)
      }
      "redirect to you have no changes" in {
        val formRequest = requestWithForm(Map("value" -> "false"))
        val result = controller.onSubmit(CYAExternal, NormalMode)(formRequest)
        redirectLocation(result) mustBe Some(routes.NotToldAnyChangesController.show.url)
      }

      "redirect to check your answers page when the mode is CheckMode" in {
        val formRequest = requestWithForm(Map("value" -> "false"))
        val result = controller.onSubmit(CYAInternal, CheckMode)(formRequest)
        redirectLocation(result) mustBe Some(routes.CheckYourAnswersController.onPageLoad().url)
      }
      "bad request when no selection" in {
        val result = controller.onSubmit(CYAInternal, NormalMode)(authenticatedFakeRequest)
        status(result) mustBe 400
      }
    }

    "removeInternal" must {
      "remove data and redirect to mini check your answers page when fromMiniCYA flag is true" in {
        when(mockSessionRepository.set(any())).thenReturn(Future.successful(true))
        val result = controller.removeInternal(Escalators.toString, NormalMode, fromMiniCYA = true)(authenticatedFakeRequest)
          status(result) mustBe SEE_OTHER
          redirectLocation(result) mustBe Some(routes.SmallCheckYourAnswersController.onPageLoad(CYAInternal, NormalMode).url)
        }

      "remove data and redirect to check your answers page when fromMiniCYA flag is false" in {
        when(mockSessionRepository.set(any())).thenReturn(Future.successful(true))
        val result = controller.removeInternal(Escalators.toString, NormalMode, fromMiniCYA = false)(authenticatedFakeRequest)
        status(result) mustBe SEE_OTHER
        redirectLocation(result) mustBe Some(routes.CheckYourAnswersController.onPageLoad().url)
      }
    }

    "removeExternal" must {
      "remove data and redirect to mini check your answers page when fromMiniCYA flag is true" in {
        when(mockSessionRepository.set(any())).thenReturn(Future.successful(true))
        val result = controller.removeExternal(ExternalFeature.SolarPanels.toString, NormalMode, fromMiniCYA = true)(authenticatedFakeRequest)
        status(result) mustBe SEE_OTHER
        redirectLocation(result) mustBe Some(routes.SmallCheckYourAnswersController.onPageLoad(CYAExternal, NormalMode).url)
      }

      "remove data and redirect to check your answers page when fromMiniCYA flag is false" in {
        when(mockSessionRepository.set(any())).thenReturn(Future.successful(true))
        val result = controller.removeExternal(ExternalFeature.SolarPanels.toString, NormalMode, fromMiniCYA = false)(authenticatedFakeRequest)
        status(result) mustBe SEE_OTHER
        redirectLocation(result) mustBe Some(routes.CheckYourAnswersController.onPageLoad().url)
      }
    }
  }

}
