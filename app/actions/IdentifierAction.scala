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

package actions

import com.google.inject.Inject
import config.FrontendAppConfig
import connectors.NGRConnector
import controllers.routes
import models.registration.CredId
import models.requests.IdentifierRequest
import play.api.mvc.*
import play.api.mvc.Results.*
import uk.gov.hmrc.auth.core.*
import uk.gov.hmrc.auth.core.ConfidenceLevel.L250
import uk.gov.hmrc.auth.core.retrieve.v2.Retrievals
import uk.gov.hmrc.auth.core.retrieve.{Credentials, Retrieval, ~}
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.http.HeaderCarrierConverter

import scala.concurrent.{ExecutionContext, Future}

trait IdentifierAction extends ActionBuilder[IdentifierRequest, AnyContent] with ActionFunction[Request, IdentifierRequest]

class AuthenticatedIdentifierAction @Inject()(
                                               override val authConnector: AuthConnector,
                                               ngrConnector: NGRConnector,
                                               appConfig: FrontendAppConfig,
                                               val parser: BodyParsers.Default
                                             )
                                             (implicit val executionContext: ExecutionContext) extends IdentifierAction with AuthorisedFunctions {

  private type RetrievalsType = Option[Credentials] ~ Option[String] ~ ConfidenceLevel

  override def invokeBlock[A](request: Request[A], block: IdentifierRequest[A] => Future[Result]): Future[Result] = {

    implicit val hc: HeaderCarrier = HeaderCarrierConverter.fromRequestAndSession(request, request.session)

    val retrievals: Retrieval[RetrievalsType] =
      Retrievals.credentials and
        Retrievals.internalId and
        Retrievals.confidenceLevel

    authorised().retrieve(retrievals) {
      case Some(credentials) ~ Some(internalId) ~ L250 =>
        isRegistered(credentials.providerId).flatMap {
          case true => block(IdentifierRequest(request, internalId, credentials.providerId))
          case false => redirectToRegister()
        }
      case _ ~ _ ~ confidenceLevel => throw new Exception("confidenceLevel not met")

    } recover {
      case ex: Throwable =>
        throw ex
    }
  }

  private def isRegistered(credId: String)(implicit hc: HeaderCarrier): Future[Boolean] = {
    ngrConnector.getRatepayer(CredId(credId)).flatMap { maybeRatepayer =>
      Future.successful(maybeRatepayer.flatMap(_.ratepayerRegistration).flatMap(_.isRegistered).getOrElse(false))
    }
  }

  private def redirectToRegister(): Future[Result] = {
    Future.successful(Redirect(s"${appConfig.registrationHost}/ngr-login-register-frontend/register"))
  }
}

