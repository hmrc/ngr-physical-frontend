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

sealed trait InternalFeature

object InternalFeature extends Enumerable.Implicits {

  case object AirConditioning extends WithName("airConditioning") with InternalFeature

  case object Escalators extends WithName("escalators") with InternalFeature

  case object GoodsLift extends WithName("goodsLift") with InternalFeature

  case object PassengerLift extends WithName("passengerLift") with InternalFeature

  case object SecurityCamera extends WithName("securityCamera") with InternalFeature

  case object CompressedAir extends WithName("compressedAir") with InternalFeature

  case object Heating extends WithName("heating") with InternalFeature

  case object Sprinklers extends WithName("sprinklers") with InternalFeature

  val values: Seq[InternalFeature] = Seq(
    AirConditioning, Escalators, GoodsLift, PassengerLift, SecurityCamera, CompressedAir, Heating, Sprinklers
  )

  def withNameOption(name: String): Option[InternalFeature] =
    values.find(_.toString == name)

  def options(implicit messages: Messages): Seq[RadioItem] = {

    val (firstFive, remaining) = values.splitAt(5)

    val hardcodedItems = firstFive.zipWithIndex.map {
      case (value, index) =>
        RadioItem(
          content = Text(messages(s"whichInternalFeature.${value.toString}")),
          value = Some(value.toString),
          id = Some(s"value_$index")
        )
    }

    val dropdownModel = Select(
      id = "other-select",
      name = "otherSelect",
      items = SelectItem(value = None, text = messages("whichInternalFeature.chooseOther")) +:
        remaining.map { value =>
         SelectItem(value = Some(value.toString), text = messages(s"whichInternalFeature.${value.toString}"))
      }
    )

    val govukSelectComponent: GovukSelect = new GovukSelect(
      new GovukLabel(),
      new GovukFormGroup(),
      new GovukHintAndErrorMessage(new GovukHint(), new GovukErrorMessage())
    )

    val extraItem = RadioItem(
      content = Text(messages("whichInternalFeature.other")),
      value = Some("other"),
      id = Some("value_5"),
      conditionalHtml = Some(govukSelectComponent.apply(dropdownModel))
    )

    hardcodedItems :+ extraItem

  }

  implicit val enumerable: Enumerable[InternalFeature] =
    Enumerable(values.map(v => v.toString -> v)*)
}
