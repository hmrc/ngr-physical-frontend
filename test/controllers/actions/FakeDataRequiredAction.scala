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

package controllers.actions

import actions.DataRequiredAction
import models.UserAnswers
import models.requests.{DataRequest, OptionalDataRequest}
import play.api.mvc.*
import scala.concurrent.{ExecutionContext, Future}
import javax.inject.Inject
import controllers.routes

class FakeDataRequiredAction @Inject()(implicit val executionContext: ExecutionContext)
  extends DataRequiredAction {

  var userAnswersToReturn: Option[UserAnswers] = Some(UserAnswers("id"))

  override protected def refine[A](request: OptionalDataRequest[A]): Future[Either[Result, DataRequest[A]]] = {
    userAnswersToReturn match {
      case None =>
        Future.successful(Left(Results.Redirect(routes.JourneyRecoveryController.onPageLoad())))
      case Some(data) =>
        Future.successful(Right(DataRequest(request.request, request.userId, data)))
    }
  }
}