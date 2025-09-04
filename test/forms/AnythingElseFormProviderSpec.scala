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

import forms.behaviours.{BooleanFieldBehaviours, FieldBehaviours}
import play.api.data.FormError

class AnythingElseFormProviderSpec extends FieldBehaviours {

  val form = new AnythingElseFormProvider()()

  ".anythingElse" - {

    val fieldName = "value"
    val requiredError = "anythingElse.error.required"
    val textFieldName = "text"
    val textRequiredError = "anythingElse.error.requiredText"

    "bind mandatory fields" in {
      val result = form.bind(Map(fieldName -> "true", textFieldName -> "some text"))
      result.errors mustBe empty
    }

    "error when no text and true" in {
      val result = form.bind(Map(fieldName -> "true"))
      result.errors mustBe List(FormError(textFieldName, textRequiredError))
    }

    "fail the mandatory field validation on missing data" in {
      val result = form.bind(Map(fieldName -> "", textFieldName -> ""))
      result.errors mustBe List(
        FormError(fieldName, requiredError),
      )
    }

  }
}
