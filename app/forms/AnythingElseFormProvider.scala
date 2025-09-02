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
import play.api.data.Forms.*
import play.api.data.validation.{Constraint, Invalid, Valid}
import play.api.libs.json.{Json, OFormat}

case class AnythingElseData(value: Boolean, text: Option[String])

object AnythingElseData {
  implicit val format: OFormat[AnythingElseData] = Json.format
}

class AnythingElseFormProvider @Inject() extends Mappings {

  private val conditionalTextRequired: Constraint[AnythingElseData] = Constraint("conditionalText.required") { data =>
    if (data.value && data.text.forall(_.trim.isEmpty)) {
      Invalid("anythingElse.error.textRequired")
    } else {
      Valid
    }
  }

  def apply(): Form[AnythingElseData] =
    Form(
      mapping(
        "value" -> boolean("anythingElse.error.required"),
        "text" -> optional(text())
      )(AnythingElseData.apply)((x: AnythingElseData) => Some((x.value, x.text)))
        .verifying(conditionalTextRequired)
    )
}
