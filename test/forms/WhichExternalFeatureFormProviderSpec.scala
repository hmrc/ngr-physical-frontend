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

import forms.behaviours.OptionFieldBehaviours
import models.ExternalFeature
import org.scalatest.matchers.should.Matchers.should
import play.api.data.{Form, FormError}

class WhichExternalFeatureFormProviderSpec extends OptionFieldBehaviours {

  val form = new WhichExternalFeatureFormProvider()()

  val fieldName = "value"
  val requiredKey = "whichExternalFeature.error.required"
  val dropdownErrorKey = "whichExternalFeature.error.dropdown"

  ".value" - {

    behave like optionsField[ExternalFeature](
      form,
      fieldName,
      validValues = ExternalFeature.values,
      invalidError = FormError(fieldName, requiredKey)
    )

    behave like otherSelectionField(
      form,
      baseField = fieldName,
      otherField = "otherSelect",
      validOtherValue = ExternalFeature.AdvertisingDisplays.toString,
      invalidOtherValue = "invalid-feature",
      dropdownError = FormError("other-select", dropdownErrorKey)
    )
  }

  // Custom behaviour for 'other' selection logic
  def otherSelectionField(
                           form: Form[ExternalFeature],
                           baseField: String,
                           otherField: String,
                           validOtherValue: String,
                           invalidOtherValue: String,
                           dropdownError: FormError
                         ): Unit = {

    "bind successfully when 'other' is selected with a valid otherSelect value" in {
      val data = Map(baseField -> "other", otherField -> validOtherValue)
      form.bind(data).value mustBe ExternalFeature.withNameOption(validOtherValue)
    }

    "fail to bind when 'other' is selected but otherSelect is missing" in {
      val data = Map(baseField -> "other")
      form.bind(data).errors should contain(dropdownError)
    }

    "fail to bind when 'other' is selected but otherSelect is empty" in {
      val data = Map(baseField -> "other", otherField -> "")
      form.bind(data).errors should contain(dropdownError)
    }

    "fail to bind when 'other' is selected but otherSelect is invalid" in {
      val data = Map(baseField -> "other", otherField -> invalidOtherValue)
      form.bind(data).errors should contain(dropdownError)
    }
  }
}