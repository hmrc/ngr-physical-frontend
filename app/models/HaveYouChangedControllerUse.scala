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

import pages.{HaveYouChangedExternalPage, HaveYouChangedInternalPage, HaveYouChangedSpacePage, QuestionPage}
import play.api.mvc.JavascriptLiteral

sealed trait HaveYouChangedControllerUse

case object Space extends HaveYouChangedControllerUse

case object Internal extends HaveYouChangedControllerUse

case object External extends HaveYouChangedControllerUse

object HaveYouChangedControllerUse {

  implicit val jsLiteral: JavascriptLiteral[HaveYouChangedControllerUse] = {
    case Space => "space"
    case Internal => "internal"
    case External => "external"
  }

  def getMessageKeys(use: HaveYouChangedControllerUse): (String, String) =
    use match {
      case Space => ("haveYouChangedSpace.title", "haveYouChangedSpace.hint")
      case Internal => ("haveYouChangedInternal.title", "haveYouChangedInternal.hint")
      case External => ("haveYouChangedExternal.title", "haveYouChangedExternal.hint")
    }

  def pageType(use: HaveYouChangedControllerUse, assessmentId: AssessmentId): QuestionPage[Boolean] =
    use match {
      case Space => HaveYouChangedSpacePage(assessmentId)
      case Internal => HaveYouChangedInternalPage(assessmentId)
      case External => HaveYouChangedExternalPage(assessmentId)
    }

}
