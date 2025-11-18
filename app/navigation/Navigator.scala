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

package navigation

import controllers.routes
import models.{CYAViewType, *}
import pages.*
import play.api.mvc.Call
import utils.ChangeChecker

import javax.inject.{Inject, Singleton}

@Singleton
class Navigator @Inject() {

  private val normalRoutes: (Page, AssessmentId) => UserAnswers => Option[Call] = {
    case (WhenCompleteChangePage(_), assessmentId) => _ => Some(routes.HaveYouChangedController.loadSpace(NormalMode, assessmentId))
    case (HaveYouChangedSpacePage(_), assessmentId) => answers =>
      answers.get(HaveYouChangedSpacePage(assessmentId)) map {
        case true => routes.ChangeToUseOfSpaceController.onPageLoad(NormalMode, assessmentId)
        case false => routes.HaveYouChangedController.loadInternal(NormalMode, assessmentId)
      }
    case (ChangeToUseOfSpacePage(_), assessmentId) => _ => Some(routes.HaveYouChangedController.loadInternal(NormalMode, assessmentId))
    case (HaveYouChangedInternalPage(_), assessmentId) => answers =>
      answers.get(HaveYouChangedInternalPage(assessmentId)) map {
        case true => routes.WhichInternalFeatureController.onPageLoad(NormalMode, assessmentId)
        case false => routes.HaveYouChangedController.loadExternal(NormalMode, assessmentId)
      }
    case (HaveYouChangedExternalPage(_), assessmentId) => answers =>
      answers.get(HaveYouChangedExternalPage(assessmentId)) map {
        case true => routes.WhichExternalFeatureController.onPageLoad(NormalMode, assessmentId)
        case false => ChangeChecker.recheckForAnyChanges(answers, List(HaveYouChangedInternalPage(assessmentId), HaveYouChangedSpacePage(assessmentId)),
          routes.AnythingElseController.onPageLoad(NormalMode, assessmentId), routes.NotToldAnyChangesController.show(assessmentId))
      }
    case (page, assessmentId) if InternalFeature.pageSet(assessmentId).contains(page)  => _ => Some(routes.SmallCheckYourAnswersController.onPageLoad(CYAInternal, NormalMode, assessmentId))
    case (page, assessmentId) if ExternalFeature.pageSet(assessmentId).contains(page)  => _ => Some(routes.SmallCheckYourAnswersController.onPageLoad(CYAExternal, NormalMode, assessmentId))
    case (AnythingElsePage(_), assessmentId) => _ => Some(routes.SupportingDocumentsController.onPageLoad(assessmentId))
    case _ => _ => None
  }

  private val checkRouteMap: (Page, AssessmentId) => UserAnswers => Option[Call] = {
    case (HaveYouChangedSpacePage(_), assessmentId) => answers =>
      answers.get(HaveYouChangedSpacePage(assessmentId)) map {
        case true => routes.ChangeToUseOfSpaceController.onPageLoad(CheckMode, assessmentId)
        case false => routes.CheckYourAnswersController.onPageLoad(assessmentId)
      }
    case (HaveYouChangedInternalPage(_), assessmentId) => answers =>
      answers.get(HaveYouChangedInternalPage(assessmentId)) map {
        case true => routes.WhichInternalFeatureController.onPageLoad(CheckMode, assessmentId)
        case false => routes.HaveYouChangedController.loadExternal(CheckMode, assessmentId)
      }
    case (HaveYouChangedExternalPage(_), assessmentId) => answers =>
      answers.get(HaveYouChangedExternalPage(assessmentId)) map {
        case true => routes.WhichExternalFeatureController.onPageLoad(CheckMode, assessmentId)
        case false => routes.CheckYourAnswersController.onPageLoad(assessmentId)
      }
    case (page, assessmentId: AssessmentId) if InternalFeature.pageSet(assessmentId).contains(page) => _ => Some(routes.SmallCheckYourAnswersController.onPageLoad(CYAInternal, CheckMode, assessmentId))
    case (page, assessmentId: AssessmentId) if ExternalFeature.pageSet(assessmentId).contains(page)  => _ => Some(routes.SmallCheckYourAnswersController.onPageLoad(CYAExternal, CheckMode, assessmentId))
    case (page: Page, assessmentId: AssessmentId)  => _ => Some(routes.CheckYourAnswersController.onPageLoad(assessmentId))
  }

  def nextPage(page: Page, mode: Mode, userAnswers: UserAnswers, assessmentId: AssessmentId): Call = mode match {
    case NormalMode =>
      println("Navigator nextPage NormalMode called for page "+page)
      normalRoutes(page, assessmentId)(userAnswers).getOrElse(throw new RuntimeException("No selection - should be caught by form validation"))
    case CheckMode =>
      checkRouteMap(page, assessmentId)(userAnswers).getOrElse(throw new RuntimeException("No selection - should be caught by form validation"))
  }
}

