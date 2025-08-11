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

import forms.mappings.Mappings
import models.InternalFeature
import play.api.data.Forms.*
import play.api.data.format.Formatter
import play.api.data.{Form, FormError}
import javax.inject.Inject

class WhichInternalFeatureFormProvider @Inject() extends Mappings {

  def formatter: Formatter[InternalFeature] = new Formatter[InternalFeature] {

    override def bind(key: String, data: Map[String, String]): Either[Seq[FormError], InternalFeature] =
      data.get(key).filter(_.nonEmpty) match {
        case Some("other") =>
          val otherValue = data.get("otherSelect").flatMap(InternalFeature.withNameOption)
          otherValue.toRight(Seq(FormError("other-select", "whichInternalFeature.error.dropdown")))
        case Some(value) =>
          InternalFeature.withNameOption(value)
            .toRight(Seq(FormError(key, "whichInternalFeature.error.required")))
        case None =>
          Left(Seq(FormError(key, "whichInternalFeature.error.required")))
      }

    override def unbind(key: String, value: InternalFeature): Map[String, String] =
      Map(key -> value.toString)
  }

  def apply(): Form[InternalFeature] = {
    Form(
      "value" -> of(formatter)
    )
  }

}
