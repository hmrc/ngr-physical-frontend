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
import helpers.{ControllerSpecSupport, TestData}
import models.ExternalFeature.LoadingBays
import models.NavBarPageContents.createDefaultNavBar
import models.requests.IdentifierRequest
import models.{ExternalFeature, Mode, NormalMode, UserAnswers, WhatHappenedTo}
import navigation.{FakeNavigator, Navigator}
import org.mockito.ArgumentMatchers.{any, contains}
import org.mockito.Mockito.when
import org.scalatest.TryValues.convertTryToSuccessOrFailure
import org.scalatestplus.mockito.MockitoSugar
import play.api.i18n.MessagesApi
import play.api.inject.bind
import play.api.mvc.{AnyContentAsEmpty, AnyContentAsFormUrlEncoded, Call}
import play.api.test.FakeRequest
import play.api.test.Helpers.*
import repositories.SessionRepository
import views.html.WhatHappenedToView

import scala.concurrent.Future

class WhatHappenedToControllerSpec extends ControllerSpecSupport with TestData {
  lazy val view: WhatHappenedToView = inject[WhatHappenedToView]
  lazy val formProvider: WhatHappenedToFormProvider = WhatHappenedToFormProvider()

  private def createController(userAnswers: Option[UserAnswers]): WhatHappenedToController = new WhatHappenedToController(
    sessionRepository = mockSessionRepository, navigator = navigator, identify = fakeAuth, getData = fakeData(userAnswers), requireData = fakeRequireData(userAnswers), formProvider = formProvider, controllerComponents = mcc, view = view
  )

