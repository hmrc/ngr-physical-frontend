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
import models.{AssessmentId, ExternalFeature, Mode, UserAnswers, WhatHappenedTo}
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

  def onPageLoadLoadingBays(mode: Mode, assessmentId: AssessmentId): Action[AnyContent] = onPageLoad(LoadingBays, mode, assessmentId)
  def onPageLoadLockupGarage(mode: Mode, assessmentId: AssessmentId): Action[AnyContent] = onPageLoad(LockupGarages, mode, assessmentId)
  def onPageLoadOutdoorSeating(mode: Mode, assessmentId: AssessmentId): Action[AnyContent] = onPageLoad(OutdoorSeating, mode, assessmentId)
  def onPageLoadParking(mode: Mode, assessmentId: AssessmentId): Action[AnyContent] = onPageLoad(Parking, mode, assessmentId)
  def onPageLoadSolarPanels(mode: Mode, assessmentId: AssessmentId): Action[AnyContent] = onPageLoad(SolarPanels, mode, assessmentId)
  def onPageLoadAdvertisingDisplays(mode: Mode, assessmentId: AssessmentId): Action[AnyContent] = onPageLoad(AdvertisingDisplays, mode, assessmentId)
  def onPageLoadBikeSheds(mode: Mode, assessmentId: AssessmentId): Action[AnyContent] = onPageLoad(BikeSheds, mode, assessmentId)
  def onPageLoadCanopies(mode: Mode, assessmentId: AssessmentId): Action[AnyContent] = onPageLoad(Canopies, mode, assessmentId)
  def onPageLoadLandHardSurfacedFenced(mode: Mode, assessmentId: AssessmentId): Action[AnyContent] = onPageLoad(LandHardSurfacedFenced, mode, assessmentId)
  def onPageLoadLandHardSurfacedOpen(mode: Mode, assessmentId: AssessmentId): Action[AnyContent] = onPageLoad(LandHardSurfacedOpen, mode, assessmentId)
  def onPageLoadLandGravelledFenced(mode: Mode, assessmentId: AssessmentId): Action[AnyContent] = onPageLoad(LandGravelledFenced, mode, assessmentId)
  def onPageLoadLandGravelledOpen(mode: Mode, assessmentId: AssessmentId): Action[AnyContent] = onPageLoad(LandGravelledOpen, mode, assessmentId)
  def onPageLoadLandUnsurfacedFenced(mode: Mode, assessmentId: AssessmentId): Action[AnyContent] = onPageLoad(LandUnsurfacedFenced, mode, assessmentId)
  def onPageLoadLandUnsurfacedOpen(mode: Mode, assessmentId: AssessmentId): Action[AnyContent] = onPageLoad(LandUnsurfacedOpen, mode, assessmentId)
  def onPageLoadPortableBuildings(mode: Mode, assessmentId: AssessmentId): Action[AnyContent] = onPageLoad(PortableBuildings, mode, assessmentId)
  def onPageLoadShippingContainers(mode: Mode, assessmentId: AssessmentId): Action[AnyContent] = onPageLoad(ShippingContainers, mode, assessmentId)

  private def onPageLoad(feature: ExternalFeature, mode: Mode, assessmentId: AssessmentId): Action[AnyContent] = (identify andThen getData andThen requireData) {
    implicit request =>

      val form: Form[WhatHappenedTo] = formProvider(feature)

      val preparedForm = request.userAnswers.get(WhatHappenedTo.page(feature, assessmentId)) match {
        case None => form
        case Some(value) => form.fill(value)
      }

      val radioItems = WhatHappenedTo.options(feature)
      val action = WhatHappenedTo.submitAction(feature, mode, assessmentId)
      val strings = WhatHappenedTo.messageKeys(feature)
      Ok(view(request.property.addressFull, strings, action, radioItems, preparedForm, mode, createDefaultNavBar()))
  }

  def onPageSubmitLoadLoadingBays(mode: Mode, assessmentId: AssessmentId): Action[AnyContent] = onSubmit(LoadingBays, mode, assessmentId)
  def onPageSubmitLockupGarage(mode: Mode, assessmentId: AssessmentId): Action[AnyContent] = onSubmit(LockupGarages, mode, assessmentId)
  def onPageSubmitOutdoorSeating(mode: Mode, assessmentId: AssessmentId): Action[AnyContent] = onSubmit(OutdoorSeating, mode, assessmentId)
  def onPageSubmitParking(mode: Mode, assessmentId: AssessmentId): Action[AnyContent] = onSubmit(Parking, mode, assessmentId)
  def onPageSubmitSolarPanels(mode: Mode, assessmentId: AssessmentId): Action[AnyContent] = onSubmit(SolarPanels, mode, assessmentId)
  def onPageSubmitAdvertisingDisplays(mode: Mode, assessmentId: AssessmentId): Action[AnyContent] = onSubmit(AdvertisingDisplays, mode, assessmentId)
  def onPageSubmitBikeSheds(mode: Mode, assessmentId: AssessmentId): Action[AnyContent] = onSubmit(BikeSheds, mode, assessmentId)
  def onPageSubmitCanopies(mode: Mode, assessmentId: AssessmentId): Action[AnyContent] = onSubmit(Canopies, mode, assessmentId)
  def onPageSubmitLandHardSurfacedFenced(mode: Mode, assessmentId: AssessmentId): Action[AnyContent] = onSubmit(LandHardSurfacedFenced, mode, assessmentId)
  def onPageSubmitLandHardSurfacedOpen(mode: Mode, assessmentId: AssessmentId): Action[AnyContent] = onSubmit(LandHardSurfacedOpen, mode, assessmentId)
  def onPageSubmitLandGravelledFenced(mode: Mode, assessmentId: AssessmentId): Action[AnyContent] = onSubmit(LandGravelledFenced, mode, assessmentId)
  def onPageSubmitLandGravelledOpen(mode: Mode, assessmentId: AssessmentId): Action[AnyContent] = onSubmit(LandGravelledOpen, mode, assessmentId)
  def onPageSubmitLandUnsurfacedFenced(mode: Mode, assessmentId: AssessmentId): Action[AnyContent] = onSubmit(LandUnsurfacedFenced, mode, assessmentId)
  def onPageSubmitLandUnsurfacedOpen(mode: Mode, assessmentId: AssessmentId): Action[AnyContent] = onSubmit(LandUnsurfacedOpen, mode, assessmentId)
  def onPageSubmitPortableBuildings(mode: Mode, assessmentId: AssessmentId): Action[AnyContent] = onSubmit(PortableBuildings, mode, assessmentId)
  def onPageSubmitShippingContainers(mode: Mode, assessmentId: AssessmentId): Action[AnyContent] = onSubmit(ShippingContainers, mode, assessmentId)
  

  private def onSubmit(feature: ExternalFeature, mode: Mode, assessmentId: AssessmentId): Action[AnyContent] = (identify andThen getData andThen requireData).async {
    implicit request =>
      val form: Form[WhatHappenedTo] = formProvider(feature)

      form.bindFromRequest().fold(
        formWithErrors =>
          val radioItems = WhatHappenedTo.options(feature)
          val action = WhatHappenedTo.submitAction(feature, mode, assessmentId)
          val strings = WhatHappenedTo.messageKeys(feature)
          Future.successful(BadRequest(view(request.property.addressFull, strings, action, radioItems, formWithErrors, mode, createDefaultNavBar()))),

        value =>
          for {
            updatedAnswers <- Future.fromTry(request.userAnswers.set(WhatHappenedTo.page(feature, assessmentId), value))
            _              <- sessionRepository.set(updatedAnswers)
          } yield Redirect(navigator.nextPage(WhatHappenedTo.page(feature, assessmentId), mode, updatedAnswers, assessmentId))
      )
  }
}
