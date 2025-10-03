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
import models.upscan.{Reference, UpscanFileReference, UpscanInitiateResponse}
import org.mockito.ArgumentMatchers
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.when
import play.api.i18n.Messages
import play.api.mvc.Results.Redirect
import play.api.mvc.{MessagesControllerComponents, Result}
import play.api.test.FakeRequest
import play.api.test.Helpers.*
import repositories.SessionRepository
import services.UploadProgressTracker
import uk.gov.hmrc.http.HeaderCarrier
import views.html.UploadDocumentView

import scala.concurrent.Future

class UploadDocumentControllerSpec extends ControllerSpecSupport with TestData {

  lazy val pageView: UploadDocumentView = inject[UploadDocumentView]
  private val fakeRequest = FakeRequest("GET", "/supporting-document-upload")
  val uploadFormData: UploadForm = inject[UploadForm]


  def controller(userAnswers: Option[UserAnswers] = Some(emptyUserAnswers)) = new UploadDocumentController(
    identify = fakeAuth,
    getData = fakeData(None),
    requireData = fakeRequireData(userAnswers),
    upScanConnector = mockUpscanConnector,
    uploadProgressTracker = mockUploadProgressTracker,
    uploadForm = uploadFormData,
    controllerComponents = mcc,
    view = pageView
  )(mockConfig)

  val pageTitle = "Upload"
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
      when(mockUploadProgressTracker.requestUpload(any(), ArgumentMatchers.eq(Reference("ref")))).thenReturn(Future.successful(()))


      def testErrorCase(errorCode: String, expectedMessage: String): Unit = {
        val result: Future[Result] = controller().onPageLoad(Some(errorCode))(fakeRequest)
        status(result) mustBe OK
        val content = contentAsString(result)
        content must include(org.apache.commons.text.StringEscapeUtils.escapeHtml4(Messages(expectedMessage)))
      }

      "Return OK and the correct view with no upload errors" in {

        val result: Future[Result] = controller().onPageLoad(None)(fakeRequest)
        status(result) mustBe OK
        val content = contentAsString(result)
        content must include(pageTitle)
        content must include(contentP)
      }


      "display 'no file selected' error for InvalidArgument" in {
        testErrorCase("InvalidArgument", "uploadDocument.error.required")
      }

      "display 'file too large' error for EntityTooLarge" in {
        testErrorCase("EntityTooLarge", "uploadDocument.error.size")
      }

      "display 'no file selected' error for EntityTooSmall" in {
        testErrorCase("EntityTooSmall", "uploadDocument.error.emptyFile")
      }


      "display 'file must be a PDF or image (PNG & JPG)' error for InvalidFileType reason" in {
        testErrorCase("InvalidFileType", "uploadDocument.error.format")
      }

      "display 'virus detected' error for QUARANTINE" in {
        testErrorCase("QUARANTINE", "uploadDocument.error.virus")
      }

      "display 'problem with upload' error for UNKNOWN reason" in {
        testErrorCase("UNKNOWN123", "uploadDocument.error.upscanUnknownError")
      }

      "throw runtime exception for unrecognisable error code" in {
        val result = intercept[RuntimeException] {
          await(controller().onPageLoad(Some("SOMEOTHERCODE"))(fakeRequest))
        }
        result.getMessage must include("unrecognisable error from upscan 'SOMEOTHERCODE'")
      }

    }

    "method onCancel" must {
      "redirect to correct location" in {
        val result: Future[Result] = controller().onCancel(None)(fakeRequest)
        status(result) mustBe SEE_OTHER
        redirectLocation(result) mustBe Some(routes.UploadedDocumentController.show(None).url)
      }
    }
  }
}
