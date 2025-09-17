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
import controllers.actions.*
import models.UserAnswers
import navigation.{FakeNavigator, Navigator}
import org.apache.pekko.actor.ActorSystem
import org.apache.pekko.stream.Materializer
import org.scalatest.concurrent.{IntegrationPatience, ScalaFutures}
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers
import org.scalatest.{OptionValues, TryValues}
import org.scalatestplus.mockito.MockitoSugar.mock
import play.api.Application
import play.api.i18n.{Messages, MessagesApi}
import play.api.inject.bind
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.mvc.{AnyContent, BodyParser, Call, MessagesControllerComponents}
import play.api.test.{FakeRequest, Injecting}
import repositories.SessionRepository

import scala.concurrent.ExecutionContext

trait SpecBase
  extends AnyFreeSpec
    with Matchers
    with TryValues
    with OptionValues
    with ScalaFutures
    with IntegrationPatience {

  val userAnswersId: String = "id"

  def emptyUserAnswers: UserAnswers = UserAnswers(userAnswersId)

  def messages(app: Application): Messages =
    app.injector.instanceOf[MessagesApi].preferred(FakeRequest())

  implicit val ec: ExecutionContext = scala.concurrent.ExecutionContext.Implicits.global
  implicit lazy val actorSystem: ActorSystem = ActorSystem("TestActorSystem")
  implicit lazy val mat: Materializer = Materializer(actorSystem)
  val mockSessionRepository: SessionRepository = mock[SessionRepository]
  
  def onwardRoute: Call = Call("GET", "/foo")

  protected def applicationBuilder(userAnswers: Option[UserAnswers] = None): GuiceApplicationBuilder = {
    val stubMcc = play.api.test.Helpers.stubMessagesControllerComponents()
    val bodyParsers: BodyParser[AnyContent] = stubMcc.parsers.defaultBodyParser

    val fakeAuth = new FakeIdentifierAction(bodyParsers)
    val fakeReg = new FakeRegistrationAction(stubMcc.parsers)
    def fakeData(answers: Option[UserAnswers]) = new FakeDataRetrievalAction(answers)

    new GuiceApplicationBuilder()
      .overrides(
        bind[IdentifierAction].toInstance(fakeAuth),
        bind[DataRequiredAction].to[DataRequiredActionImpl],
        bind[RegistrationAction].toInstance(fakeReg),
        bind[DataRetrievalAction].toInstance(fakeData(userAnswers)),
        bind[Navigator].toInstance(new FakeNavigator(onwardRoute)),
        bind[SessionRepository].toInstance(mockSessionRepository)
      )
  }
 }
