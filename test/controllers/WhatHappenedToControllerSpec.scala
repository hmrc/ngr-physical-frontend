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
import forms.WhatHappenedToFormProvider
import helpers.ControllerSpecSupport
import models.{ExternalFeature, NormalMode, UserAnswers, WhatHappenedTo}
import navigation.{FakeNavigator, Navigator}
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.when
import org.scalatestplus.mockito.MockitoSugar
import play.api.inject.bind
import play.api.mvc.Call
import play.api.test.FakeRequest
import play.api.test.Helpers.*
import repositories.SessionRepository
import views.html.WhatHappenedToView

import scala.concurrent.Future

class WhatHappenedToControllerSpec extends ControllerSpecSupport {
  lazy val view: WhatHappenedToView = inject[WhatHappenedToView]
  lazy val formProvider: WhatHappenedToFormProvider = WhatHappenedToFormProvider()

  private def controller(userAnswers: Option[UserAnswers]): WhatHappenedToController = new WhatHappenedToController(
    sessionRepository = mockSessionRepository, navigator = navigator, identify = fakeAuth, getData = fakeData(userAnswers), requireData = fakeRequireData(userAnswers), formProvider = formProvider, controllerComponents = mcc, view = view
  )

  "WhatHappenedToController" should {
    ExternalFeature.values.foreach { feature =>

      "onPageLoad" must {
        s"return 200: ${feature.toString}" in {
          val result = controller(Some(emptyUserAnswers)).onPageLoad(feature, NormalMode)(authenticatedFakeRequest)
          status(result) mustBe 200
        }
        s"return HTML: ${feature.toString}" in {
          val result = controller(Some(emptyUserAnswers)).onPageLoad(feature, NormalMode)(authenticatedFakeRequest)
          contentType(result) mustBe Some("text/html")
          charset(result) mustBe Some("utf-8")
        }

        s"redirect to journey recovery Expired for a GET if no existing data is found: ${feature.toString}" in {
          val result = controller(None).onPageLoad(feature, NormalMode)(authenticatedFakeRequest)
          status(result) mustBe 303
          redirectLocation(result).value mustBe controllers.routes.JourneyRecoveryController.onPageLoad().url
        }
      }

      "onSubmit" must {
        s"redirect with valid form submission: ${feature.toString}" in {
          when(mockSessionRepository.set(any())).thenReturn(Future.successful(true))
          val formRequest = requestWithForm(Map("value" -> "added"))
          val result = controller(Some(emptyUserAnswers)).onSubmit(feature, NormalMode)(formRequest)
          status(result) mustBe 303
        }

        s"Bad request with invalid form: ${feature.toString}" in {
          when(mockSessionRepository.set(any())).thenReturn(Future.successful(true))
          val formRequest = requestWithForm(Map("value" -> "wow"))
          val result = controller(Some(emptyUserAnswers)).onSubmit(feature, NormalMode)(formRequest)
          status(result) mustBe 400
        }

        s"redirect to journey recovery Expired for a POST if no existing data is found: ${feature.toString}" in {
          val formRequest = requestWithForm(Map("value" -> "added"))
          val result = controller(None).onSubmit(feature, NormalMode)(formRequest)
          status(result) mustBe 303
          redirectLocation(result).value mustBe controllers.routes.JourneyRecoveryController.onPageLoad().url
        }
      }

    }
  }


}
