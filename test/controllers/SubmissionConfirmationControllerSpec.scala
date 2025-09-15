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

import helpers.ControllerSpecSupport
import play.api.test.Helpers.*
import views.html.SubmissionConfirmationView
import models.*
import pages.*
import org.scalatest.TryValues.convertTryToSuccessOrFailure
import scala.util.Try

class SubmissionConfirmationControllerSpec extends ControllerSpecSupport {
  val view: SubmissionConfirmationView = inject[SubmissionConfirmationView]
  val userAnswers: Option[UserAnswers] = Some(UserAnswers("id").set(DeclarationPage, "1234-1234-1234").success.value)
  val controller: SubmissionConfirmationController = new SubmissionConfirmationController(identify = fakeAuth, getData = fakeData(userAnswers), requireData = fakeRequireData, controllerComponents = mcc, view = view)

  "SubmissionConfirmationController" should {
    "onPageLoad" must {
      "return 200" in {
        val result = controller.onPageLoad(authenticatedFakeRequest)
        status(result) mustBe 200
      }

      "return HTML" in {
        val result = controller.onPageLoad(authenticatedFakeRequest)
        contentType(result) mustBe Some("text/html")
        charset(result) mustBe Some("utf-8")
      }
    }
  }
}