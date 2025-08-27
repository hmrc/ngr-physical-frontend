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

  def pageLoadAction(feature: ExternalFeature, mode: Mode): Call =
    feature match {
      case ExternalFeature.LoadingBays => onPageLoadLoadingBays(mode)
      case ExternalFeature.LockupGarages => onPageLoadLockupGarage(mode)
      case ExternalFeature.OutdoorSeating => onPageLoadOutdoorSeating(mode)
      case ExternalFeature.Parking => onPageLoadParking(mode)
      case ExternalFeature.SolarPanels => onPageLoadSolarPanels(mode)
      case ExternalFeature.AdvertisingDisplays => onPageLoadAdvertisingDisplays(mode)
      case ExternalFeature.BikeSheds => onPageLoadBikeSheds(mode)
      case ExternalFeature.Canopies => onPageLoadCanopies(mode)
      case ExternalFeature.LandHardSurfacedFenced => onPageLoadLandHardSurfacedFenced(mode)
      case ExternalFeature.LandHardSurfacedOpen => onPageLoadLandHardSurfacedOpen(mode)
      case ExternalFeature.LandGravelledFenced => onPageLoadLandGravelledFenced(mode)
      case ExternalFeature.LandGravelledOpen => onPageLoadLandGravelledOpen(mode)
      case ExternalFeature.LandUnsurfacedFenced => onPageLoadLandUnsurfacedFenced(mode)
      case ExternalFeature.LandUnsurfacedOpen => onPageLoadLandUnsurfacedOpen(mode)
      case ExternalFeature.PortableBuildings => onPageLoadPortableBuildings(mode)
      case ExternalFeature.ShippingContainers => onPageLoadShippingContainers(mode)
    }
  
  def submitAction(feature: ExternalFeature, mode: Mode): Call =
    feature match {
      case LoadingBays => onPageSubmitLoadLoadingBays(mode: Mode)
      case LockupGarages => onPageSubmitLockupGarage(mode: Mode)
      case OutdoorSeating => onPageSubmitOutdoorSeating(mode: Mode)
      case Parking => onPageSubmitParking(mode: Mode)
      case SolarPanels => onPageSubmitSolarPanels(mode: Mode)
      case AdvertisingDisplays => onPageSubmitAdvertisingDisplays(mode: Mode)
      case BikeSheds => onPageSubmitBikeSheds(mode: Mode)
      case Canopies => onPageSubmitCanopies(mode: Mode)
      case LandHardSurfacedFenced => onPageSubmitLandHardSurfacedFenced(mode: Mode)
      case LandHardSurfacedOpen => onPageSubmitLandHardSurfacedOpen(mode: Mode)
      case LandGravelledFenced => onPageSubmitLandGravelledFenced(mode: Mode)
      case LandGravelledOpen => onPageSubmitLandGravelledOpen(mode: Mode)
      case LandUnsurfacedFenced => onPageSubmitLandUnsurfacedFenced(mode: Mode)
      case LandUnsurfacedOpen => onPageSubmitLandUnsurfacedOpen(mode: Mode)
      case PortableBuildings=> onPageSubmitPortableBuildings(mode: Mode)
      case ShippingContainers => onPageSubmitShippingContainers(mode: Mode)
    }
  
  def page(feature: ExternalFeature): QuestionPage[WhatHappenedTo] =
    feature match {
      case LoadingBays => WhatHappenedToLoadingBaysPage
      case LockupGarages => WhatHappenedToLockupGaragesPage
      case OutdoorSeating => WhatHappenedToOutdoorSeatingPage
      case Parking => WhatHappenedToParkingPage
      case SolarPanels => WhatHappenedToSolarPanelsPage
      case AdvertisingDisplays => WhatHappenedToAdvertisingDisplaysPage
      case BikeSheds => WhatHappenedToBikeShedsPage
      case Canopies => WhatHappenedToCanopiesPage
      case LandHardSurfacedFenced => WhatHappenedToLandHardSurfacedFencedPage
      case LandHardSurfacedOpen => WhatHappenedToLandHardSurfacedOpenPage
      case LandGravelledFenced => WhatHappenedToLandGravelledFencedPage
      case LandGravelledOpen => WhatHappenedToLandHardSurfacedOpenPage
      case LandUnsurfacedFenced => WhatHappenedToLandUnsurfacedFencedPage
      case LandUnsurfacedOpen => WhatHappenedToLandUnsurfacedOpenPage
      case PortableBuildings=> WhatHappenedToPortableBuildingsPage
      case ShippingContainers => WhatHappenedToShippingContainersPage
    }

  implicit val enumerable: Enumerable[WhatHappenedTo] =
    Enumerable(values.map(v => v.toString -> v)*)
}
