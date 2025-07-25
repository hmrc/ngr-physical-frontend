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

import com.google.inject.ImplementedBy
import play.api.mvc.Results.Redirect
import play.api.mvc._
import uk.gov.hmrc.auth.core.retrieve.Name
import uk.gov.hmrc.http.HeaderCarrier
import config.AppConfig
import connectors.NGRConnector
import models.auth.AuthenticatedUserRequest
import models.registration.CredId
import uk.gov.hmrc.play.http.HeaderCarrierConverter
import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class RegistrationActionImpl @Inject()(
                                    ngrConnector: NGRConnector,
                                    authenticate: AuthRetrievals,
                                    appConfig: AppConfig,
                                    mcc: MessagesControllerComponents
                                  )(implicit ec: ExecutionContext) extends RegistrationAction {

  override def invokeBlock[A](request: Request[A], block: AuthenticatedUserRequest[A] => Future[Result]): Future[Result] = {

    authenticate.invokeBlock(request, { implicit authRequest: AuthenticatedUserRequest[A] =>
      implicit val hc: HeaderCarrier = HeaderCarrierConverter.fromRequestAndSession(authRequest, authRequest.session)

      val credId = CredId(authRequest.credId.getOrElse(""))

      ngrConnector.getRatepayer(credId).flatMap{ maybeRatepayer =>
        val isRegistered = maybeRatepayer
          .flatMap(_.ratepayerRegistration)
          .flatMap(_.isRegistered)
          .getOrElse(false)

        val name:Option[String] = maybeRatepayer
          .flatMap(user => user.ratepayerRegistration)
          .map(info => info.name.map(value => value.value))
          .getOrElse(Some(""))

        if (isRegistered) {
          block(authRequest.copy( name = Some(Name(name = name, lastName = Some("")))))
        } else {
         redirectToRegister()
        }
      }
    })
  }

  private def redirectToRegister(): Future[Result] = {
    Future.successful(Redirect(s"${appConfig.registrationUrl}/ngr-login-register-frontend/register"))
  }
  // $COVERAGE-OFF$
  override def parser: BodyParser[AnyContent] = mcc.parsers.defaultBodyParser

  override protected def executionContext: ExecutionContext = ec
  // $COVERAGE-ON$

}

@ImplementedBy(classOf[RegistrationActionImpl])
trait RegistrationAction extends ActionBuilder[AuthenticatedUserRequest, AnyContent] with ActionFunction[Request, AuthenticatedUserRequest]
