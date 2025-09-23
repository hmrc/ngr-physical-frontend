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
import connectors.UpscanConnector
import forms.UploadForm
import helpers.{ControllerSpecSupport, TestData}
import models.UserAnswers
import models.registration.CredId
import models.upscan.UploadStatus.UploadedSuccessfully
import models.upscan.{Reference, UploadId, UpscanFileReference, UpscanInitiateResponse}
import org.mockito.ArgumentMatchers
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.when
import play.api.i18n.Messages
import play.api.mvc.{MessagesControllerComponents, Result}
import play.api.test.FakeRequest
import play.api.test.Helpers.*
import repositories.SessionRepository
import services.UploadProgressTracker
import uk.gov.hmrc.http.{HeaderCarrier, StringContextOps}
import views.html.{UploadDocumentView, UploadedDocumentView}

import scala.concurrent.Future

class UploadedDocumentControllerSpec extends ControllerSpecSupport with TestData {

  lazy val pageView: UploadedDocumentView = inject[UploadedDocumentView]
  private val fakeRequest = FakeRequest("GET", "/supporting-document-uploaded")


  def controller(userAnswers: Option[UserAnswers]) = new UploadedDocumentController(
    identify = fakeAuth,
    getData = fakeData(None),
    requireData = fakeRequireData(userAnswers),
    uploadProgressTracker = mockUploadProgressTracker,
    controllerComponents = mcc,
    view = pageView,
    sessionRepository = mockSessionRepository
  )(mockConfig)

  val pageTitle = "Uploaded"
  val contentP = "Files must be PDF, JPG or PNG and must be smaller than 25MB."

  when(mockNGRConnector.getLinkedProperty(any[CredId])(any())).thenReturn(Future.successful(Some(property)))

  "Upload documents Controller" must {
    "method show" must {

      when(mockUpscanConnector.initiate(any(), any())(any[HeaderCarrier]))
        .thenReturn(Future.successful(
          UpscanInitiateResponse(
            UpscanFileReference("ref"),
            "postTarget",
            Map("key" -> "value")
          )
        ))

      when(mockSessionRepository.set(any())).thenReturn(Future.successful(true))
      when(mockUploadProgressTracker.getUploadResult(any())).thenReturn(Future.successful(Some(UploadedSuccessfully("filename.png", ".png", url"http://example.com/dummyLink", Some(120L)))))
      when(mockUploadProgressTracker.requestUpload(any(), ArgumentMatchers.eq(Reference("ref")))).thenReturn(Future.successful(()))

      "Return OK and the correct view" in {

        val result: Future[Result] = controller(Some(emptyUserAnswers)).show(UploadId("12235"))(fakeRequest)
        status(result) mustBe OK
        val content = contentAsString(result)
        content must include(pageTitle)
        content must include(contentP)
      }

    }

    "method onSubmit" must {
      "Return OK and send user to correct location" in {
        val result: Future[Result] = controller(Some(emptyUserAnswers)).onSubmit(UploadId("12235"), false)(fakeRequest)
        status(result) mustBe SEE_OTHER
        redirectLocation(result) mustBe Some(routes.CheckYourAnswersController.onPageLoad().url)
      }

    }


  }
}
