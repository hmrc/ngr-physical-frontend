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

package models

import controllers.routes
import models.{ExternalFeature, NormalMode}
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers.mustBe
import org.scalatest.matchers.should.Matchers
import pages.*
import play.api.mvc.Call

class ExternalFeatureSpec extends AnyFreeSpec with Matchers {

  "ExternalFeature" - {
    "should contain all the pageSet" in {
      ExternalFeature.pageSet mustBe List(
        WhatHappenedToLoadingBaysPage,
        WhatHappenedToLockupGaragesPage,
        WhatHappenedToOutdoorSeatingPage,
        WhatHappenedToParkingPage,
        WhatHappenedToSolarPanelsPage,
        WhatHappenedToAdvertisingDisplaysPage,
        WhatHappenedToBikeShedsPage,
        WhatHappenedToCanopiesPage,
        WhatHappenedToLandHardSurfacedFencedPage,
        WhatHappenedToLandHardSurfacedOpenPage,
        WhatHappenedToLandGravelledFencedPage,
        WhatHappenedToLandGravelledOpenPage,
        WhatHappenedToLandUnsurfacedFencedPage,
        WhatHappenedToLandUnsurfacedOpenPage,
        WhatHappenedToPortableBuildingsPage,
        WhatHappenedToShippingContainersPage
      )
    }
  }

  "changeLink" - {
    "should return correct Call for each ExternalFeature" in {
      ExternalFeature.changeLink(ExternalFeature.LoadingBays, NormalMode) shouldBe routes.WhatHappenedToController.onPageLoadLoadingBays(NormalMode)
      ExternalFeature.changeLink(ExternalFeature.LockupGarages, NormalMode) shouldBe routes.WhatHappenedToController.onPageLoadLockupGarage(NormalMode)
      ExternalFeature.changeLink(ExternalFeature.OutdoorSeating, NormalMode) shouldBe routes.WhatHappenedToController.onPageLoadOutdoorSeating(NormalMode)
      ExternalFeature.changeLink(ExternalFeature.Parking, NormalMode) shouldBe routes.WhatHappenedToController.onPageLoadParking(NormalMode)
      ExternalFeature.changeLink(ExternalFeature.SolarPanels, NormalMode) shouldBe routes.WhatHappenedToController.onPageLoadSolarPanels(NormalMode)
      ExternalFeature.changeLink(ExternalFeature.AdvertisingDisplays, NormalMode) shouldBe routes.WhatHappenedToController.onPageLoadAdvertisingDisplays(NormalMode)
      ExternalFeature.changeLink(ExternalFeature.BikeSheds, NormalMode) shouldBe routes.WhatHappenedToController.onPageLoadBikeSheds(NormalMode)
      ExternalFeature.changeLink(ExternalFeature.Canopies, NormalMode) shouldBe routes.WhatHappenedToController.onPageLoadCanopies(NormalMode)
      ExternalFeature.changeLink(ExternalFeature.LandHardSurfacedFenced, NormalMode) shouldBe routes.WhatHappenedToController.onPageLoadLandHardSurfacedFenced(NormalMode)
      ExternalFeature.changeLink(ExternalFeature.LandHardSurfacedOpen, NormalMode) shouldBe routes.WhatHappenedToController.onPageLoadLandHardSurfacedOpen(NormalMode)
      ExternalFeature.changeLink(ExternalFeature.LandGravelledFenced, NormalMode) shouldBe routes.WhatHappenedToController.onPageLoadLandGravelledFenced(NormalMode)
      ExternalFeature.changeLink(ExternalFeature.LandGravelledOpen, NormalMode) shouldBe routes.WhatHappenedToController.onPageLoadLandGravelledOpen(NormalMode)
      ExternalFeature.changeLink(ExternalFeature.LandUnsurfacedFenced, NormalMode) shouldBe routes.WhatHappenedToController.onPageLoadLandUnsurfacedFenced(NormalMode)
      ExternalFeature.changeLink(ExternalFeature.LandUnsurfacedOpen, NormalMode) shouldBe routes.WhatHappenedToController.onPageLoadLandUnsurfacedOpen(NormalMode)
      ExternalFeature.changeLink(ExternalFeature.PortableBuildings, NormalMode) shouldBe routes.WhatHappenedToController.onPageLoadPortableBuildings(NormalMode)
      ExternalFeature.changeLink(ExternalFeature.ShippingContainers, NormalMode) shouldBe routes.WhatHappenedToController.onPageLoadShippingContainers(NormalMode)
    }
  }
}