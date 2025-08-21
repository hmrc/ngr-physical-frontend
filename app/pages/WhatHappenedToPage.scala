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

package pages

import models.{ExternalFeatureGroup1, HowMuchOfProperty, WhatHappenedTo, WithName}
import play.api.libs.json.JsPath

case object WhatHappenedToLoadingBaysPage extends QuestionPage[WhatHappenedTo] {

  override def path: JsPath = JsPath \ toString

  override def toString: String = "whatHappenedToLoadingBays"
}

case object WhatHappenedToLockupGaragesPage extends QuestionPage[WhatHappenedTo] {
  override def path: JsPath = JsPath \ toString

  override def toString: String = "WhatHappenedToLockupGarages"
}

case object WhatHappenedToOutdoorSeatingPage extends QuestionPage[WhatHappenedTo] {

  override def path: JsPath = JsPath \ toString

  override def toString: String = "whatHappenedToOutdoorSeating"
}

case object WhatHappenedToParkingPage extends QuestionPage[WhatHappenedTo] {
  override def path: JsPath = JsPath \ toString

  override def toString: String = "WhatHappenedToParking"
}

case object WhatHappenedToSolarPanelsPage extends QuestionPage[WhatHappenedTo] {

  override def path: JsPath = JsPath \ toString

  override def toString: String = "whatHappenedToSolarPanels"
}

case object WhatHappenedToAdvertisingDisplaysPage extends QuestionPage[WhatHappenedTo] {
  override def path: JsPath = JsPath \ toString

  override def toString: String = "WhatHappenedToAdvertisingDisplays"
}

case object WhatHappenedToBikeShedsPage extends QuestionPage[WhatHappenedTo] {

  override def path: JsPath = JsPath \ toString

  override def toString: String = "whatHappenedToBikeSheds"
}

case object WhatHappenedToCanopiesPage extends QuestionPage[WhatHappenedTo] {
  override def path: JsPath = JsPath \ toString

  override def toString: String = "WhatHappenedToCanopies"
}

case object WhatHappenedToLandHardSurfacedFencedPage extends QuestionPage[WhatHappenedTo] {

  override def path: JsPath = JsPath \ toString

  override def toString: String = "whatHappenedToLandHardSurfacedFenced"
}

case object WhatHappenedToLandHardSurfacedOpenPage extends QuestionPage[WhatHappenedTo] {
  override def path: JsPath = JsPath \ toString

  override def toString: String = "WhatHappenedToLandHardSurfacedOpen"
}

case object WhatHappenedToLandGravelledFencedPage extends QuestionPage[WhatHappenedTo] {

  override def path: JsPath = JsPath \ toString

  override def toString: String = "whatHappenedToLandGravelledFenced"
}

case object WhatHappenedToLandGravelledOpenPage extends QuestionPage[WhatHappenedTo] {
  override def path: JsPath = JsPath \ toString

  override def toString: String = "WhatHappenedToLandGravelledOpen"
}

case object WhatHappenedToLandUnsurfacedFencedPage extends QuestionPage[WhatHappenedTo] {

  override def path: JsPath = JsPath \ toString

  override def toString: String = "whatHappenedToLandUnsurfacedFenced"
}

case object WhatHappenedToLandUnsurfacedOpenPage extends QuestionPage[WhatHappenedTo] {
  override def path: JsPath = JsPath \ toString

  override def toString: String = "WhatHappenedToLandUnsurfacedOpen"
}

case object WhatHappenedToPortableBuildingsPage extends QuestionPage[WhatHappenedTo] {

  override def path: JsPath = JsPath \ toString

  override def toString: String = "whatHappenedToPortableBuildings"
}

case object WhatHappenedToShippingContainersPage extends QuestionPage[WhatHappenedTo] {
  override def path: JsPath = JsPath \ toString

  override def toString: String = "WhatHappenedToShippingContainers"
}


