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

import controllers.routes.HowMuchOfPropertyController.*
import models.InternalFeature.*
import pages.*
import play.api.i18n.Messages
import play.api.mvc.Call
import uk.gov.hmrc.govukfrontend.views.Aliases.Text
import uk.gov.hmrc.govukfrontend.views.viewmodels.radios.RadioItem

sealed trait HowMuchOfProperty

object HowMuchOfProperty extends Enumerable.Implicits {
  case object AllOf extends WithName("all") with HowMuchOfProperty
  case object SomeOf extends WithName("some") with HowMuchOfProperty
  case object NoneOf extends WithName("none") with HowMuchOfProperty

  val values: Seq[HowMuchOfProperty] = Seq(
    AllOf, SomeOf, NoneOf
  )

  def options(feature: InternalFeatureGroup1)(implicit messages: Messages): Seq[RadioItem] = values.zipWithIndex.map {
    case (value, index) =>
      val strings = messageKeys(feature)
      val content = strings(value.toString)
      RadioItem(
        content = Text(messages(content)),
        value   = Some(value.toString),
        id      = Some(s"value_$index")
      )
  }

  def messageKeys(feature: InternalFeatureGroup1): Map[String, String] = {
    val key = feature.toString
    Map(
      "title" -> s"howMuchOfProperty.$key.title",
      "hint" -> s"howMuchOfProperty.$key.hint",
      "all" -> s"howMuchOfProperty.$key.all",
      "some" -> s"howMuchOfProperty.$key.some",
      "none" -> s"howMuchOfProperty.$key.none"
    )
  }

  def errorKey(feature: InternalFeatureGroup1): String = s"howMuchOfProperty.${feature.toString}.error"

  def pageLoadAction(feature: InternalFeatureGroup1, mode: Mode): Call =
    feature match {
      case AirConditioning => onPageLoadAirCon(mode)
      case Escalators => onPageLoadEscalator(mode)
      case GoodsLift => onPageLoadGoodsLift(mode)
      case PassengerLift => onPageLoadPassengerLift(mode)
      case CompressedAir => onPageLoadCompressedAir(mode)
      case Heating => onPageLoadHeating(mode)
      case Sprinklers => onPageLoadSprinklers(mode)
    }

  def submitAction(feature: InternalFeatureGroup1, mode: Mode): Call =
    feature match {
      case AirConditioning => onSubmitAirCon(mode)
      case Escalators => onSubmitEscalator(mode)
      case GoodsLift => onSubmitGoodsLift(mode)
      case PassengerLift => onSubmitPassengerLift(mode)
      case CompressedAir => onSubmitCompressedAir(mode)
      case Heating => onSubmitHeating(mode)
      case Sprinklers => onSubmitSprinklers(mode)
    }

  def page(feature: InternalFeatureGroup1): QuestionPage[HowMuchOfProperty] =
    feature match {
      case AirConditioning => HowMuchOfPropertyAirConPage
      case Escalators => HowMuchOfPropertyEscalatorsPage
      case GoodsLift => HowMuchOfPropertyGoodsLiftPage
      case PassengerLift => HowMuchOfPropertyPassengerLiftPage
      case CompressedAir => HowMuchOfPropertyCompressedAirPage
      case Heating => HowMuchOfPropertyHeatingPage
      case Sprinklers => HowMuchOfPropertySprinklersPage
    }

  implicit val enumerable: Enumerable[HowMuchOfProperty] =
    Enumerable(values.map(v => v.toString -> v)*)
}
