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

import actions.*
import config.AppConfig
import models.NavBarPageContents.createDefaultNavBar
import pages.DeclarationPage
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import repositories.SessionRepository
import uk.gov.hmrc.http.NotFoundException
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import views.html.SubmissionConfirmationView

import javax.inject.Inject
import scala.concurrent.ExecutionContext

class SubmissionConfirmationController @Inject()(
                                                  identify: IdentifierAction,
                                                  getData: DataRetrievalAction,
                                                  requireData: DataRequiredAction,
                                                  val controllerComponents: MessagesControllerComponents,
                                                  view: SubmissionConfirmationView,
                                                  sessionRepository: SessionRepository
                                                )(implicit appConfig: AppConfig, ec: ExecutionContext) extends FrontendBaseController with I18nSupport {

  def onPageLoad: Action[AnyContent] = (identify andThen getData andThen requireData).async {
    implicit request =>
      val ref = request.userAnswers.get(DeclarationPage).getOrElse(throw new NotFoundException("Reference number not found"))
      sessionRepository.clear(request.credId).map { _ =>
        Ok(view(request.property.addressFull, ref, createDefaultNavBar()))
      }
  }
}
