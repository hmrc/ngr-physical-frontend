package models

import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.funsuite.AnyFunSuiteLike
import org.scalatest.matchers.must.Matchers.mustBe
import pages.*

class ExternalFeatureSpec extends AnyFreeSpec {

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

}
