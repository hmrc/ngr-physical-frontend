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
import uk.gov.hmrc.govukfrontend.views.viewmodels.checkboxes.CheckboxItem
import uk.gov.hmrc.govukfrontend.views.viewmodels.content.Text
import viewmodels.govuk.checkbox.*

sealed trait UseOfSpaces

object UseOfSpaces extends Enumerable.Implicits {

  case object Rearrangedtheuseofspace extends WithName("rearrangedTheUseOfSpace") with UseOfSpaces
  case object Builtanextension extends WithName("builtAnExtension") with UseOfSpaces
  case object DemolishedPart extends WithName("demolishedPart") with UseOfSpaces

  val values: Seq[UseOfSpaces] = Seq(
    Rearrangedtheuseofspace,
    Builtanextension,
    DemolishedPart
  )

  def checkboxItems(implicit messages: Messages): Seq[CheckboxItem] =
    values.zipWithIndex.map {
      case (value, index) =>
        CheckboxItemViewModel(
          content = Text(messages(s"changeToUseOfSpace.${value.toString}")),
          fieldId = "selectUseOfSpace",
          index   = index,
          value   = value.toString
        )
    }

  implicit val enumerable: Enumerable[UseOfSpaces] =
    Enumerable(values.map(v => v.toString -> v)*)
}
