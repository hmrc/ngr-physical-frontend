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

package controllers

import actions.{ IdentifierAction, RegistrationAction}
import play.api.i18n.I18nSupport
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import config.AppConfig
import connectors.NGRConnector
import models.NavBarPageContents.createDefaultNavBar
import models.registration.CredId
import views.html.ChangedFeaturesOrSpaceView
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendController
import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ChangedFeaturesOrSpaceController @Inject()(
                                                  mcc: MessagesControllerComponents,
                                                  view: ChangedFeaturesOrSpaceView,
                                                  ngrConnector: NGRConnector,
                                                  authenticate: IdentifierAction,
                                                  isRegisteredCheck: RegistrationAction,
                                                )(implicit appConfig: AppConfig, ec: ExecutionContext) extends FrontendController(mcc) with I18nSupport {

  def show: Action[AnyContent] =
    (authenticate andThen isRegisteredCheck).async { implicit request =>
      val credId = request.credId
      ngrConnector.getLinkedProperty(CredId(credId)).flatMap {
        case Some(property) => 
          Future.successful(Ok(view(property.addressFull, createDefaultNavBar())))
        case None => throw new RuntimeException("No Address found")
      }
    }

  def next: Action[AnyContent] =
    (authenticate andThen isRegisteredCheck).async { implicit request =>
      Future.successful(Redirect(routes.InfoAndSupportingDocController.show))
    }
}

