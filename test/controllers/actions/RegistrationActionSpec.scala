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

package controllers.actions

import actions.{AuthenticatedIdentifierAction, IdentifierAction, RegistrationAction, RegistrationActionImpl}
import base.SpecBase
import com.google.inject.Inject
import config.FrontendAppConfig
import connectors.NGRConnector
import controllers.routes
import models.registration.{CredId, RatepayerRegistration, RatepayerRegistrationValuation}
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.when
import org.scalatestplus.mockito.MockitoSugar.mock
import play.api.inject.bind
import play.api.mvc.{Action, AnyContent, BodyParsers, Results}
import play.api.test.FakeRequest
import play.api.test.Helpers.*
import uk.gov.hmrc.auth.core.*
import uk.gov.hmrc.auth.core.authorise.Predicate
import uk.gov.hmrc.auth.core.retrieve.{Credentials, Retrieval, ~}
import uk.gov.hmrc.http.HeaderCarrier

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ExecutionContext, Future}

class RegistrationActionSpec extends SpecBase {

  class Harness(authAction: RegistrationAction) {
    def onPageLoad(): Action[AnyContent] = authAction { _ => Results.Ok }
  }

  "Auth Action" - {

    "when the user is logged in" - {
      "must redirect to registration login service" in {

        lazy val testBodyParser: BodyParsers.Default = mock[BodyParsers.Default]
        val mockNGRConnector: NGRConnector = mock[NGRConnector]
        val mockAppConfig: FrontendAppConfig = mock[FrontendAppConfig]

        val application = applicationBuilder(userAnswers = None)
          .overrides(
              bind[NGRConnector].toInstance(mockNGRConnector),
              bind[FrontendAppConfig].toInstance(mockAppConfig),
              bind[BodyParsers.Default].toInstance(testBodyParser))
          .build()

        val emptyRatepayer: RatepayerRegistrationValuation = RatepayerRegistrationValuation(CredId("1234"), None)

        when(mockNGRConnector.getRatepayer(any())(any())).thenReturn(Future.successful(Some(emptyRatepayer)))
        when(mockAppConfig.registrationHost).thenReturn("")

        running(application) {

          val action = application.injector.instanceOf[RegistrationActionImpl]
          val controller = new Harness(action)
          val result = controller.onPageLoad()(FakeRequest("", ""))
          status(result) mustBe SEE_OTHER
          redirectLocation(result) mustBe Some("/ngr-login-register-frontend/register")
        }
      }

      "must allow the request to proceed if the user is registered" in {

        lazy val testBodyParser: BodyParsers.Default = mock[BodyParsers.Default]
        val mockNGRConnector: NGRConnector = mock[NGRConnector]
        val mockAppConfig: FrontendAppConfig = mock[FrontendAppConfig]

        val application = applicationBuilder(userAnswers = None)
          .overrides(
            bind[NGRConnector].toInstance(mockNGRConnector),
            bind[FrontendAppConfig].toInstance(mockAppConfig),
            bind[BodyParsers.Default].toInstance(testBodyParser))
          .build()

        val registeredRatepayer: RatepayerRegistrationValuation = RatepayerRegistrationValuation(CredId("1234"), Some(RatepayerRegistration(isRegistered = Some(true))))

        when(mockNGRConnector.getRatepayer(any())(any())).thenReturn(Future.successful(Some(registeredRatepayer)))
        when(mockAppConfig.registrationHost).thenReturn("")

        running(application) {

          val action = application.injector.instanceOf[RegistrationActionImpl]
          val controller = new Harness(action)
          val result = controller.onPageLoad()(FakeRequest("", ""))
          status(result) mustBe OK
        }
      }
    }
  }

}