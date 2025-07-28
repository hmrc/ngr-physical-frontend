package controllers

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.Application
import play.api.http.Status
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.test.FakeRequest
import play.api.test.Helpers.*

class ChangedFeaturesOrSpaceControllerSpec
  extends AnyWordSpec
  with Matchers
  with GuiceOneAppPerSuite {

  override def fakeApplication(): Application =
    new GuiceApplicationBuilder()
      .build()

  private val fakeRequest = FakeRequest("GET", "/")
  private val controller = app.injector.instanceOf[ChangedFeaturesOrSpaceController]

  "GET /" should :
    "return 200" in :
      val result = controller.show(fakeRequest)
      status(result) shouldBe Status.OK

    "return HTML" in :
      val result = controller.show(fakeRequest)
      contentType(result) shouldBe Some("text/html")
      charset(result) shouldBe Some("utf-8")
}
