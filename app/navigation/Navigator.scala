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
class Navigator @Inject()() {

  private val normalRoutes: Page => AssessmentId => UserAnswers => Option[Call] = {
    case WhenCompleteChangePage => id => _ => Some(routes.HaveYouChangedController.loadSpace(NormalMode, id))
    case HaveYouChangedSpacePage => id => answers =>
      answers.get(HaveYouChangedSpacePage) map {
        case true => routes.ChangeToUseOfSpaceController.onPageLoad(NormalMode, id)
        case false => routes.HaveYouChangedController.loadInternal(NormalMode, id)
      }
    case ChangeToUseOfSpacePage => id => _ => Some(routes.HaveYouChangedController.loadInternal(NormalMode, id))
    case HaveYouChangedInternalPage => id => answers =>
      answers.get(HaveYouChangedInternalPage) map {
        case true => routes.WhichInternalFeatureController.onPageLoad(NormalMode, id)
        case false => routes.HaveYouChangedController.loadExternal(NormalMode, id)
      }
    case HaveYouChangedExternalPage => id => answers =>
      answers.get(HaveYouChangedExternalPage) map {
        case true => routes.WhichExternalFeatureController.onPageLoad(NormalMode, id)
        case false => ChangeChecker.recheckForAnyChanges(answers, List(HaveYouChangedInternalPage, HaveYouChangedSpacePage),
          routes.AnythingElseController.onPageLoad(NormalMode, id), routes.NotToldAnyChangesController.show(id))
      }
    case page if InternalFeature.pageSet.contains(page) => id => _ => Some(routes.SmallCheckYourAnswersController.onPageLoad(CYAInternal, NormalMode, id))
    case page if ExternalFeature.pageSet.contains(page) => id => _ => Some(routes.SmallCheckYourAnswersController.onPageLoad(CYAExternal, NormalMode, id))
    case AnythingElsePage => id => _ => Some(routes.SupportingDocumentsController.onPageLoad(id))
    case _ => _ => _ => None
  }

  private val checkRouteMap: Page => AssessmentId => UserAnswers => Option[Call] = {
    case HaveYouChangedSpacePage => id => answers =>
      answers.get(HaveYouChangedSpacePage) map {
        case true => routes.ChangeToUseOfSpaceController.onPageLoad(CheckMode, id)
        case false => routes.CheckYourAnswersController.onPageLoad(id)
      }
    case HaveYouChangedInternalPage => id => answers =>
      answers.get(HaveYouChangedInternalPage) map {
        case true => routes.WhichInternalFeatureController.onPageLoad(CheckMode, id)
        case false => routes.HaveYouChangedController.loadExternal(CheckMode, id)
      }
    case HaveYouChangedExternalPage => id => answers =>
      answers.get(HaveYouChangedExternalPage) map {
        case true => routes.WhichExternalFeatureController.onPageLoad(CheckMode, id)
        case false => routes.CheckYourAnswersController.onPageLoad(id)
      }
    case page if InternalFeature.pageSet.contains(page) => id => _ => Some(routes.SmallCheckYourAnswersController.onPageLoad(CYAInternal, CheckMode, id))
    case page if ExternalFeature.pageSet.contains(page) => id => _ => Some(routes.SmallCheckYourAnswersController.onPageLoad(CYAExternal, CheckMode, id))
    case page: Page => id => _ => Some(routes.CheckYourAnswersController.onPageLoad(id))
  }

  def nextPage(page: Page, mode: Mode, userAnswers: UserAnswers, assessmentId: AssessmentId): Call = mode match {
    case NormalMode =>
      normalRoutes(page)(assessmentId)(userAnswers).getOrElse(throw new RuntimeException("No selection - should be caught by form validation"))
    case CheckMode =>
      checkRouteMap(page)(assessmentId)(userAnswers).getOrElse(throw new RuntimeException("No selection - should be caught by form validation"))
  }
}

