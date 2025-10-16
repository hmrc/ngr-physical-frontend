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

package models.upscan

import models.registration.CredId
import models.upscan.UploadStatus.{Failed, InProgress, UploadedSuccessfully}
import org.bson.types.ObjectId
import play.api.libs.json.*
import play.api.mvc.QueryStringBindable

import java.net.URL
import java.util.UUID

sealed trait UploadStatus
object UploadStatus:
  case object InProgress extends UploadStatus
  case object Failed     extends UploadStatus
  case class UploadedSuccessfully(
                                   name       : String,
                                   mimeType   : String,
                                   downloadUrl: URL,
                                   size       : Option[Long]
                                 ) extends UploadStatus

case class UploadDetails(
                          id       : ObjectId,
                          uploadId : UploadId,
                          reference: Reference,
                          status   : UploadStatus
                        )

case class UploadId(value: String) extends AnyVal

object UploadId {
  def generate(): UploadId =
    UploadId(java.util.UUID.randomUUID().toString)

  implicit val uploadIdFormat: Format[UploadId] = new Format[UploadId] {
    override def reads(json: JsValue): JsResult[UploadId] =
      json.validate[String].map(UploadId(_))

    override def writes(id: UploadId): JsValue =
      JsString(id.value)
  }
}


implicit def queryBinder(implicit stringBinder: QueryStringBindable[String]): QueryStringBindable[UploadId] =
  stringBinder.transform(UploadId(_),_.value)