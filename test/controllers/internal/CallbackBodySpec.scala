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

import controllers.internal.CallbackBody.{given_Reads_ErrorDetails, given_Reads_UploadDetails}
import models.upscan.Reference
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
import play.api.libs.json.{JsPath, JsSuccess, Json, JsonValidationError}

import java.net.URL
import java.time.Instant


class CallbackBodySpec extends AnyWordSpec with Matchers {

  "CallbackBody JSON reader" should {
    "be able to deserialize successful body" in {
      val body =
        """
          |{
          |    "reference" : "11370e18-6e24-453e-b45a-76d3e32ea33d",
          |    "fileStatus" : "READY",
          |    "downloadUrl" : "https://bucketName.s3.eu-west-2.amazonaws.com?1235676",
          |    "uploadDetails": {
          |        "uploadTimestamp": "2018-04-24T09:30:00Z",
          |        "checksum": "396f101dd52e8b2ace0dcf5ed09b1d1f030e608938510ce46e7a5c7a4e775100",
          |        "fileName": "test.pdf",
          |        "fileMimeType": "application/pdf",
          |        "size": 45678
          |    }
          |}
          |
        """.stripMargin

      Json.parse(body).validate[CallbackBody] mustBe JsSuccess(
        ReadyCallbackBody(
          reference = Reference("11370e18-6e24-453e-b45a-76d3e32ea33d"),
          downloadUrl = URL("https://bucketName.s3.eu-west-2.amazonaws.com?1235676"),
          uploadDetails = UploadDetails(
            uploadTimestamp = Instant.parse("2018-04-24T09:30:00Z"),
            checksum = "396f101dd52e8b2ace0dcf5ed09b1d1f030e608938510ce46e7a5c7a4e775100",
            fileMimeType = "application/pdf",
            fileName = "test.pdf",
            size = 45678L
          )
        )
      )
    }

    "deserialize FailedCallbackBody when fileStatus is FAILED" in {
      val json = Json.obj(
        "fileStatus" -> "FAILED",
        "reference" -> "ref456",
        "failureDetails" -> Json.obj(
          "failureReason" -> "VirusDetected",
          "message" -> "File contains a virus"
        )
      )

      val result = json.validate[CallbackBody]
      result.isSuccess mustBe true
      result.get mustBe FailedCallbackBody(
        reference = Reference("ref456"),
        failureDetails = ErrorDetails(
          failureReason = "VirusDetected",
          message = "File contains a virus"
        )
      )
    }

    "return JsError for invalid type discriminator" in {
      val json = Json.obj(
        "fileStatus" -> "UNKNOWN",
        "reference" -> "ref789"
      )

      val result = json.validate[CallbackBody]
      result.isError mustBe true
      result.asEither.left.toOption.get.head._2.head.message must include("Invalid type discriminator: \"UNKNOWN\"")
    }

    "return JsError for missing type discriminator" in {
      val json = Json.obj(
        "reference" -> "ref000"
      )

      val result = json.validate[CallbackBody]
      result.isError mustBe true
      result.asEither.left.toOption.get.head._2.head.message must include("Missing type discriminator")
    }

    "deserialize UploadDetails" in {

      val json = Json.obj(
        "uploadTimestamp" -> "2018-04-24T09:30:00Z",
        "checksum" -> "396f101dd52e8b2ace0dcf5ed09b1d1f030e608938510ce46e7a5c7a4e775100",
        "fileName" -> "test.pdf",
        "fileMimeType" -> "application/pdf",
        "size" -> 45678
      )

      val result = json.validate[UploadDetails]
      result.isSuccess mustBe true
      result.get mustBe UploadDetails(
        uploadTimestamp = Instant.parse("2018-04-24T09:30:00Z"),
        checksum = "396f101dd52e8b2ace0dcf5ed09b1d1f030e608938510ce46e7a5c7a4e775100",
        fileMimeType = "application/pdf",
        fileName = "test.pdf",
        size = 45678L
      )
    }

    "de-serialise ErrorDetails" in {
      val json = Json.obj(
        "failureReason" -> "VirusDetected",
        "message" -> "File contains a virus"
      )

      val result = json.validate[ErrorDetails]
      result.isSuccess mustBe true
      result.get mustBe ErrorDetails(
        failureReason = "VirusDetected",
        message = "File contains a virus"
      )
    }
  }

}
