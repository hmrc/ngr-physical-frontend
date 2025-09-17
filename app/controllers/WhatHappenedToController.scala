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
import config.AppConfig
import forms.WhatHappenedToFormProvider
import models.ExternalFeature.{AdvertisingDisplays, BikeSheds, Canopies, LandGravelledFenced, LandGravelledOpen, LandHardSurfacedFenced, LandHardSurfacedOpen, LandUnsurfacedFenced, LandUnsurfacedOpen, LoadingBays, LockupGarages, OutdoorSeating, Parking, PortableBuildings, ShippingContainers, SolarPanels}
import models.NavBarPageContents.createDefaultNavBar
import models.{ExternalFeature, Mode, UserAnswers, WhatHappenedTo}
import navigation.Navigator
import pages.*
import play.api.data.Form
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import repositories.SessionRepository
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import views.html.WhatHappenedToView

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class WhatHappenedToController @Inject()(
                                       sessionRepository: SessionRepository,
                                       navigator: Navigator,
                                       identify: IdentifierAction,
                                       getData: DataRetrievalAction,
                                       requireData: DataRequiredAction,
                                       formProvider: WhatHappenedToFormProvider,
                                       val controllerComponents: MessagesControllerComponents,
                                       view: WhatHappenedToView
                                     )(implicit ec: ExecutionContext, appConfig: AppConfig) extends FrontendBaseController with I18nSupport {

  def onPageLoadLoadingBays(mode: Mode): Action[AnyContent] = onPageLoad(LoadingBays, mode)
  def onPageLoadLockupGarage(mode: Mode): Action[AnyContent] = onPageLoad(LockupGarages, mode)
  def onPageLoadOutdoorSeating(mode: Mode): Action[AnyContent] = onPageLoad(OutdoorSeating, mode)
  def onPageLoadParking(mode: Mode): Action[AnyContent] = onPageLoad(Parking, mode)
  def onPageLoadSolarPanels(mode: Mode): Action[AnyContent] = onPageLoad(SolarPanels, mode)
  def onPageLoadAdvertisingDisplays(mode: Mode): Action[AnyContent] = onPageLoad(AdvertisingDisplays, mode)
  def onPageLoadBikeSheds(mode: Mode): Action[AnyContent] = onPageLoad(BikeSheds, mode)
  def onPageLoadCanopies(mode: Mode): Action[AnyContent] = onPageLoad(Canopies, mode)
  def onPageLoadLandHardSurfacedFenced(mode: Mode): Action[AnyContent] = onPageLoad(LandHardSurfacedFenced, mode)
  def onPageLoadLandHardSurfacedOpen(mode: Mode): Action[AnyContent] = onPageLoad(LandHardSurfacedOpen, mode)
  def onPageLoadLandGravelledFenced(mode: Mode): Action[AnyContent] = onPageLoad(LandGravelledFenced, mode)
  def onPageLoadLandGravelledOpen(mode: Mode): Action[AnyContent] = onPageLoad(LandGravelledOpen, mode)
  def onPageLoadLandUnsurfacedFenced(mode: Mode): Action[AnyContent] = onPageLoad(LandUnsurfacedFenced, mode)
  def onPageLoadLandUnsurfacedOpen(mode: Mode): Action[AnyContent] = onPageLoad(LandUnsurfacedOpen, mode)
  def onPageLoadPortableBuildings(mode: Mode): Action[AnyContent] = onPageLoad(PortableBuildings, mode)
  def onPageLoadShippingContainers(mode: Mode): Action[AnyContent] = onPageLoad(ShippingContainers, mode)

  def onPageLoad(feature: ExternalFeature, mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData) {
    implicit request =>
      
      val form: Form[WhatHappenedTo] = formProvider(feature)

      val preparedForm = request.userAnswers.get(WhatHappenedTo.page(feature)) match {
        case None => form
        case Some(value) => form.fill(value)
      }

      val radioItems = WhatHappenedTo.options(feature)
      val action = WhatHappenedTo.submitAction(feature, mode)
      val strings = WhatHappenedTo.messageKeys(feature)
      Ok(view(request.property.addressFull, strings, action, radioItems, preparedForm, mode, createDefaultNavBar()))
  }

  def onPageSubmitLoadLoadingBays(mode: Mode): Action[AnyContent] = onSubmit(LoadingBays, mode)
  def onPageSubmitLockupGarage(mode: Mode): Action[AnyContent] = onSubmit(LockupGarages, mode)
  def onPageSubmitOutdoorSeating(mode: Mode): Action[AnyContent] = onSubmit(OutdoorSeating, mode)
  def onPageSubmitParking(mode: Mode): Action[AnyContent] = onSubmit(Parking, mode)
  def onPageSubmitSolarPanels(mode: Mode): Action[AnyContent] = onSubmit(SolarPanels, mode)
  def onPageSubmitAdvertisingDisplays(mode: Mode): Action[AnyContent] = onSubmit(AdvertisingDisplays, mode)
  def onPageSubmitBikeSheds(mode: Mode): Action[AnyContent] = onSubmit(BikeSheds, mode)
  def onPageSubmitCanopies(mode: Mode): Action[AnyContent] = onSubmit(Canopies, mode)
  def onPageSubmitLandHardSurfacedFenced(mode: Mode): Action[AnyContent] = onSubmit(LandHardSurfacedFenced, mode)
  def onPageSubmitLandHardSurfacedOpen(mode: Mode): Action[AnyContent] = onSubmit(LandHardSurfacedOpen, mode)
  def onPageSubmitLandGravelledFenced(mode: Mode): Action[AnyContent] = onSubmit(LandGravelledFenced, mode)
  def onPageSubmitLandGravelledOpen(mode: Mode): Action[AnyContent] = onSubmit(LandGravelledOpen, mode)
  def onPageSubmitLandUnsurfacedFenced(mode: Mode): Action[AnyContent] = onSubmit(LandUnsurfacedFenced, mode)
  def onPageSubmitLandUnsurfacedOpen(mode: Mode): Action[AnyContent] = onSubmit(LandUnsurfacedOpen, mode)
  def onPageSubmitPortableBuildings(mode: Mode): Action[AnyContent] = onSubmit(PortableBuildings, mode)
  def onPageSubmitShippingContainers(mode: Mode): Action[AnyContent] = onSubmit(ShippingContainers, mode)
  

  def onSubmit(feature: ExternalFeature, mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async {
    implicit request =>
      val form: Form[WhatHappenedTo] = formProvider(feature)

      form.bindFromRequest().fold(
        formWithErrors =>
          val radioItems = WhatHappenedTo.options(feature)
          val action = WhatHappenedTo.submitAction(feature, mode)
          val strings = WhatHappenedTo.messageKeys(feature)
          Future.successful(BadRequest(view(request.property.addressFull, strings, action, radioItems, formWithErrors, mode, createDefaultNavBar()))),

        value =>
          for {
            updatedAnswers <- Future.fromTry(request.userAnswers.set(WhatHappenedTo.page(feature), value))
            _              <- sessionRepository.set(updatedAnswers)
          } yield Redirect(navigator.nextPage(WhatHappenedTo.page(feature), mode, updatedAnswers))
      )
  }
}
