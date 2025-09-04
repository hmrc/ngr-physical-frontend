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

import javax.inject.{Inject, Singleton}

@Singleton
class Navigator @Inject()() {

  private val normalRoutes: Page => UserAnswers => Call = {
    case WhenCompleteChangePage => _ => routes.HaveYouChangedController.loadSpace(NormalMode)
    case HaveYouChangedSpacePage => answers =>
      answers.get(HaveYouChangedSpacePage) match {
        case Some(true) => routes.ChangeToUseOfSpaceController.onPageLoad(NormalMode)
        case Some(false) => routes.HaveYouChangedController.loadInternal(NormalMode)
        case _ => throw new RuntimeException("No selection - should be caught by form validation")
      }
    case ChangeToUseOfSpacePage => _ => routes.HaveYouChangedController.loadInternal(NormalMode)
    case HaveYouChangedInternalPage => answers =>
      answers.get(HaveYouChangedInternalPage) match {
        case Some(true) => routes.WhichInternalFeatureController.onPageLoad(NormalMode)
        case Some(false) => routes.HaveYouChangedController.loadExternal(NormalMode)
        case _ => throw new RuntimeException("No selection - should be caught by form validation")
      }
    case HaveYouChangedExternalPage => answers =>
      answers.get(HaveYouChangedExternalPage) match {
        case Some(true) => routes.WhichExternalFeatureController.onPageLoad
        case Some(false) => routes.AnythingElseController.onPageLoad(NormalMode)
        case _ => throw new RuntimeException("No selection - should be caught by form validation")
      }
    case WhichInternalFeaturePage => answers =>
      answers.get(WhichInternalFeaturePage) match {
        case Some(feature) =>
          routes.WhichInternalFeatureController.onPageLoad(NormalMode)
        case None => throw new RuntimeException("No selection - should be caught by form validation")
      }
    case WhichExternalFeaturePage => answers =>
      answers.get(WhichExternalFeaturePage) match {
        case Some(feature) =>
          routes.WhichExternalFeatureController.onPageLoad(NormalMode)
        case None => throw new RuntimeException("No selection - should be caught by form validation")
      }
    case page if InternalFeature.pageSet.contains(page) => _ => routes.SmallCheckYourAnswersController.onPageLoad(CYAInternal)
    case page if ExternalFeature.pageSet.contains(page) => _ => routes.SmallCheckYourAnswersController.onPageLoad(CYAExternal)
    case AnythingElsePage => _ => routes.SupportingDocumentsController.onPageLoad()
    case _ => _ => routes.IndexController.onPageLoad()
  }

  private val checkRouteMap: Page => UserAnswers => Option[Call] = {
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
    case _ => _ => Some(routes.CheckYourAnswersController.onPageLoad())
  }

  def nextPage(page: Page, mode: Mode, userAnswers: UserAnswers): Call = mode match {
    case NormalMode =>
      normalRoutes(page)(userAnswers)
    case CheckMode =>
      checkRouteMap(page)(userAnswers).getOrElse(throw new RuntimeException("No selection - should be caught by form validation"))
  }
}
