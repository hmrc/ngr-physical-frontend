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

package controllers.internal

import models.upscan.Reference
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.*
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers.mustBe
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.mockito.MockitoSugar
import play.api.libs.json.Json
import play.api.mvc.*
import play.api.test.Helpers.*
import play.api.test.{FakeRequest, Helpers}
import services.UpscanCallbackDispatcher

import java.net.URL
import java.time.Instant
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class UploadCallbackControllerSpec
  extends AnyWordSpec
    with Matchers
    with ScalaFutures
    with MockitoSugar {

  "UploadCallbackController.callback" must {
    "return Ok for valid READY callback" in {
      val dispatcher = mock[UpscanCallbackDispatcher]

      when(dispatcher.handleCallback(any())).thenReturn(scala.concurrent.Future.successful(()))

      val controller = new UploadCallbackController(
        dispatcher,
        Helpers.stubMessagesControllerComponents()
      )

      val json = Json.obj(
        "fileStatus" -> "READY",
        "reference" -> "ref123",
        "downloadUrl" -> "http://example.com/file",
        "uploadDetails" -> Json.obj(
          "uploadTimestamp" -> Instant.now.toString,
          "checksum" -> "abc123",
          "fileMimeType" -> "application/pdf",
          "fileName" -> "file.pdf",
          "size" -> 12345L
        )
      )

      val request = FakeRequest("POST", "/").withBody(json)
      val result = controller.callback.apply(request)
      status(result) mustBe OK
    }

    "return Ok for valid FAILED callback" in {

      val dispatcher = mock[UpscanCallbackDispatcher]
      when(dispatcher.handleCallback(any[CallbackBody]))
        .thenReturn(Future.successful(()))

      val controller = new UploadCallbackController(
        dispatcher,
        Helpers.stubMessagesControllerComponents()
      )

      val json = Json.obj(
        "fileStatus" -> "FAILED",
        "reference" -> "ref456",
        "failureDetails" -> Json.obj(
          "failureReason" -> "QUARANTINE",
          "message" -> "Virus detected"
        )
      )

      val request = FakeRequest("POST", "/").withBody(json)
      val result = controller.callback.apply(request)
      status(result) mustBe OK
    }
  }
}
