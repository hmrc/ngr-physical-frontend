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

import forms.behaviours.FieldBehaviours
import play.api.data.FormError

import scala.collection.immutable.List

class ChangeToUseOfSpaceFormProviderSpec extends FieldBehaviours {

  val form = new ChangeToUseOfSpaceFormProvider()()

  ".selectUseOfSpace" - {

    val fieldName = "selectUseOfSpace"
    val requiredKey = "changeToUseOfSpace.selectUseOfSpace.error.required"

    val booleanFieldName = "hasPlanningPermission"
    val booleanRequiredKey = "changeToUseOfSpace.hasPlanningPermission.error.required"

    val referenceFieldName = "permissionReference"
    val referenceRequiredKey =  "changeToUseOfSpace.permissionReference.error.required"
    val referenceLengthKey =  "changeToUseOfSpace.permissionReference.error.length"

    "bind mandatory fields" in {
      val result = form.bind(Map(s"$fieldName[0]" -> "rearrangedTheUseOfSpace", booleanFieldName -> "true", referenceFieldName -> "12345"))
      result.errors mustBe empty
    }

    "bind mandatory fields when 'hasPlanningPermission' value is 'false'" in {
      val result = form.bind(Map(s"$fieldName[0]" -> "rearrangedTheUseOfSpace", booleanFieldName -> "false"))
      result.errors mustBe empty
    }

    "fail the mandatory field validation on missing data" in {
      val result = form.bind( Map("selectUseOfSpace" -> "", booleanFieldName -> ""))
      result.errors mustBe List(
        FormError(fieldName, requiredKey),
        FormError(booleanFieldName, booleanRequiredKey)
      )
    }

    "fail the mandatory field validation when 'permissionReference' is missing data" in {

      val data = Map(s"$fieldName[0]" -> "rearrangedTheUseOfSpace", booleanFieldName -> "true", referenceFieldName -> "")
      val result = form.bind(data)
      result.errors mustBe List(FormError(referenceFieldName, referenceRequiredKey))
    }

    "fail the max length field validation when 'permissionReference'" in {

      val data = Map(s"$fieldName[0]" -> "rearrangedTheUseOfSpace", booleanFieldName -> "true", referenceFieldName -> "Ref"*10)
      val result = form.bind(data)
      result.errors mustBe List(FormError(referenceFieldName, referenceLengthKey, List(20)))
    }

    "fail the mandatory field validation when 'permissionReference' is missing field" in {

      val data = Map(s"$fieldName[0]" -> "rearrangedTheUseOfSpace", booleanFieldName -> "true")
      val result = form.bind(data)
      result.errors mustBe List(FormError(referenceFieldName, referenceRequiredKey))
    }
  }

}
