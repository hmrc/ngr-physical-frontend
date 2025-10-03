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

import actions.{AuthenticatedIdentifierAction, IdentifierAction}
import base.SpecBase
import com.google.inject.Inject
import config.FrontendAppConfig
import controllers.routes
import play.api.mvc.{Action, AnyContent, BodyParsers, Results}
import play.api.test.FakeRequest
import play.api.test.Helpers.*
import uk.gov.hmrc.auth.core.*
import uk.gov.hmrc.auth.core.authorise.Predicate
import uk.gov.hmrc.auth.core.retrieve.Retrieval
import uk.gov.hmrc.http.HeaderCarrier

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ExecutionContext, Future}

class AuthActionSpec extends SpecBase {

  class Harness(authAction: IdentifierAction) {
    def onPageLoad(): Action[AnyContent] = authAction { _ => Results.Ok }
  }

  "Auth Action" - {
    "Auth Action" - {

      "when the user hasn't logged in" - {

        "must fail with MissingBearerToken" in {

          val application = applicationBuilder(userAnswers = None).build()

          running(application) {
            val bodyParsers = application.injector.instanceOf[BodyParsers.Default]

            val authAction = new AuthenticatedIdentifierAction(new FakeFailingAuthConnector(new MissingBearerToken), bodyParsers)
            val controller = new Harness(authAction)

            intercept[MissingBearerToken] {
              await(controller.onPageLoad()(FakeRequest()))
            }
          }
        }
      }

      "the user's session has expired" - {

        "must fail with BearerTokenExpired" in {

          val application = applicationBuilder(userAnswers = None).build()

          running(application) {
            val bodyParsers = application.injector.instanceOf[BodyParsers.Default]

            val authAction = new AuthenticatedIdentifierAction(new FakeFailingAuthConnector(new BearerTokenExpired), bodyParsers)
            val controller = new Harness(authAction)

            intercept[BearerTokenExpired] {
              await(controller.onPageLoad()(FakeRequest()))
            }
          }
        }
      }

      "the user doesn't have sufficient enrolments" - {

        "must fail with InsufficientEnrolments" in {

          val application = applicationBuilder(userAnswers = None).build()

          running(application) {
            val bodyParsers = application.injector.instanceOf[BodyParsers.Default]

            val authAction = new AuthenticatedIdentifierAction(new FakeFailingAuthConnector(new InsufficientEnrolments), bodyParsers)
            val controller = new Harness(authAction)

            intercept[InsufficientEnrolments] {
              await(controller.onPageLoad()(FakeRequest()))
            }
          }
        }
      }

      "the user doesn't have sufficient confidence level" - {

        "must fail with InsufficientConfidenceLevel" in {

          val application = applicationBuilder(userAnswers = None).build()

          running(application) {
            val bodyParsers = application.injector.instanceOf[BodyParsers.Default]

            val authAction = new AuthenticatedIdentifierAction(new FakeFailingAuthConnector(new InsufficientConfidenceLevel), bodyParsers)
            val controller = new Harness(authAction)

            intercept[InsufficientConfidenceLevel] {
              await(controller.onPageLoad()(FakeRequest()))
            }
          }
        }
      }

      "the user used an unaccepted auth provider" - {

        "must fail with UnsupportedAuthProvider" in {

          val application = applicationBuilder(userAnswers = None).build()

          running(application) {
            val bodyParsers = application.injector.instanceOf[BodyParsers.Default]

            val authAction = new AuthenticatedIdentifierAction(new FakeFailingAuthConnector(new UnsupportedAuthProvider), bodyParsers)
            val controller = new Harness(authAction)

            intercept[UnsupportedAuthProvider] {
              await(controller.onPageLoad()(FakeRequest()))
            }
          }
        }
      }

      "the user has an unsupported affinity group" - {

        "must fail with UnsupportedAffinityGroup" in {

          val application = applicationBuilder(userAnswers = None).build()

          running(application) {
            val bodyParsers = application.injector.instanceOf[BodyParsers.Default]

            val authAction = new AuthenticatedIdentifierAction(new FakeFailingAuthConnector(new UnsupportedAffinityGroup), bodyParsers)
            val controller = new Harness(authAction)

            intercept[UnsupportedAffinityGroup] {
              await(controller.onPageLoad()(FakeRequest()))
            }
          }
        }
      }

      "the user has an unsupported credential role" - {

        "must fail with UnsupportedCredentialRole" in {

          val application = applicationBuilder(userAnswers = None).build()

          running(application) {
            val bodyParsers = application.injector.instanceOf[BodyParsers.Default]

            val authAction = new AuthenticatedIdentifierAction(new FakeFailingAuthConnector(new UnsupportedCredentialRole), bodyParsers)
            val controller = new Harness(authAction)

            intercept[UnsupportedCredentialRole] {
              await(controller.onPageLoad()(FakeRequest()))
            }
          }
        }
      }

    }
  }

  class FakeFailingAuthConnector @Inject()(exceptionToReturn: Throwable) extends AuthConnector {
    val serviceUrl: String = ""

    override def authorise[A](predicate: Predicate, retrieval: Retrieval[A])(implicit hc: HeaderCarrier, ec: ExecutionContext): Future[A] =
      Future.failed(exceptionToReturn)
  }
}