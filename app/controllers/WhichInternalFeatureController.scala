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
import forms.WhichInternalFeatureFormProvider
import models.InternalFeature
import models.InternalFeature.*
import models.NavBarPageContents.createDefaultNavBar
import play.api.data.Form
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents, Result}
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import views.html.WhichInternalFeatureView
import javax.inject.{Inject, Singleton}
import scala.concurrent.Future

@Singleton
class WhichInternalFeatureController @Inject()(identify: IdentifierAction,
                                                getData: DataRetrievalAction,
                                                formProvider: WhichInternalFeatureFormProvider,
                                                val controllerComponents: MessagesControllerComponents,
                                                view: WhichInternalFeatureView
                                              )(implicit appConfig: AppConfig) extends FrontendBaseController with I18nSupport {

  val form: Form[String] = formProvider()

  def onPageLoad: Action[AnyContent] = (identify andThen getData) {
    implicit request =>
      Ok(view(request.property.addressFull, form, createDefaultNavBar()))
  }

  def onSubmit: Action[AnyContent] = (identify andThen getData).async {
    implicit request =>
      form.bindFromRequest().fold(
        formWithErrors =>
          println(formWithErrors)
          Future.successful(BadRequest(view(request.property.addressFull, formWithErrors, createDefaultNavBar()))),
        value =>
          val optionalFeature = value match {
            case "other" =>
              request.body.asFormUrlEncoded.flatMap(_.get("otherSelect").flatMap(_.headOption))
                .flatMap(InternalFeature.withNameOption)
            case _ =>
              InternalFeature.withNameOption(value)
          }
          optionalFeature match {
            case Some(feature) =>
              nextPage(feature)
            case None =>
              val errorForm = form.withError("value", "whichInternalFeature.error.required")
              Future.successful(BadRequest(view(request.property.addressFull, errorForm, createDefaultNavBar())))
          }
      )

  }

  private def nextPage(feature: InternalFeature): Future[Result] = {
    val call = feature match {
      case SecurityCamera => routes.WhichInternalFeatureController.onPageLoad // Group 2
      case CompressedAir => routes.WhichInternalFeatureController.onPageLoad // Group 3
      case Escalators => routes.WhichInternalFeatureController.onPageLoad // Group 4
      case _ => routes.WhichInternalFeatureController.onPageLoad // Group 1
    }
    Future.successful(Redirect(call))
  }

}
