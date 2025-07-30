package controllers.actions

import actions.IdentifierAction
import models.requests.IdentifierRequest
import play.api.mvc.*

import scala.concurrent.{ExecutionContext, Future}

class FakeIdentifierAction(
                            userId: String,
                            val parser: BodyParser[AnyContent]
                          )(implicit val executionContext: ExecutionContext)
  extends IdentifierAction {

  override def invokeBlock[A](request: Request[A], block: IdentifierRequest[A] => Future[Result]): Future[Result] = {
    val newRequest = new IdentifierRequest(request, userId, userId)
    block(newRequest)
  }
}