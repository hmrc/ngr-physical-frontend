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

import forms.HowMuchOfPropertyFormProvider
import helpers.ControllerSpecSupport
import models.{HowMuchOfProperty, InternalFeatureGroup1, NormalMode}
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.when
import play.api.test.Helpers.*
import views.html.HowMuchOfPropertyView

import scala.concurrent.Future

class HowMuchOfPropertyControllerSpec extends ControllerSpecSupport {
  lazy val view: HowMuchOfPropertyView = inject[HowMuchOfPropertyView]
  lazy val formProvider: HowMuchOfPropertyFormProvider = HowMuchOfPropertyFormProvider()
  private val controller: HowMuchOfPropertyController = new HowMuchOfPropertyController(
    sessionRepository = mockSessionRepository, navigator = navigator, identify = fakeAuth, getData = fakeData(None), formProvider = formProvider, controllerComponents = mcc, view = view
  )

  "HowMuchOfPropertyController" should {
    InternalFeatureGroup1.values.foreach { feature =>

      "onPageLoad" must {
        s"return 200: ${feature.toString}" in {
          val result = controller.onPageLoad(feature, NormalMode)(authenticatedFakeRequest)
          status(result) mustBe 200
        }
        s"return HTML: ${feature.toString}" in {
          val result = controller.onPageLoad(feature, NormalMode)(authenticatedFakeRequest)
          contentType(result) mustBe Some("text/html")
          charset(result) mustBe Some("utf-8")
        }
      }
      "onSubmit" must {
        s"redirect with valid form submission: ${feature.toString}" in {
          when(mockSessionRepository.set(any())).thenReturn(Future.successful(true))
          val formRequest = requestWithForm(Map("value" -> "some"))
          val result = controller.onSubmit(feature, NormalMode)(formRequest)
          status(result) mustBe 303
        }
        s"Bad request with invalid form: ${feature.toString}" in {
          when(mockSessionRepository.set(any())).thenReturn(Future.successful(true))
          val formRequest = requestWithForm(Map("value" -> "wow"))
          val result = controller.onSubmit(feature, NormalMode)(formRequest)
          status(result) mustBe 400
        }
      }

    }
  }

}