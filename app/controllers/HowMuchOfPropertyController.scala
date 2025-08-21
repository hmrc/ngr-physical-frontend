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
import forms.HowMuchOfPropertyFormProvider
import models.InternalFeature.{AirConditioning, CompressedAir, Escalators, GoodsLift, Heating, PassengerLift, Sprinklers}
import models.NavBarPageContents.createDefaultNavBar
import models.{HowMuchOfProperty, InternalFeature, InternalFeatureGroup1, Mode, UserAnswers}
import navigation.Navigator
import pages.*
import play.api.data.Form
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, Call, MessagesControllerComponents}
import repositories.SessionRepository
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import views.html.HowMuchOfPropertyView

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class HowMuchOfPropertyController @Inject()(
                                       sessionRepository: SessionRepository,
                                       navigator: Navigator,
                                       identify: IdentifierAction,
                                       getData: DataRetrievalAction,
                                       formProvider: HowMuchOfPropertyFormProvider,
                                       val controllerComponents: MessagesControllerComponents,
                                       view: HowMuchOfPropertyView
                                     )(implicit ec: ExecutionContext, appConfig: AppConfig) extends FrontendBaseController with I18nSupport {

  
  def onPageLoadAirCon(mode: Mode): Action[AnyContent] = onPageLoad(AirConditioning, mode)
  def onPageLoadHeating(mode: Mode): Action[AnyContent] = onPageLoad(Heating, mode)
  def onPageLoadSprinklers(mode: Mode): Action[AnyContent] = onPageLoad(Sprinklers, mode)
  def onPageLoadGoodsLift(mode: Mode): Action[AnyContent] = onPageLoad(GoodsLift, mode)
  def onPageLoadEscalator(mode: Mode): Action[AnyContent] = onPageLoad(Escalators, mode)
  def onPageLoadPassengerLift(mode: Mode): Action[AnyContent] = onPageLoad(PassengerLift, mode)
  def onPageLoadCompressedAir(mode: Mode): Action[AnyContent] = onPageLoad(CompressedAir, mode)

  def onPageLoad(feature: InternalFeatureGroup1, mode: Mode): Action[AnyContent] = (identify andThen getData) {
    implicit request =>

      val form: Form[HowMuchOfProperty] = formProvider(feature)

      val preparedForm = request.userAnswers.getOrElse(UserAnswers(request.userId)).get(HowMuchOfProperty.page(feature)) match {
        case None => form
        case Some(value) => form.fill(value)
      }

      val radioItems = HowMuchOfProperty.options(feature)
      val action = HowMuchOfProperty.submitAction(feature, mode)
      val strings = HowMuchOfProperty.messageKeys(feature)
      Ok(view(request.property.addressFull, strings, action, radioItems, preparedForm, mode, createDefaultNavBar()))
  }

  def onSubmitAirCon(mode: Mode): Action[AnyContent] = onSubmit(AirConditioning, mode)
  def onSubmitHeating(mode: Mode): Action[AnyContent] = onSubmit(Heating, mode)
  def onSubmitSprinklers(mode: Mode): Action[AnyContent] = onSubmit(Sprinklers, mode)
  def onSubmitGoodsLift(mode: Mode): Action[AnyContent] = onSubmit(GoodsLift, mode)
  def onSubmitEscalator(mode: Mode): Action[AnyContent] = onSubmit(Escalators, mode)
  def onSubmitPassengerLift(mode: Mode): Action[AnyContent] = onSubmit(PassengerLift, mode)
  def onSubmitCompressedAir(mode: Mode): Action[AnyContent] = onSubmit(CompressedAir, mode)

  def onSubmit(feature: InternalFeatureGroup1, mode: Mode): Action[AnyContent] = (identify andThen getData).async {
    implicit request =>

      val form: Form[HowMuchOfProperty] = formProvider(feature)

      form.bindFromRequest().fold(
        formWithErrors =>
          val radioItems = HowMuchOfProperty.options(feature)
          val action = HowMuchOfProperty.submitAction(feature, mode)
          val strings = HowMuchOfProperty.messageKeys(feature)
          Future.successful(BadRequest(view(request.property.addressFull, strings, action, radioItems, formWithErrors, mode, createDefaultNavBar()))),

        value =>
          for {
            updatedAnswers <- Future.fromTry(request.userAnswers.getOrElse(UserAnswers(request.userId)).set(HowMuchOfProperty.page(feature), value))
            _              <- sessionRepository.set(updatedAnswers)
          } yield Redirect(navigator.nextPage(HowMuchOfProperty.page(feature), mode, updatedAnswers))
      )
  }
}
