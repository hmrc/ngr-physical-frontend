package controllers.actions

import actions.DataRetrievalAction
import models.UserAnswers
import models.requests.{IdentifierRequest, OptionalDataRequest}
import scala.concurrent.{ExecutionContext, Future}

class FakeDataRetrievalAction(userAnswers: Option[UserAnswers]) extends DataRetrievalAction {
  
  override protected def executionContext: ExecutionContext =
    scala.concurrent.ExecutionContext.Implicits.global

  override protected def transform[A](request: IdentifierRequest[A]): Future[OptionalDataRequest[A]] = {
    Future.successful(
      OptionalDataRequest(request.request, request.credId, userAnswers)
    )
  }
}