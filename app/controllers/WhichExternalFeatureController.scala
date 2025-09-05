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
import forms.WhichExternalFeatureFormProvider
import models.ExternalFeature.*
import models.NavBarPageContents.createDefaultNavBar
import models.{ExternalFeature, Mode, NormalMode, WhatHappenedTo}
import play.api.data.Form
import play.api.i18n.I18nSupport
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents, Result}
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import views.html.WhichExternalFeatureView

import javax.inject.{Inject, Singleton}
import scala.concurrent.Future

@Singleton
class WhichExternalFeatureController @Inject()(identify: IdentifierAction,
                                               getData: DataRetrievalAction,
                                               formProvider: WhichExternalFeatureFormProvider,
                                               val controllerComponents: MessagesControllerComponents,
                                               view: WhichExternalFeatureView
                                              )(implicit appConfig: AppConfig) extends FrontendBaseController with I18nSupport {

  val form: Form[ExternalFeature] = formProvider()

  def onPageLoad(mode: Mode): Action[AnyContent] = (identify andThen getData) {
    implicit request =>
      Ok(view(request.property.addressFull, form, createDefaultNavBar(), mode))
  }

  def onSubmit(mode: Mode): Action[AnyContent] = (identify andThen getData).async {
    implicit request =>
      form.bindFromRequest().fold(
        formWithErrors =>
          Future.successful(BadRequest(view(request.property.addressFull, formWithErrors, createDefaultNavBar(), mode))),
        feature =>
          nextPage(feature, mode)
      )
  }

  private def nextPage(feature: ExternalFeature, mode: Mode): Future[Result] = {
    Future.successful(Redirect(WhatHappenedTo.pageLoadAction(feature, mode)))
  }

}
