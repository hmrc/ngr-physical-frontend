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

import play.api.mvc.JavascriptLiteral

sealed trait CYAViewType
case object CYAInternal extends CYAViewType
case object CYAExternal extends CYAViewType

object CYAViewType {
  implicit val jsLiteral: JavascriptLiteral[CYAViewType] = {
    case CYAInternal => "CYAInternal"
    case CYAExternal => "CYAExternal"
  }
  def titleKey(viewType: CYAViewType): String = viewType match {
    case CYAInternal => "internalCheckYourAnswers.title"
    case CYAExternal => "externalCheckYourAnswers.title"
  }
  def anotherKey(viewType: CYAViewType): String = viewType match {
    case CYAInternal => "internalCheckYourAnswers.another"
    case CYAExternal => "externalCheckYourAnswers.another"
  }
  def noChangesKey(viewType: CYAViewType): String = viewType match {
    case CYAInternal => "internalCheckYourAnswers.changes.no"
    case CYAExternal => "externalCheckYourAnswers.changes.no"
  }
}
