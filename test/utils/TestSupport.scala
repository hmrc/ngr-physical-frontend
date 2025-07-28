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

package utils

import org.scalatest.BeforeAndAfterEach
import org.scalatest.concurrent.{IntegrationPatience, ScalaFutures}
import org.scalatest.matchers.must.Matchers
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.i18n.{Lang, Messages, MessagesApi, MessagesImpl}
import play.api.mvc.{AnyContentAsEmpty, MessagesControllerComponents}
import play.api.test.{FakeRequest, Injecting}
import uk.gov.hmrc.auth.core.retrieve.{Credentials, Name}
import uk.gov.hmrc.auth.core.{AffinityGroup, ConfidenceLevel, Nino}
import uk.gov.hmrc.http.{HeaderCarrier, HeaderNames}

import scala.concurrent.ExecutionContext

trait TestSupport extends PlaySpec
  with GuiceOneAppPerSuite
  with Matchers
  with MockitoSugar
  with Injecting
  with BeforeAndAfterEach
  with ScalaFutures
  with IntegrationPatience {

    implicit lazy val ec: ExecutionContext = inject[ExecutionContext]
    implicit val hc: HeaderCarrier         = HeaderCarrier()
    lazy val mcc: MessagesControllerComponents = inject[MessagesControllerComponents]
    lazy val messagesApi: MessagesApi = inject[MessagesApi]
    implicit lazy val messages: Messages = MessagesImpl(Lang("en"), messagesApi)

    lazy val testCredId: Credentials = Credentials(providerId = "0000000022", providerType = "Government-Gateway")
    lazy val testNino: String = "AA000003D"
    lazy val testConfidenceLevel: ConfidenceLevel = ConfidenceLevel.L250
    lazy val testEmail: String = "user@test.com"
    lazy val testAffinityGroup: AffinityGroup = AffinityGroup.Individual
    lazy val testName: Name = Name(name = Some("testUser"), lastName = Some("testUserLastName"))

    lazy val fakeRequest: FakeRequest[AnyContentAsEmpty.type] =
      FakeRequest("", "").withHeaders(HeaderNames.authorisation -> "Bearer 1")

  }
