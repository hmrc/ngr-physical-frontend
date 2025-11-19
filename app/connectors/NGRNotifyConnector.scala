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

package connectors

import config.{AppConfig, FrontendAppConfig}
import models.{AssessmentId, NotifyPropertyChangeResponse, PropertyChangesUserAnswers}
import models.registration.{CredId, RatepayerRegistrationValuation}
import play.api.Logging
import play.api.libs.json.Json
import play.api.libs.ws.JsonBodyWritables.writeableOf_JsValue
import uk.gov.hmrc.http.HttpReads.Implicits.*
import uk.gov.hmrc.http.client.HttpClientV2
import uk.gov.hmrc.http.{HeaderCarrier, HttpReads, NotFoundException, StringContextOps}

import java.net.URL
import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}
import play.mvc.Http.HeaderNames


@Singleton
class NGRNotifyConnector @Inject()(http: HttpClientV2,
                                   appConfig: AppConfig,
                            )(implicit ec: ExecutionContext) extends Logging {
  private val headers = Map(
    HeaderNames.CONTENT_TYPE -> "application/json"
  )

 // private def url(path: String, assessmentId: AssessmentId): URL = url"${appConfig.nextGenerationRatesNotifyUrl}/ngr-notify/$path/$assessmentId"
  private def url(path: String, assessmentId: AssessmentId): URL = url"${appConfig.nextGenerationRatesNotifyUrl}/ngr-notify/$path"

  def postPropertyChanges(userAnswers: PropertyChangesUserAnswers, assessmentId: AssessmentId)(implicit hc: HeaderCarrier): Future[NotifyPropertyChangeResponse] = {
    if (appConfig.features.bridgeEndpointEnabled()) {
      http.post(url("physical", assessmentId))
        .withBody(Json.toJson(userAnswers))
        .setHeader(headers.toSeq *)
        .execute[NotifyPropertyChangeResponse]
    } else {
      Future.successful(NotifyPropertyChangeResponse(None))
    }
  }

}
