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

package models

import models.registration.CredId
import play.api.libs.json.{Format, Json, OFormat}

import java.time.LocalDate

//TODO add upload to model NGR-419
case class PropertyChangesUserAnswers(credId: CredId,
                                      dateOfChange: LocalDate,
                                      useOfSpace: Option[ChangeToUseOfSpace] = None,
                                      internalFeatures: Option[InternalFeature] = None,
                                      externalFeatures: Option[ExternalFeature] = None,
                                      additionalInfo: Option[AnythingElseData] = None
                                     )

object PropertyChangesUserAnswers {
  implicit val format: OFormat[PropertyChangesUserAnswers] = Json.format
}
