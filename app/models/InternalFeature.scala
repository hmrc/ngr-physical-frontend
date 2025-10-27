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
import models.InternalFeature.*
import models.requests.OptionalDataRequest
import pages.*
import play.api.i18n.Messages
import play.api.mvc.{AnyContent, Call}
import repositories.SessionRepository
import uk.gov.hmrc.govukfrontend.views.Aliases.{SelectItem, Text}
import uk.gov.hmrc.govukfrontend.views.html.components.{GovukErrorMessage, GovukHint, GovukLabel, GovukSelect}
import uk.gov.hmrc.govukfrontend.views.html.helpers.{GovukFormGroup, GovukHintAndErrorMessage}
import uk.gov.hmrc.govukfrontend.views.viewmodels.radios.RadioItem
import uk.gov.hmrc.govukfrontend.views.viewmodels.select.Select
import uk.gov.hmrc.govukfrontend.views.viewmodels.summarylist.SummaryListRow
import utils.StringUtils.camelCaseToHyphen
import viewmodels.govuk.summarylist.*
import viewmodels.implicits.*

sealed trait InternalFeature

sealed trait InternalFeatureGroup1 extends InternalFeature

object InternalFeature extends Enumerable.Implicits {

  case object AirConditioning extends WithName("airConditioning") with InternalFeatureGroup1
  case object Escalators extends WithName("escalators") with InternalFeatureGroup1
  case object GoodsLift extends WithName("goodsLift") with InternalFeatureGroup1
  case object PassengerLift extends WithName("passengerLift") with InternalFeatureGroup1
  case object SecurityCamera extends WithName("securityCamera") with InternalFeature
  case object CompressedAir extends WithName("compressedAir") with InternalFeatureGroup1
  case object Heating extends WithName("heating") with InternalFeatureGroup1
  case object Sprinklers extends WithName("sprinklers") with InternalFeatureGroup1

  val values: Seq[InternalFeature] = Seq(
    AirConditioning, Escalators, GoodsLift, PassengerLift, SecurityCamera, CompressedAir, Heating, Sprinklers
  )

  def withNameOption(name: String): Option[InternalFeature] =
    values.find(_.toString == name)

  def toGroup1(feature: InternalFeature): Option[InternalFeatureGroup1] = feature match {
    case f: InternalFeatureGroup1 => Some(f)
    case _ => None
  }

  def getAnswersToSend(userAnswers: UserAnswers): Seq[(InternalFeature, String)] = {
    InternalFeature.values.map {
      case feature: InternalFeatureGroup1 => (feature, userAnswers.get(HowMuchOfProperty.page(feature)).getOrElse("").toString())
      case SecurityCamera => (SecurityCamera, userAnswers.get(SecurityCamerasChangePage).getOrElse("").toString())
    }.filter {case (_, v) => v != ""}
  }


  def getAnswers(userAnswers: UserAnswers, mode: Mode, fromMiniCYA: Boolean = false)(implicit messages: Messages): Seq[SummaryListRow] = {
    InternalFeature.values.flatMap {
      case feature: InternalFeatureGroup1 =>
        userAnswers.get(HowMuchOfProperty.page(feature)).map { value =>
          SummaryListRowViewModel(
            key = s"internalFeature.${feature.toString}",
            value = ValueViewModel(valueString(feature, value.toString)),
            actions = Seq(
              ActionItemViewModel("site.change", changeLink(feature, mode).url),
              ActionItemViewModel("site.remove", routes.SureWantRemoveChangeController.onPageLoad(camelCaseToHyphen(feature.toString), mode, fromMiniCYA).url)
            ),
            actionClasses = "govuk-!-width-one-third"
          )
        }

      case SecurityCamera =>
        userAnswers.get(SecurityCamerasChangePage).map { value =>
          SummaryListRowViewModel(
            key = "internalFeature.securityCamera",
            value = ValueViewModel(value.toString),
            actions = Seq(
              ActionItemViewModel("site.change", changeLink(SecurityCamera, mode).url),
              ActionItemViewModel("site.remove", routes.SureWantRemoveChangeController.onPageLoad(camelCaseToHyphen(SecurityCamera.toString), mode, fromMiniCYA).url)
            ),
            actionClasses = "govuk-!-width-one-third"
          )
        }
    }
  }

  def changeLink(feature: InternalFeature, mode: Mode): Call = {
    feature match {
      case AirConditioning => routes.HowMuchOfPropertyController.onPageLoadAirCon(mode)
      case Escalators => routes.HowMuchOfPropertyController.onPageLoadEscalator(mode)
      case GoodsLift => routes.HowMuchOfPropertyController.onPageLoadGoodsLift(mode)
      case PassengerLift => routes.HowMuchOfPropertyController.onPageLoadPassengerLift(mode)
      case SecurityCamera => routes.SecurityCamerasChangeController.onPageLoad(mode)
      case CompressedAir => routes.HowMuchOfPropertyController.onPageLoadCompressedAir(mode)
      case Heating => routes.HowMuchOfPropertyController.onPageLoadHeating(mode)
      case Sprinklers => routes.HowMuchOfPropertyController.onPageLoadSprinklers(mode)
    }
  }

  val pageSet: Seq[QuestionPage[? >: HowMuchOfProperty & Int]] = Seq(
    HowMuchOfPropertyAirConPage,
    HowMuchOfPropertyHeatingPage,
    HowMuchOfPropertySprinklersPage,
    HowMuchOfPropertyGoodsLiftPage,
    HowMuchOfPropertyEscalatorsPage,
    HowMuchOfPropertyPassengerLiftPage,
    HowMuchOfPropertyCompressedAirPage,
    SecurityCamerasChangePage
  )


  def valueString(feature: InternalFeatureGroup1, value: String)(implicit messages: Messages): String = {
    if (value == "none") {
      feature match {
        case CompressedAir => messages("internalFeature.compressedAir.none")
        case _ => messages("internalFeature.none")
      }
    } else {
      feature match {
        case CompressedAir if value == "all" => messages("internalFeature.compressedAir.added")
        case _ => messages(s"internalFeature.${feature.toString}.value", value)
      }
    }
  }

  def options(implicit messages: Messages): Seq[RadioItem] = {

    val (firstFive, remaining) = values.splitAt(5)

    val hardcodedItems = firstFive.zipWithIndex.map {
      case (value, index) =>
        RadioItem(
          content = Text(messages(s"internalFeature.${value.toString}")),
          value = Some(value.toString),
          id = Some(s"value_$index")
        )
    }

    val dropdownModel = Select(
      id = "other-select",
      name = "otherSelect",
      items = SelectItem(value = None, text = messages("whichInternalFeature.chooseOther")) +:
        remaining.map { value =>
         SelectItem(value = Some(value.toString), text = messages(s"internalFeature.${value.toString}"))
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

object InternalFeatureGroup1 {
  val values: Seq[InternalFeatureGroup1] = Seq(
    AirConditioning, Escalators, GoodsLift, PassengerLift, CompressedAir, Heating, Sprinklers
  )
}