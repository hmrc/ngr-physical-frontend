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

import actions.{DataRetrievalAction, IdentifierAction}
import config.AppConfig
import forms.SureWantRemoveChangeFormProvider
import models.{CYAExternal, CYAInternal, CYAViewType}
import models.SureWantRemoveChange.getFeatureLabel
import navigation.Navigator
import play.api.data.Form
import play.api.i18n.{I18nSupport, Messages}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import repositories.SessionRepository
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import views.html.SureWantRemoveChangeView
import models.NavBarPageContents.createDefaultNavBar

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class SureWantRemoveChangeController @Inject()(
                                          identify: IdentifierAction,
                                          getData: DataRetrievalAction,
                                          formProvider: SureWantRemoveChangeFormProvider,
                                          val controllerComponents: MessagesControllerComponents,
                                          view: SureWantRemoveChangeView
                                        )(implicit appConfig: AppConfig) extends FrontendBaseController with I18nSupport {

  def onPageLoad(viewType: CYAViewType, featureString: String): Action[AnyContent] =
    (identify andThen getData) {
      implicit request =>
        val form: Form[Boolean] = formProvider(getFeatureLabel(featureString).getOrElse(featureString))
        Ok(view(request.property.addressFull, getTitle(featureString), viewType, featureString, form, createDefaultNavBar()))
    }

  def onSubmit(viewType: CYAViewType, featureString: String): Action[AnyContent] = (identify andThen getData).async {
    implicit request =>
      val form: Form[Boolean] = formProvider(getFeatureLabel(featureString).getOrElse(featureString))

      form.bindFromRequest().fold(
        formWithErrors =>
          Future.successful(BadRequest(view(request.property.addressFull, getTitle(featureString), viewType, featureString, formWithErrors, createDefaultNavBar()))),
        {
          case true => {
            viewType match {
              case CYAInternal => Future.successful(Redirect(routes.SmallCheckYourAnswersController.removeInternal(featureString).url))
              case CYAExternal => Future.successful(Redirect(routes.SmallCheckYourAnswersController.removeExternal(featureString).url))
            }
          }
          case false => Future.successful(Redirect(routes.SmallCheckYourAnswersController.onPageLoad(viewType)))
        }
      )
  }

  private def getTitle(featureString: String)(implicit messages: Messages): String = {

    val featureLabel = getFeatureLabel(featureString)

    featureLabel match {
      case Some(label) => messages("sureWantRemoveChange.title", label)
      case None => messages("sureWantRemoveChange.title", featureString)
    }
  }

}
