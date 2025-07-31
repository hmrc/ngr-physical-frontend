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
import forms.ChangeToUseOfSpaceFormProvider
import models.UseOfSpaces.Rearrangedtheuseofspace
import models.{ChangeToUseOfSpace, NormalMode, UserAnswers}
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.when
import org.scalatestplus.mockito.MockitoSugar
import pages.ChangeToUseOfSpacePage
import play.api.data.Form
import play.api.test.FakeRequest
import play.api.test.Helpers.*
import views.html.ChangeToUseOfSpaceView
import scala.concurrent.{ExecutionContext, Future}

class ChangeToUseOfSpaceControllerSpec extends SpecBase with MockitoSugar {
  
  val formProvider = new ChangeToUseOfSpaceFormProvider()
  val form: Form[ChangeToUseOfSpace] = formProvider()
  lazy val changeToUseOfSpaceRoute: String = routes.ChangeToUseOfSpaceController.onPageLoad(NormalMode).url
  val changeToUseOfSpace: ChangeToUseOfSpace = ChangeToUseOfSpace(Set(Rearrangedtheuseofspace), true, Some("1234555"))
  val userAnswers: UserAnswers = emptyUserAnswers.set(ChangeToUseOfSpacePage, changeToUseOfSpace).success.value

  "ChangeToUseOfSpace Controller" - {

    "must return OK and the correct view for a GET" in {

      val application = applicationBuilder(userAnswers = Some(emptyUserAnswers)).build()

      running(application) {
        val request = FakeRequest(GET, changeToUseOfSpaceRoute)

        val view = application.injector.instanceOf[ChangeToUseOfSpaceView]

        val result = route(application, request).value

        status(result) mustEqual OK
        contentAsString(result) mustEqual view(form, NormalMode)(request, messages(application)).toString
      }
    }

    "must populate the view correctly on a GET when the question has previously been answered" in {

      val application = applicationBuilder(userAnswers = Some(userAnswers)).build()

      running(application) {
        val request = FakeRequest(GET, changeToUseOfSpaceRoute)

        val view = application.injector.instanceOf[ChangeToUseOfSpaceView]

        val result = route(application, request).value

        status(result) mustEqual OK

        contentAsString(result) mustEqual view(form.fill(ChangeToUseOfSpace(Set(Rearrangedtheuseofspace),true, Some("1234555"))), NormalMode)(request, messages(application)).toString
      }
    }


    "must redirect when planning permission is true and reference is provided" in {

      when(mockSessionRepository.set(any())) thenReturn Future.successful(true)

      val application = applicationBuilder(userAnswers = None).build()

      running(application) {
        val request = FakeRequest(POST, changeToUseOfSpaceRoute)
          .withFormUrlEncodedBody(
            "selectUseOfSpace[0]" -> "rearrangedTheUseOfSpace",
            "hasPlanningPermission" -> "false"
          )

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual onwardRoute.url
      }
    }

    "must return a Bad Request and errors when invalid data is submitted" in {

      val application = applicationBuilder(userAnswers = Some(emptyUserAnswers)).build()

      running(application) {
        val request =
          FakeRequest(POST, changeToUseOfSpaceRoute)
            .withFormUrlEncodedBody(("value", "invalid value"))

        val boundForm = form.bind(Map("value" -> "invalid value"))

        val view = application.injector.instanceOf[ChangeToUseOfSpaceView]

        val result = route(application, request).value

        status(result) mustEqual BAD_REQUEST
        contentAsString(result) mustEqual view(boundForm, NormalMode)(request, messages(application)).toString
      }
    }


//TODO uncomment when first page is implemented [Date field]

  /*  "must redirect to Journey Recovery for a GET if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      running(application) {
        val request = FakeRequest(GET, changeToUseOfSpaceRoute)

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual routes.JourneyRecoveryController.onPageLoad().url
      }
    }

    "must redirect to Journey Recovery for a POST if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      running(application) {
        val request =
          FakeRequest(POST, changeToUseOfSpaceRoute)
            .withFormUrlEncodedBody(("selectUseOfSpace", "value 1"), ("hasPlanningPermission", "value 2"))

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual routes.JourneyRecoveryController.onPageLoad().url
      }
    }*/
  }
}
