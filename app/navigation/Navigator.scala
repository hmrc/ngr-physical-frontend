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

  private val normalRoutes: Page => UserAnswers => Option[Call] = {
    case WhenCompleteChangePage => _ => Some(routes.HaveYouChangedController.loadSpace(NormalMode))
    case HaveYouChangedSpacePage => answers =>
      answers.get(HaveYouChangedSpacePage) map {
        case true => routes.ChangeToUseOfSpaceController.onPageLoad(NormalMode)
        case false => routes.HaveYouChangedController.loadInternal(NormalMode)
      }
    case ChangeToUseOfSpacePage => _ => Some(routes.HaveYouChangedController.loadInternal(NormalMode))
    case HaveYouChangedInternalPage => answers =>
      answers.get(HaveYouChangedInternalPage) map {
        case true => routes.WhichInternalFeatureController.onPageLoad(NormalMode)
        case false => routes.HaveYouChangedController.loadExternal(NormalMode)
      }
    case HaveYouChangedExternalPage => answers =>
      answers.get(HaveYouChangedExternalPage) map {
        case true => routes.WhichExternalFeatureController.onPageLoad(NormalMode)
        case false => ChangeChecker.recheckForAnyChanges(answers, List(HaveYouChangedInternalPage, HaveYouChangedSpacePage), 
          routes.AnythingElseController.onPageLoad(NormalMode), routes.NotToldAnyChangesController.show)
      }
    case page if InternalFeature.pageSet.contains(page) => _ => Some(routes.SmallCheckYourAnswersController.onPageLoad(CYAInternal, NormalMode))
    case page if ExternalFeature.pageSet.contains(page) => _ => Some(routes.SmallCheckYourAnswersController.onPageLoad(CYAExternal, NormalMode))
    case AnythingElsePage => _ => Some(routes.SupportingDocumentsController.onPageLoad())
    case _ => _ => None
  }

  private val checkRouteMap: Page => UserAnswers => Option[Call] = {
    case HaveYouChangedSpacePage => answers =>
      answers.get(HaveYouChangedSpacePage) map {
        case true => routes.ChangeToUseOfSpaceController.onPageLoad(CheckMode)
        case false => routes.CheckYourAnswersController.onPageLoad()
      }
    case HaveYouChangedInternalPage => answers =>
      answers.get(HaveYouChangedInternalPage) map {
        case true => routes.WhichInternalFeatureController.onPageLoad(CheckMode)
        case false => routes.HaveYouChangedController.loadExternal(CheckMode)
      }
    case HaveYouChangedExternalPage => answers =>
      answers.get(HaveYouChangedExternalPage) map {
        case true => routes.WhichExternalFeatureController.onPageLoad(CheckMode)
        case false => routes.CheckYourAnswersController.onPageLoad()
      }
    case page if InternalFeature.pageSet.contains(page) => _ => Some(routes.SmallCheckYourAnswersController.onPageLoad(CYAInternal, CheckMode))
    case page if ExternalFeature.pageSet.contains(page) => _ => Some(routes.SmallCheckYourAnswersController.onPageLoad(CYAExternal, CheckMode))
    case page: Page => _ => Some(routes.CheckYourAnswersController.onPageLoad())
  }

  def nextPage(page: Page, mode: Mode, userAnswers: UserAnswers): Call = mode match {
    case NormalMode =>
      normalRoutes(page)(userAnswers).getOrElse(throw new RuntimeException("No selection - should be caught by form validation"))
    case CheckMode =>
      checkRouteMap(page)(userAnswers).getOrElse(throw new RuntimeException("No selection - should be caught by form validation"))
  }
}

