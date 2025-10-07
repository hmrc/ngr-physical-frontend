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

package services

import controllers.internal.{CallbackBody, ErrorDetails, FailedCallbackBody, ReadyCallbackBody}
import helpers.{TestData, TestSupport}
import models.upscan.{Reference, UploadId, UploadStatus}
import models.upscan.{UploadDetails => UploadRecord}
import controllers.internal.{UploadDetails => CallbackUploadDetails}
import org.bson.types.ObjectId
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.when
import org.mongodb.scala.bson.BsonDocument
import org.mongodb.scala.bson.collection.immutable.Document
import play.api.test.Helpers.await
import repositories.FileUploadRepo
import uk.gov.hmrc.http.{BadRequestException, StringContextOps}
import uk.gov.hmrc.mongo.test.DefaultPlayMongoRepositorySupport

import java.net.URL
import java.time.{Instant, LocalDate, LocalDateTime, ZoneId, ZoneOffset}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import play.api.test.Helpers.defaultAwaitTimeout

class UpscanCallbackDispatcherSpec extends TestSupport with TestData with DefaultPlayMongoRepositorySupport[UploadRecord]{

  override val repository: FileUploadRepo = FileUploadRepo(mongoComponent)
  val progressTracker = UploadProgressTracker(repository)
  val callbackDispatcher = UpscanCallbackDispatcher(progressTracker)

  override def beforeEach(): Unit = {
    super.beforeEach()
    repository.deleteAll().futureValue
  }

  "UpscanCallbackDispatcher" should  {
    val testReference = Reference("12345")
    val id = UploadId("12345")
    val downloadUrl = url"https://www.some-site.com/a-file.txt"

    "handleCallback update session storage when ReadyCallbackBody returned " in {

      val callbackBody = ReadyCallbackBody(
        reference = testReference,
        downloadUrl = downloadUrl,
        uploadDetails = CallbackUploadDetails(
          uploadTimestamp = LocalDateTime.now().atZone(ZoneId.of("UTC")).toInstant,
          checksum = "asdadsdeadead",
          fileMimeType = "image/png",
          fileName = "example1",
          6400000L
        )
      )

      await(
        repository.insert(
          UploadRecord(
            id       = ObjectId.get(),
            uploadId = id,
            reference = testReference,
            status   = UploadStatus.InProgress
          )
        )
      )


      await(repository.updateStatus(testReference, UploadStatus.InProgress))

      await(callbackDispatcher.handleCallback(callbackBody))

    }

    "handleCallback update session storage when FailedCallbackBody returned " in {

      val callbackBody = FailedCallbackBody(
        reference = testReference,
        failureDetails = ErrorDetails("", "")
        )
      
      await(
      repository.insert(
        UploadRecord(
          id = ObjectId.get(),
          uploadId = id,
          reference = testReference,
          status = UploadStatus.InProgress
        )
      )
      )

      await(repository.updateStatus(testReference, UploadStatus.Failed))

      await(callbackDispatcher.handleCallback(callbackBody))

    }

    "handleCallback throw error due to incorrect file type" in {

      val callbackBody = ReadyCallbackBody(
        reference = testReference,
        downloadUrl = downloadUrl,
        uploadDetails = CallbackUploadDetails(
          uploadTimestamp = LocalDateTime.now().atZone(ZoneId.of("UTC")).toInstant,
          checksum = "asdadsdeadead",
          fileMimeType = "image/notpng",
          fileName = "example1",
          size = 6400000L
        )
      )

      intercept[BadRequestException] {
        await(callbackDispatcher.handleCallback(callbackBody))
      }
    }
    
  }

}
