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
import forms.RemoveFileFormProvider
import helpers.ControllerSpecSupport
import models.upscan.{UploadId, UploadStatus}
import models.upscan.UploadStatus.UploadedSuccessfully
import models.{NormalMode, UserAnswers}
import navigation.{FakeNavigator, Navigator}
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.when
import org.scalatestplus.mockito.MockitoSugar
import pages.RemoveFilePage
import play.api.data.Form
import play.api.inject.bind
import play.api.mvc.Call
import play.api.test.FakeRequest
import play.api.test.Helpers.*
import repositories.SessionRepository
import services.UploadProgressTracker
import uk.gov.hmrc.http.StringContextOps
import views.html.RemoveFileView

import java.net.URL
import scala.concurrent.Future

class RemoveFileControllerSpec extends ControllerSpecSupport {

  val formProvider = new RemoveFileFormProvider()
  val uploadProgressTracker: UploadProgressTracker = mock[UploadProgressTracker]
  val form: Form[Boolean] = formProvider()
  val uploadId: String = "123"
  val address: String = "123 street lane"
  val fileName: String = "file.pdf"

  val view: RemoveFileView = inject[RemoveFileView]
  val controller: RemoveFileController = new RemoveFileController(messagesApi, fakeAuth, mockSessionRepository, fakeData(Some(emptyUserAnswers)), fakeRequireData(Some(emptyUserAnswers)), formProvider, uploadProgressTracker, mcc, view)

  when(uploadProgressTracker.getUploadResult(any()))
    .thenReturn(Future.successful(Some(UploadStatus.UploadedSuccessfully("file.pdf", "application/pdf", url"http:localhost:8080", Some(1234)))))

  "RemoveFile Controller" must {
    "onPageLoad" must {
      "return 200" in {
        val result = controller.onPageLoad(uploadId, assessmentId)(authenticatedFakeRequest)
        status(result) mustBe OK
        contentType(result) mustBe Some("text/html")
        charset(result) mustBe Some("utf-8")
      }
    }

  }
}
