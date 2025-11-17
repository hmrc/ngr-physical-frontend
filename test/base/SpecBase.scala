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

package base

import actions.*
import centralisedauthorisation.resourceclient.filters.ResourceClientFilter
import centralisedauthorisation.resourceclient.modules.FrontendCentralisedAuthorisationModule
import centralisedauthorisation.resourceclient.utils
import config.AppConfig
import connectors.NGRNotifyConnector
import controllers.actions.*
import models.ExternalFeature.LoadingBays
import models.InternalFeature.AirConditioning
import models.{AnythingElseData, AssessmentId, ChangeToUseOfSpace, HowMuchOfProperty, UseOfSpaces, UserAnswers, WhatHappenedTo}
import navigation.{FakeNavigator, Navigator}
import org.apache.pekko.actor.ActorSystem
import org.apache.pekko.stream.Materializer
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.when
import org.mockito.invocation.InvocationOnMock
import org.scalatest.concurrent.{IntegrationPatience, ScalaFutures}
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers
import org.scalatest.{OptionValues, TryValues}
import org.scalatestplus.mockito.MockitoSugar.mock
import pages.{AnythingElsePage, ChangeToUseOfSpacePage, DeclarationPage, HaveYouChangedSpacePage, SecurityCamerasChangePage, UploadDocumentsPage, WhenCompleteChangePage}
import play.api.Application
import play.api.i18n.{Messages, MessagesApi}
import play.api.inject.bind
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.mvc.{AnyContent, BodyParser, Call, EssentialAction, MessagesControllerComponents, RequestHeader, Result}
import play.api.test.{FakeRequest, Injecting}
import repositories.SessionRepository

import java.time.LocalDate
import scala.concurrent.{ExecutionContext, Future}

trait SpecBase
  extends AnyFreeSpec
    with Matchers
    with TryValues
    with OptionValues
    with ScalaFutures
    with IntegrationPatience {

  def messages(app: Application): Messages =
    app.injector.instanceOf[MessagesApi].preferred(FakeRequest())

  implicit val ec: ExecutionContext = scala.concurrent.ExecutionContext.Implicits.global
  val mockSessionRepository: SessionRepository = mock[SessionRepository]
  def onwardRoute: Call = Call("GET", "/foo")

  val fakeFilter: ResourceClientFilter = mock[ResourceClientFilter]

  when(fakeFilter.apply(any[EssentialAction])).thenAnswer((invocation: InvocationOnMock) => {
    val next: EssentialAction = invocation.getArgument(0, classOf[EssentialAction])
    next
  })
  
  protected def applicationBuilder(userAnswers: Option[UserAnswers] = None): GuiceApplicationBuilder = {
    new GuiceApplicationBuilder()
      .overrides(
        bind[ResourceClientFilter].toInstance(fakeFilter),
        bind[IdentifierAction].to[FakeIdentifierAction],
        bind[DataRequiredAction].to[DataRequiredActionImpl],
        bind[DataRetrievalAction].toInstance(new FakeDataRetrievalAction(userAnswers)),
        bind[Navigator].toInstance(new FakeNavigator(onwardRoute)),
        bind[SessionRepository].toInstance(mockSessionRepository)
      )
  }
 }
