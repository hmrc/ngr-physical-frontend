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

  private val assessmentId = AssessmentId("test-assessment-id")

  "ExternalFeature" - {
    "should contain all the pageSet" in {
      ExternalFeature.pageSet(assessmentId) mustBe List(
        WhatHappenedToLoadingBaysPage(assessmentId) ,
        WhatHappenedToLockupGaragesPage(assessmentId) ,
        WhatHappenedToOutdoorSeatingPage(assessmentId) ,
        WhatHappenedToParkingPage(assessmentId) ,
        WhatHappenedToSolarPanelsPage(assessmentId) ,
        WhatHappenedToAdvertisingDisplaysPage(assessmentId) ,
        WhatHappenedToBikeShedsPage(assessmentId) ,
        WhatHappenedToCanopiesPage(assessmentId) ,
        WhatHappenedToLandHardSurfacedFencedPage(assessmentId) ,
        WhatHappenedToLandHardSurfacedOpenPage(assessmentId) ,
        WhatHappenedToLandGravelledFencedPage(assessmentId) ,
        WhatHappenedToLandGravelledOpenPage(assessmentId) ,
        WhatHappenedToLandUnsurfacedFencedPage(assessmentId) ,
        WhatHappenedToLandUnsurfacedOpenPage(assessmentId) ,
        WhatHappenedToPortableBuildingsPage(assessmentId) ,
        WhatHappenedToShippingContainersPage(assessmentId)
      )
    }
  }

  "changeLink" - {
    "should return correct Call for each ExternalFeature" in {
      ExternalFeature.changeLink(ExternalFeature.LoadingBays, NormalMode, assessmentId) shouldBe routes.WhatHappenedToController.onPageLoadLoadingBays(NormalMode, assessmentId)
      ExternalFeature.changeLink(ExternalFeature.LockupGarages, NormalMode, assessmentId) shouldBe routes.WhatHappenedToController.onPageLoadLockupGarage(NormalMode, assessmentId)
      ExternalFeature.changeLink(ExternalFeature.OutdoorSeating, NormalMode, assessmentId) shouldBe routes.WhatHappenedToController.onPageLoadOutdoorSeating(NormalMode, assessmentId)
      ExternalFeature.changeLink(ExternalFeature.Parking, NormalMode, assessmentId) shouldBe routes.WhatHappenedToController.onPageLoadParking(NormalMode, assessmentId)
      ExternalFeature.changeLink(ExternalFeature.SolarPanels, NormalMode, assessmentId) shouldBe routes.WhatHappenedToController.onPageLoadSolarPanels(NormalMode, assessmentId)
      ExternalFeature.changeLink(ExternalFeature.AdvertisingDisplays, NormalMode, assessmentId) shouldBe routes.WhatHappenedToController.onPageLoadAdvertisingDisplays(NormalMode, assessmentId)
      ExternalFeature.changeLink(ExternalFeature.BikeSheds, NormalMode, assessmentId) shouldBe routes.WhatHappenedToController.onPageLoadBikeSheds(NormalMode, assessmentId)
      ExternalFeature.changeLink(ExternalFeature.Canopies, NormalMode, assessmentId) shouldBe routes.WhatHappenedToController.onPageLoadCanopies(NormalMode, assessmentId)
      ExternalFeature.changeLink(ExternalFeature.LandHardSurfacedFenced, NormalMode, assessmentId) shouldBe routes.WhatHappenedToController.onPageLoadLandHardSurfacedFenced(NormalMode, assessmentId)
      ExternalFeature.changeLink(ExternalFeature.LandHardSurfacedOpen, NormalMode, assessmentId) shouldBe routes.WhatHappenedToController.onPageLoadLandHardSurfacedOpen(NormalMode, assessmentId)
      ExternalFeature.changeLink(ExternalFeature.LandGravelledFenced, NormalMode, assessmentId) shouldBe routes.WhatHappenedToController.onPageLoadLandGravelledFenced(NormalMode, assessmentId)
      ExternalFeature.changeLink(ExternalFeature.LandGravelledOpen, NormalMode, assessmentId) shouldBe routes.WhatHappenedToController.onPageLoadLandGravelledOpen(NormalMode, assessmentId)
      ExternalFeature.changeLink(ExternalFeature.LandUnsurfacedFenced, NormalMode, assessmentId) shouldBe routes.WhatHappenedToController.onPageLoadLandUnsurfacedFenced(NormalMode, assessmentId)
      ExternalFeature.changeLink(ExternalFeature.LandUnsurfacedOpen, NormalMode, assessmentId) shouldBe routes.WhatHappenedToController.onPageLoadLandUnsurfacedOpen(NormalMode, assessmentId)
      ExternalFeature.changeLink(ExternalFeature.PortableBuildings, NormalMode, assessmentId) shouldBe routes.WhatHappenedToController.onPageLoadPortableBuildings(NormalMode, assessmentId)
      ExternalFeature.changeLink(ExternalFeature.ShippingContainers, NormalMode, assessmentId) shouldBe routes.WhatHappenedToController.onPageLoadShippingContainers(NormalMode, assessmentId)
    }
  }
}