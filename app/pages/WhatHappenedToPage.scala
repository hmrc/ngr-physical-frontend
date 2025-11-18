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

import models.{AssessmentId, ExternalFeature, HowMuchOfProperty, WhatHappenedTo, WithName}
import play.api.libs.json.JsPath

case class WhatHappenedToLoadingBaysPage(assessmentId: AssessmentId) extends QuestionPage[WhatHappenedTo] {

  override def path: JsPath = JsPath \ assessmentId.value \ toString

  override def toString: String = "whatHappenedToLoadingBays"
}

case class WhatHappenedToLockupGaragesPage(assessmentId: AssessmentId) extends QuestionPage[WhatHappenedTo] {
  override def path: JsPath = JsPath \ assessmentId.value \ toString

  override def toString: String = "WhatHappenedToLockupGarages"
}

case class WhatHappenedToOutdoorSeatingPage(assessmentId: AssessmentId) extends QuestionPage[WhatHappenedTo] {

  override def path: JsPath = JsPath \ assessmentId.value \ toString

  override def toString: String = "whatHappenedToOutdoorSeating"
}

case class WhatHappenedToParkingPage(assessmentId: AssessmentId) extends QuestionPage[WhatHappenedTo] {
  override def path: JsPath = JsPath \ assessmentId.value \ toString

  override def toString: String = "WhatHappenedToParking"
}

case class WhatHappenedToSolarPanelsPage(assessmentId: AssessmentId) extends QuestionPage[WhatHappenedTo] {

  override def path: JsPath = JsPath \ assessmentId.value \ toString

  override def toString: String = "whatHappenedToSolarPanels"
}

case class WhatHappenedToAdvertisingDisplaysPage(assessmentId: AssessmentId) extends QuestionPage[WhatHappenedTo] {
  override def path: JsPath = JsPath \ assessmentId.value \ toString

  override def toString: String = "WhatHappenedToAdvertisingDisplays"
}

case class WhatHappenedToBikeShedsPage(assessmentId: AssessmentId) extends QuestionPage[WhatHappenedTo] {

  override def path: JsPath = JsPath \ assessmentId.value \ toString

  override def toString: String = "whatHappenedToBikeSheds"
}

case class WhatHappenedToCanopiesPage(assessmentId: AssessmentId) extends QuestionPage[WhatHappenedTo] {
  override def path: JsPath = JsPath \ assessmentId.value \ toString

  override def toString: String = "WhatHappenedToCanopies"
}

case class WhatHappenedToLandHardSurfacedFencedPage(assessmentId: AssessmentId) extends QuestionPage[WhatHappenedTo] {

  override def path: JsPath = JsPath \ assessmentId.value \ toString

  override def toString: String = "whatHappenedToLandHardSurfacedFenced"
}

case class WhatHappenedToLandHardSurfacedOpenPage(assessmentId: AssessmentId) extends QuestionPage[WhatHappenedTo] {
  override def path: JsPath = JsPath \ assessmentId.value \ toString

  override def toString: String = "WhatHappenedToLandHardSurfacedOpen"
}

case class WhatHappenedToLandGravelledFencedPage(assessmentId: AssessmentId) extends QuestionPage[WhatHappenedTo] {

  override def path: JsPath = JsPath \ assessmentId.value \ toString

  override def toString: String = "whatHappenedToLandGravelledFenced"
}

case class WhatHappenedToLandGravelledOpenPage(assessmentId: AssessmentId) extends QuestionPage[WhatHappenedTo] {
  override def path: JsPath = JsPath \ assessmentId.value \ toString

  override def toString: String = "WhatHappenedToLandGravelledOpen"
}

case class WhatHappenedToLandUnsurfacedFencedPage(assessmentId: AssessmentId) extends QuestionPage[WhatHappenedTo] {

  override def path: JsPath = JsPath \ assessmentId.value \ toString

  override def toString: String = "whatHappenedToLandUnsurfacedFenced"
}

case class WhatHappenedToLandUnsurfacedOpenPage(assessmentId: AssessmentId) extends QuestionPage[WhatHappenedTo] {
  override def path: JsPath = JsPath \ assessmentId.value \ toString

  override def toString: String = "WhatHappenedToLandUnsurfacedOpen"
}

case class WhatHappenedToPortableBuildingsPage(assessmentId: AssessmentId) extends QuestionPage[WhatHappenedTo] {

  override def path: JsPath = JsPath \ assessmentId.value \ toString

  override def toString: String = "whatHappenedToPortableBuildings"
}

case class WhatHappenedToShippingContainersPage(assessmentId: AssessmentId) extends QuestionPage[WhatHappenedTo] {
  override def path: JsPath = JsPath \ assessmentId.value \ toString

  override def toString: String = "WhatHappenedToShippingContainers"
}


