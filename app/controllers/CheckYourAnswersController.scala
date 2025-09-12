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

import actions.{DataRequiredAction, DataRetrievalAction, IdentifierAction}
import com.google.inject.{Inject, Singleton}
import config.{AppConfig, FrontendAppConfig}
import models.NavBarPageContents.createDefaultNavBar
import models.UserAnswers
import pages.WhenCompleteChangePage
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import uk.gov.hmrc.govukfrontend.views.viewmodels.summarylist.SummaryList
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import utils.CheckYourAnswersHelper
import viewmodels.Section
import viewmodels.checkAnswers.WhenCompleteChangeSummary
import viewmodels.govuk.summarylist.*
import views.html.CheckYourAnswersView

import scala.concurrent.ExecutionContext

@Singleton
class CheckYourAnswersController @Inject()(
                                            override val messagesApi: MessagesApi,
                                            identify: IdentifierAction,
                                            getData: DataRetrievalAction,
                                            val controllerComponents: MessagesControllerComponents,
                                            cyaHelper: CheckYourAnswersHelper,
                                            view: CheckYourAnswersView
                                          )(implicit appConfig: AppConfig)  extends FrontendBaseController with I18nSupport {

  def onPageLoad(): Action[AnyContent] = (identify andThen getData) {
    implicit request =>


      val list: Seq[Section] = cyaHelper.createSectionList(request.userAnswers.getOrElse(UserAnswers(request.userId)))

      Ok(view(request.property.addressFull, createDefaultNavBar(), list))
  }

  def onSubmit(): Action[AnyContent] =
    (identify andThen getData) {
      implicit request =>
        Redirect(routes.DeclarationController.show)
    }
  
}
