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

import mocks.MockHttpV2
import models.{AnythingElseData, ChangeToUseOfSpace, ExternalFeature, InternalFeature, NotifyPropertyChangeResponse, PropertyChangesUserAnswers}
import models.propertyLinking.{PropertyLinkingUserAnswers, VMVProperty}
import models.registration.*
import models.registration.ReferenceType.TRN
import org.mockito.Mockito.when
import play.api.test.Helpers.{await, defaultAwaitTimeout}
import uk.gov.hmrc.http.{HttpResponse, NotFoundException}

import java.time.LocalDate
import scala.concurrent.Future

class NGRNotifyConnectorSpec extends MockHttpV2 {
  val ngrConnector: NGRNotifyConnector = new NGRNotifyConnector(mockHttpClientV2, mockConfig)
  val credId: CredId = CredId("1234")

  val userAnswers: PropertyChangesUserAnswers = PropertyChangesUserAnswers(
    credId = credId,
    dateOfChange = LocalDate.of(1999, 1, 4),
    useOfSpace = None,
    internalFeatures = Seq.empty[(InternalFeature, String)],
    externalFeatures = Seq.empty[(ExternalFeature, String)],
    additionalInfo = None,
    declarationRef = None
  )

  override def beforeEach(): Unit = {
    super.beforeEach()
    mockConfig.features.bridgeEndpointEnabled(true)
  }

  "postPropertyChanges" when {
    "Successfully return a response  when provided correct body" in {
      val response: NotifyPropertyChangeResponse = NotifyPropertyChangeResponse(None)
      setupMockHttpV2PostWithHeaderCarrier(
        s"${mockConfig.nextGenerationRatesNotifyUrl}/ngr-notify/physical",
        Seq("Content-Type" -> "application/json")
      )(response)
      val result: Future[NotifyPropertyChangeResponse] = ngrConnector.postPropertyChanges(userAnswers)
      result.futureValue.error mustBe None
    }

    "endpoint returns an error" in {
      val response: NotifyPropertyChangeResponse = NotifyPropertyChangeResponse(Some("an error happened"))
      mockConfig.features.bridgeEndpointEnabled(true)
      setupMockHttpV2PostWithHeaderCarrier(
        s"${mockConfig.nextGenerationRatesNotifyUrl}/ngr-notify/physical",
        Seq("Content-Type" -> "application/json")
      )(response)

      val result: Future[NotifyPropertyChangeResponse] = ngrConnector.postPropertyChanges(userAnswers)
      result.futureValue mustBe response

    }
  }
}