  "WhatHappenedToController" should {
    ExternalFeature.values.foreach { feature =>

      "onPageLoad" must {
        s"return 200: ${feature.toString}" in {
          val result = onPageLoad(Some(emptyUserAnswers), feature, NormalMode)(authenticatedFakeRequest)
          status(result) mustBe 200
        }

        s"return 200 with pre-populates date: ${feature.toString}" in {
          val userAnswers = emptyUserAnswers.set(WhatHappenedTo.page(feature), WhatHappenedTo.Added).success.value
          val result = onPageLoad(Some(userAnswers), feature, NormalMode)(authenticatedFakeRequest)
          status(result) mustBe 200
          contentAsString(result) must include("added")
        }

        s"redirect to journey recovery Expired for a GET if no existing data is found: ${feature.toString}" in {
          val result = onPageLoad(None, feature, NormalMode)(authenticatedFakeRequest)
          status(result) mustBe 303
          redirectLocation(result).value mustBe routes.JourneyRecoveryController.onPageLoad().url
        }
      }

      "onSubmit" must {
        s"redirect with valid form submission: ${feature.toString}" in {
          when(mockSessionRepository.set(any())).thenReturn(Future.successful(true))
          val formRequest = requestWithForm(Map("value" -> "added"))
          val result = onSubmit(Some(emptyUserAnswers), feature, NormalMode)(formRequest)
          status(result) mustBe 303
        }

        s"Bad request with invalid form: ${feature.toString}" in {
          when(mockSessionRepository.set(any())).thenReturn(Future.successful(true))
          val formRequest: IdentifierRequest[AnyContentAsFormUrlEncoded] = requestWithForm(Map("value" -> "wow"))
          val result = onSubmit(Some(emptyUserAnswers), feature, NormalMode)(formRequest)
          status(result) mustBe 400
        }

        s"redirect to journey recovery Expired for a POST if no existing data is found: ${feature.toString}" in {
          val formRequest = requestWithForm(Map("value" -> "added"))
          val result = onSubmit(None, feature, NormalMode)(formRequest)
          status(result) mustBe 303
          redirectLocation(result).value mustBe routes.JourneyRecoveryController.onPageLoad().url
        }
      }
    }

    def onPageLoad(userAnswers: Option[UserAnswers], feature: ExternalFeature, mode: Mode)(fakeRequest: IdentifierRequest[AnyContentAsEmpty.type]) = {
      val controller = createController(userAnswers)
      feature match {
        case LoadingBays => controller.onPageLoadLoadingBays(mode)(fakeRequest)
        case ExternalFeature.LockupGarages => controller.onPageLoadLockupGarage(mode)(fakeRequest)
        case ExternalFeature.OutdoorSeating => controller.onPageLoadOutdoorSeating(mode)(fakeRequest)
        case ExternalFeature.Parking => controller.onPageLoadParking(mode)(fakeRequest)
        case ExternalFeature.SolarPanels => controller.onPageLoadSolarPanels(mode)(fakeRequest)
        case ExternalFeature.AdvertisingDisplays => controller.onPageLoadAdvertisingDisplays(mode)(fakeRequest)
        case ExternalFeature.BikeSheds => controller.onPageLoadBikeSheds(mode)(fakeRequest)
        case ExternalFeature.Canopies => controller.onPageLoadCanopies(mode)(fakeRequest)
        case ExternalFeature.LandHardSurfacedFenced => controller.onPageLoadLandHardSurfacedFenced(mode)(fakeRequest)
        case ExternalFeature.LandHardSurfacedOpen => controller.onPageLoadLandHardSurfacedOpen(mode)(fakeRequest)
        case ExternalFeature.LandGravelledFenced => controller.onPageLoadLandGravelledFenced(mode)(fakeRequest)
        case ExternalFeature.LandGravelledOpen => controller.onPageLoadLandGravelledOpen(mode)(fakeRequest)
        case ExternalFeature.LandUnsurfacedFenced => controller.onPageLoadLandUnsurfacedFenced(mode)(fakeRequest)
        case ExternalFeature.LandUnsurfacedOpen => controller.onPageLoadLandUnsurfacedOpen(mode)(fakeRequest)
        case ExternalFeature.PortableBuildings => controller.onPageLoadPortableBuildings(mode)(fakeRequest)
        case ExternalFeature.ShippingContainers => controller.onPageLoadShippingContainers(mode)(fakeRequest)
      }
    }

    def onSubmit(userAnswers: Option[UserAnswers], feature: ExternalFeature, mode: Mode)(formRequest: IdentifierRequest[AnyContentAsFormUrlEncoded]) = {
      val controller = createController(userAnswers)
      feature match {
        case LoadingBays => controller.onPageSubmitLoadLoadingBays(mode)(formRequest)
        case ExternalFeature.LockupGarages => controller.onPageSubmitLockupGarage(mode)(formRequest)
        case ExternalFeature.OutdoorSeating => controller.onPageSubmitOutdoorSeating(mode)(formRequest)
        case ExternalFeature.Parking => controller.onPageSubmitParking(mode)(formRequest)
        case ExternalFeature.SolarPanels => controller.onPageSubmitSolarPanels(mode)(formRequest)
        case ExternalFeature.AdvertisingDisplays => controller.onPageSubmitAdvertisingDisplays(mode)(formRequest)
        case ExternalFeature.BikeSheds => controller.onPageSubmitBikeSheds(mode)(formRequest)
        case ExternalFeature.Canopies => controller.onPageSubmitCanopies(mode)(formRequest)
        case ExternalFeature.LandHardSurfacedFenced => controller.onPageSubmitLandHardSurfacedFenced(mode)(formRequest)
        case ExternalFeature.LandHardSurfacedOpen => controller.onPageSubmitLandHardSurfacedOpen(mode)(formRequest)
        case ExternalFeature.LandGravelledFenced => controller.onPageSubmitLandGravelledFenced(mode)(formRequest)
        case ExternalFeature.LandGravelledOpen => controller.onPageSubmitLandGravelledOpen(mode)(formRequest)
        case ExternalFeature.LandUnsurfacedFenced => controller.onPageSubmitLandUnsurfacedFenced(mode)(formRequest)
        case ExternalFeature.LandUnsurfacedOpen => controller.onPageSubmitLandUnsurfacedOpen(mode)(formRequest)
        case ExternalFeature.PortableBuildings => controller.onPageSubmitPortableBuildings(mode)(formRequest)
        case ExternalFeature.ShippingContainers => controller.onPageSubmitShippingContainers(mode)(formRequest)
      }
    }
  }
}
