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

import controllers.routes.WhatHappenedToController.*
import pages.*
import play.api.i18n.Messages
import uk.gov.hmrc.govukfrontend.views.Aliases.Text
import uk.gov.hmrc.govukfrontend.views.viewmodels.radios.RadioItem
import models.ExternalFeature.*
import play.api.mvc.Call

sealed trait WhatHappenedTo

object WhatHappenedTo extends Enumerable.Implicits {

  case object Added extends WithName("added") with WhatHappenedTo
  case object RemovedSome extends WithName("removedSome") with WhatHappenedTo
  case object RemovedAll extends WithName("removedAll") with WhatHappenedTo

  val values: Seq[WhatHappenedTo] = Seq(
    Added, RemovedSome, RemovedAll
  )

  def messageKeys(feature: ExternalFeature): Map[String, String] = {
    val key = feature.toString
    Map(
      "title" -> s"whatHappenedTo.$key.title",
      "hint" -> s"whatHappenedTo.$key.hint",
      "added" -> s"whatHappenedTo.$key.added",
      "removedSome" -> s"whatHappenedTo.$key.remove.some",
      "removedAll" -> s"whatHappenedTo.$key.remove.all"
    )
  }

  def options(feature: ExternalFeature)(implicit messages: Messages): Seq[RadioItem] = values.zipWithIndex.map {
    case (value, index) =>
      val strings = messageKeys(feature)
      val content = strings(value.toString)
      RadioItem(
        content = Text(messages(content)),
        value = Some(value.toString),
        id = Some(s"value_$index")
      )
  }

  def errorKey(feature: ExternalFeature): String = s"whatHappenedTo.${feature.toString}.error"

  def pageLoadAction(feature: ExternalFeature, mode: Mode, assessmentId: AssessmentId): Call =
    feature match {
      case ExternalFeature.LoadingBays => onPageLoadLoadingBays(mode, assessmentId)
      case ExternalFeature.LockupGarages => onPageLoadLockupGarage(mode, assessmentId)
      case ExternalFeature.OutdoorSeating => onPageLoadOutdoorSeating(mode, assessmentId)
      case ExternalFeature.Parking => onPageLoadParking(mode, assessmentId)
      case ExternalFeature.SolarPanels => onPageLoadSolarPanels(mode, assessmentId)
      case ExternalFeature.AdvertisingDisplays => onPageLoadAdvertisingDisplays(mode, assessmentId)
      case ExternalFeature.BikeSheds => onPageLoadBikeSheds(mode, assessmentId)
      case ExternalFeature.Canopies => onPageLoadCanopies(mode, assessmentId)
      case ExternalFeature.LandHardSurfacedFenced => onPageLoadLandHardSurfacedFenced(mode, assessmentId)
      case ExternalFeature.LandHardSurfacedOpen => onPageLoadLandHardSurfacedOpen(mode, assessmentId)
      case ExternalFeature.LandGravelledFenced => onPageLoadLandGravelledFenced(mode, assessmentId)
      case ExternalFeature.LandGravelledOpen => onPageLoadLandGravelledOpen(mode, assessmentId)
      case ExternalFeature.LandUnsurfacedFenced => onPageLoadLandUnsurfacedFenced(mode, assessmentId)
      case ExternalFeature.LandUnsurfacedOpen => onPageLoadLandUnsurfacedOpen(mode, assessmentId)
      case ExternalFeature.PortableBuildings => onPageLoadPortableBuildings(mode, assessmentId)
      case ExternalFeature.ShippingContainers => onPageLoadShippingContainers(mode, assessmentId)
    }
  
  def submitAction(feature: ExternalFeature, mode: Mode, assessmentId: AssessmentId): Call =
    feature match {
      case LoadingBays => onPageSubmitLoadLoadingBays(mode: Mode, assessmentId)
      case LockupGarages => onPageSubmitLockupGarage(mode: Mode, assessmentId)
      case OutdoorSeating => onPageSubmitOutdoorSeating(mode: Mode, assessmentId)
      case Parking => onPageSubmitParking(mode: Mode, assessmentId)
      case SolarPanels => onPageSubmitSolarPanels(mode: Mode, assessmentId)
      case AdvertisingDisplays => onPageSubmitAdvertisingDisplays(mode: Mode, assessmentId)
      case BikeSheds => onPageSubmitBikeSheds(mode: Mode, assessmentId)
      case Canopies => onPageSubmitCanopies(mode: Mode, assessmentId)
      case LandHardSurfacedFenced => onPageSubmitLandHardSurfacedFenced(mode: Mode, assessmentId)
      case LandHardSurfacedOpen => onPageSubmitLandHardSurfacedOpen(mode: Mode, assessmentId)
      case LandGravelledFenced => onPageSubmitLandGravelledFenced(mode: Mode, assessmentId)
      case LandGravelledOpen => onPageSubmitLandGravelledOpen(mode: Mode, assessmentId)
      case LandUnsurfacedFenced => onPageSubmitLandUnsurfacedFenced(mode: Mode, assessmentId)
      case LandUnsurfacedOpen => onPageSubmitLandUnsurfacedOpen(mode: Mode, assessmentId)
      case PortableBuildings=> onPageSubmitPortableBuildings(mode: Mode, assessmentId)
      case ShippingContainers => onPageSubmitShippingContainers(mode: Mode, assessmentId)
    }
  
  def page(feature: ExternalFeature, assessmentId: AssessmentId): QuestionPage[WhatHappenedTo] =
    feature match {
      case LoadingBays => WhatHappenedToLoadingBaysPage(assessmentId)
      case LockupGarages => WhatHappenedToLockupGaragesPage(assessmentId)
      case OutdoorSeating => WhatHappenedToOutdoorSeatingPage(assessmentId)
      case Parking => WhatHappenedToParkingPage(assessmentId)
      case SolarPanels => WhatHappenedToSolarPanelsPage(assessmentId)
      case AdvertisingDisplays => WhatHappenedToAdvertisingDisplaysPage(assessmentId)
      case BikeSheds => WhatHappenedToBikeShedsPage(assessmentId)
      case Canopies => WhatHappenedToCanopiesPage(assessmentId)
      case LandHardSurfacedFenced => WhatHappenedToLandHardSurfacedFencedPage(assessmentId)
      case LandHardSurfacedOpen => WhatHappenedToLandHardSurfacedOpenPage(assessmentId)
      case LandGravelledFenced => WhatHappenedToLandGravelledFencedPage(assessmentId)
      case LandGravelledOpen => WhatHappenedToLandGravelledOpenPage(assessmentId)
      case LandUnsurfacedFenced => WhatHappenedToLandUnsurfacedFencedPage(assessmentId)
      case LandUnsurfacedOpen => WhatHappenedToLandUnsurfacedOpenPage(assessmentId)
      case PortableBuildings=> WhatHappenedToPortableBuildingsPage(assessmentId)
      case ShippingContainers => WhatHappenedToShippingContainersPage(assessmentId)
    }

  implicit val enumerable: Enumerable[WhatHappenedTo] =
    Enumerable(values.map(v => v.toString -> v)*)
}
