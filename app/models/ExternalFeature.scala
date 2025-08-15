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

import play.api.i18n.Messages
import uk.gov.hmrc.govukfrontend.views.Aliases.{SelectItem, Text}
import uk.gov.hmrc.govukfrontend.views.html.components.{GovukErrorMessage, GovukHint, GovukLabel, GovukSelect}
import uk.gov.hmrc.govukfrontend.views.html.helpers.{GovukFormGroup, GovukHintAndErrorMessage}
import uk.gov.hmrc.govukfrontend.views.viewmodels.radios.RadioItem
import uk.gov.hmrc.govukfrontend.views.viewmodels.select.Select

sealed trait ExternalFeature

object ExternalFeature extends Enumerable.Implicits {

  case object LoadingBays extends WithName("loadingBays") with ExternalFeature
  case object LockupGarages extends WithName("lockupGarages") with ExternalFeature
  case object OutdoorSeating extends WithName("outdoorSeating") with ExternalFeature
  case object Parking extends WithName("parking") with ExternalFeature
  case object SolarPanels extends WithName("solarPanels") with ExternalFeature
  case object AdvertisingDisplays extends WithName("advertisingDisplays") with ExternalFeature
  case object BikeSheds extends WithName("bikeSheds") with ExternalFeature
  case object Canopies extends WithName("canopies") with ExternalFeature
  case object LandHardSurfacedFenced extends WithName("landHardSurfacedFenced") with ExternalFeature
  case object LandHardSurfacedOpen extends WithName("landHardSurfacedOpen") with ExternalFeature
  case object LandGravelledFenced extends WithName("landGravelledFenced") with ExternalFeature
  case object LandGravelledOpen extends WithName("landGravelledOpen") with ExternalFeature
  case object LandUnsurfacedFenced extends WithName("landUnsurfacedFenced") with ExternalFeature
  case object LandUnsurfacedOpen extends WithName("landUnSurfacedOpen") with ExternalFeature
  case object PortableBuildings extends WithName("portableBuildings") with ExternalFeature
  case object ShippingContainers extends WithName("shippingContainers") with ExternalFeature

  val values: Seq[ExternalFeature] = Seq(
    LoadingBays, LockupGarages, OutdoorSeating, Parking, SolarPanels, AdvertisingDisplays,
    BikeSheds, Canopies, LandHardSurfacedFenced, LandHardSurfacedOpen, LandGravelledFenced,
    LandGravelledOpen, LandUnsurfacedFenced, LandUnsurfacedOpen, PortableBuildings, ShippingContainers
  )

  def withNameOption(name: String): Option[ExternalFeature] =
    values.find(_.toString == name)

  def options(implicit messages: Messages): Seq[RadioItem] = {

    val (firstFive, remaining) = values.splitAt(5)

    val hardcodedItems = firstFive.zipWithIndex.map {
      case (value, index) =>
        RadioItem(
          content = Text(messages(s"whichExternalFeature.${value.toString}")),
          value = Some(value.toString),
          id = Some(s"value_$index")
        )
    }

    val dropdownModel = Select(
      id = "other-select",
      name = "otherSelect",
      items = SelectItem(value = None, text = messages("whichExternalFeature.chooseOther")) +:
        remaining.map { value =>
         SelectItem(value = Some(value.toString), text = messages(s"whichExternalFeature.${value.toString}"))
      }
    )

    val govukSelectComponent: GovukSelect = new GovukSelect(
      new GovukLabel(),
      new GovukFormGroup(),
      new GovukHintAndErrorMessage(new GovukHint(), new GovukErrorMessage())
    )

    val extraItem = RadioItem(
      content = Text(messages("whichExternalFeature.other")),
      value = Some("other"),
      id = Some("value_5"),
      conditionalHtml = Some(govukSelectComponent.apply(dropdownModel))
    )

    hardcodedItems :+ extraItem

  }

  implicit val enumerable: Enumerable[ExternalFeature] =
    Enumerable(values.map(v => v.toString -> v)*)
}
