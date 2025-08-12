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

package forms

import javax.inject.Inject
import forms.mappings.Mappings
import play.api.data.Form
import models.{HowMuchOfProperty, InternalFeature, InternalFeatureGroup1}

class HowMuchOfPropertyFormProvider @Inject() extends Mappings {

  def errorKey(feature: InternalFeatureGroup1): String = feature match {
    case InternalFeature.AirConditioning => "howMuchOfProperty.airConditioning.error"
    case InternalFeature.Escalators => "howMuchOfProperty.escalators.error"
    case InternalFeature.GoodsLift => "howMuchOfProperty.goodsLift.error"
    case InternalFeature.PassengerLift => "howMuchOfProperty.passengerLift.error"
    case InternalFeature.Heating => "howMuchOfProperty.heating.error"
    case InternalFeature.Sprinklers => "howMuchOfProperty.sprinklers.error"
  }
  def apply(feature: InternalFeatureGroup1): Form[HowMuchOfProperty] =
    Form(
      "value" -> enumerable[HowMuchOfProperty](errorKey(feature))
    )
}
