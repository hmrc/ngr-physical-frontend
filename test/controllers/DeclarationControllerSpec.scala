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

package controllers

import base.SpecBase
import config.FrontendAppConfig
import helpers.{ControllerSpecSupport, TestData}
import models.ExternalFeature.LoadingBays
import models.InternalFeature.AirConditioning
import models.NavBarPageContents.createDefaultNavBar
import models.{AnythingElseData, ChangeToUseOfSpace, HowMuchOfProperty, UseOfSpaces, UserAnswers, WhatHappenedTo}
import models.registration.CredId
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.when
import org.scalatest.TryValues
import pages.{AnythingElsePage, ChangeToUseOfSpacePage, DeclarationPage, HaveYouChangedSpacePage, SecurityCamerasChangePage, WhenCompleteChangePage}
import play.api.mvc.Result
import play.api.test.FakeRequest
import play.api.test.Helpers.*
import views.html.{AnswerErrorTemplate, DeclarationView}

import java.time.LocalDate
import scala.concurrent.Future

class DeclarationControllerSpec extends ControllerSpecSupport with TryValues {

  lazy val view: DeclarationView = inject[DeclarationView]
  lazy val errorTemplateView: AnswerErrorTemplate = inject[AnswerErrorTemplate]

  def minUserAnswers: UserAnswers =
    emptyUserAnswers
      .set(DeclarationPage(assessmentId), "some-reference").success.value
      .set(AnythingElsePage(assessmentId), AnythingElseData(false, None)).success.value
      .set(
        ChangeToUseOfSpacePage(assessmentId),
        ChangeToUseOfSpace(
          selectUseOfSpace = Set.empty[UseOfSpaces],
          hasPlanningPermission = false,
          permissionReference = None
        )).success.value
      .set(HaveYouChangedSpacePage(assessmentId), false).success.value
      .set(HowMuchOfProperty.page(AirConditioning, assessmentId), HowMuchOfProperty.values.head).success.value
      .set(SecurityCamerasChangePage(assessmentId), 0).success.value
      .set(WhatHappenedTo.page(LoadingBays, assessmentId), WhatHappenedTo.Added).success.value
      .set(WhenCompleteChangePage(assessmentId), LocalDate.of(2025, 8, 12)).success.value

  def controllerWithUserAnswers(userAnswers: Option[UserAnswers]) = DeclarationController(
    mcc,
    view,
    fakeAuth,
    fakeData(userAnswers),
    fakeRequireData(userAnswers),
    mockSessionRepository,
    mockNGRNotifyConnector,
    errorTemplateView
  )

  "Declaration Controller" must {

    ".show" should {
      "correctly render page" in {
        val result = controllerWithUserAnswers(Some(minUserAnswers)).show(assessmentId)(authenticatedFakeRequest)
        status(result) mustBe 200
        contentType(result) mustBe Some("text/html")
        charset(result) mustBe Some("utf-8")
      }

    }

    ".next" should {
      "redirect when accepted and DeclarationPage data is empty" in {
        when(mockSessionRepository.set(any())).thenReturn(Future.successful(true))
        val result = controllerWithUserAnswers(Some(emptyUserAnswers)).next(assessmentId)(authenticatedFakeRequest)

        status(result) mustBe BAD_REQUEST
      }

      "redirect when accepted and DeclarationPage data is present without generated Reference" in {
        when(mockSessionRepository.set(any())).thenReturn(Future.successful(true))
        when(mockNGRNotifyConnector.postPropertyChanges(any(), any())(any())).thenReturn(Future.successful(ACCEPTED))
        val result = controllerWithUserAnswers(Some(minUserAnswers.remove(DeclarationPage(assessmentId)).success.value)).next(assessmentId)(authenticatedFakeRequest)

        status(result) mustBe SEE_OTHER
        redirectLocation(result).value mustBe routes.SubmissionConfirmationController.onPageLoad(assessmentId).url
      }

      "redirect when accepted and DeclarationPage data is present without mandatory field when change completed" in {
        when(mockSessionRepository.set(any())).thenReturn(Future.successful(true))
        when(mockNGRNotifyConnector.postPropertyChanges(any(), any())(any())).thenReturn(Future.successful(ACCEPTED))
        val result = controllerWithUserAnswers(Some(minUserAnswers.remove(WhenCompleteChangePage(assessmentId)).success.value)).next(assessmentId)(authenticatedFakeRequest)

        status(result) mustBe BAD_REQUEST
      }

      "redirect when accepted and DeclarationPage data is present with generated Reference" in {
        when(mockSessionRepository.set(any())).thenReturn(Future.successful(true))
        when(mockNGRNotifyConnector.postPropertyChanges(any(), any())(any())).thenReturn(Future.successful(ACCEPTED))
        val result = controllerWithUserAnswers(Some(minUserAnswers)).next(assessmentId)(authenticatedFakeRequest)

        status(result) mustBe SEE_OTHER
        redirectLocation(result).value mustBe routes.SubmissionConfirmationController.onPageLoad(assessmentId).url
      }

    }
  }
  
}
