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
import models.{ExternalFeature, InternalFeatureGroup1}
import org.apache.pekko.http.scaladsl.model.HttpHeader.ParsingResult.Ok
import play.api.test.Helpers.*
import views.html.SureWantRemoveChangeView

import scala.util.Random

class SureWantRemoveChangeControllerSpec extends ControllerSpecSupport {
  lazy val view: SureWantRemoveChangeView = inject[SureWantRemoveChangeView]
  lazy val formProvider: SureWantRemoveChangeFormProvider = SureWantRemoveChangeFormProvider()
  private val controller: SureWantRemoveChangeController = new SureWantRemoveChangeController(
    identify = fakeAuth, getData = fakeData(None), formProvider = formProvider, controllerComponents = mcc, view = view
  )

  "SureWantRemoveChangeController" should {
    val randomFeature = InternalFeatureGroup1.values(Random.nextInt(InternalFeatureGroup1.values.length))
    "onPageLoad" must {
      "return 200" in {
        val result = controller.onPageLoad(randomFeature.toString)(authenticatedFakeRequest)
        status(result) mustBe OK
      }

      "return HTML" in {
        val result = controller.onPageLoad(randomFeature.toString)(authenticatedFakeRequest)
        contentType(result) mustBe Some("text/html")
        charset(result) mustBe Some("utf-8")
      }
    }

    "onSubmit" must {
      "redirect to removeInternal" in {
        val formRequest = requestWithForm(Map("value" -> "true"))
        val randomInternalFeature = InternalFeatureGroup1.values(Random.nextInt(InternalFeatureGroup1.values.length))
        val result = controller.onSubmit(randomInternalFeature.toString)(formRequest)
        redirectLocation(result) mustBe Some(routes.SmallCheckYourAnswersController.removeInternal(randomInternalFeature.toString).url)
      }
      "redirect to removeExternal" in {
        val formRequest = requestWithForm(Map("value" -> "true"))
        val randomExternalFeature = ExternalFeature.values(Random.nextInt(ExternalFeature.values.length))
        val result = controller.onSubmit(randomExternalFeature.toString)(formRequest)
        redirectLocation(result) mustBe Some(routes.SmallCheckYourAnswersController.removeExternal(randomExternalFeature.toString).url)
      }
      "bad request when no selection" in {
        val randomFeature = InternalFeatureGroup1.values(Random.nextInt(InternalFeatureGroup1.values.length))
        val result = controller.onSubmit(randomFeature.toString)(authenticatedFakeRequest)
        status(result) mustBe 400
      }
    }
  }

}
