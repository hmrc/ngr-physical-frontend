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
import models.{ChangeToUseOfSpace, UseOfSpaces}
import play.api.data.Form
import play.api.data.Forms.*
import uk.gov.voa.play.form.ConditionalMappings.*
import models.UseOfSpaces.*

import javax.inject.Inject

class ChangeToUseOfSpaceFormProvider @Inject() extends Mappings {

   private val permissionReferenceMaxLength = 20
  
   def apply(): Form[ChangeToUseOfSpace] =
     println("Available enums: " + enumerable[UseOfSpaces]("error").withPrefix("selectUseOfSpace"))
     Form(
     mapping(
      "selectUseOfSpace" -> set(enumerable[UseOfSpaces]("changeToUseOfSpace.selectUseOfSpace.error.required")).verifying(nonEmptySet("changeToUseOfSpace.selectUseOfSpace.error.required")),
      "hasPlanningPermission" -> boolean("changeToUseOfSpace.hasPlanningPermission.error.required"),
       "permissionReference" -> mandatoryIfTrue("hasPlanningPermission", text("changeToUseOfSpace.permissionReference.error.required")
       .verifying(maxLength(permissionReferenceMaxLength, "changeToUseOfSpace.permissionReference.error.length")))
    )(ChangeToUseOfSpace.apply)((x: ChangeToUseOfSpace) => Some((x.selectUseOfSpace, x.hasPlanningPermission, x.permissionReference)))
   )
 }
