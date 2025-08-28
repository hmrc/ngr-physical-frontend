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
import forms.SureWantRemoveChangeFormProvider
import models.NavBarPageContents.createDefaultNavBar
import models.Sure
import navigation.Navigator
import play.api.data.Form
import play.api.i18n.I18nSupport
import play.api.mvc.{Action, AnyContent, Call, MessagesControllerComponents}
import repositories.SessionRepository
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import views.html.SureWantRemoveChangeView

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class SureWantRemoveChangeController @Inject()(
                                          sessionRepository: SessionRepository,
                                          navigator: Navigator,
                                          identify: IdentifierAction,
                                          getData: DataRetrievalAction,
                                          formProvider: SureWantRemoveChangeFormProvider,
                                          val controllerComponents: MessagesControllerComponents,
                                          view: SureWantRemoveChangeView
                                        )(implicit ec: ExecutionContext, appConfig: AppConfig) extends FrontendBaseController with I18nSupport {

  def onPageLoad(featureString: String): Action[AnyContent] =
    (identify andThen getData) {
      implicit request =>
        val form: Form[Boolean] = formProvider(featureString)
        Ok(view(request.property.addressFull, Sure.message(featureString), featureString, form, createDefaultNavBar()))
    }

  def next(): Action[AnyContent] =
    (identify andThen getData) {
      Redirect(routes.IndexController.onPageLoad())
    }

  def onSubmit(featureString: String): Action[AnyContent] = (identify andThen getData).async {
    implicit request =>
      val form: Form[Boolean] = formProvider(featureString)

      form.bindFromRequest().fold(
        formWithErrors =>
          Future.successful(BadRequest(view(request.property.addressFull, Sure.message(featureString), featureString, formWithErrors, createDefaultNavBar()))),
        {
          case true => Future.successful(Redirect(routes.IndexController.onPageLoad()))
          case false => Future.successful(Redirect(routes.IndexController.onPageLoad()))
        }
      )
  }
}
