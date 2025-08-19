package controllers

import forms.WhichExternalFeatureFormProvider
import helpers.ControllerSpecSupport
import models.ExternalFeature
import views.html.WhichExternalFeatureView
import play.api.test.Helpers.*

class WhichExternalFeatureControllerSpec extends ControllerSpecSupport {
  lazy val view: WhichExternalFeatureView = inject[WhichExternalFeatureView]
  lazy val formProvider: WhichExternalFeatureFormProvider = WhichExternalFeatureFormProvider()
  private val controller: WhichExternalFeatureController = new WhichExternalFeatureController(
    identify = fakeAuth, getData = fakeData(None), formProvider = formProvider, controllerComponents = mcc, view = view
  )


  "WhichExternalFeatureController" should {
    "onPageLoad" must {
      "return 200" in {
        val result = controller.onPageLoad(authenticatedFakeRequest)
        status(result) mustBe 200
      }

      "return HTML" in {
        val result = controller.onPageLoad(authenticatedFakeRequest)
        contentType(result) mustBe Some("text/html")
        charset(result) mustBe Some("utf-8")
      }
    }
    "onSubmit" must {
      "redirect" in {
        ExternalFeature.values.map { feature =>
          val formRequest = requestWithForm(Map("value" -> feature.toString))
          val result = controller.onSubmit(formRequest)
          status(result) mustBe 303
        }
      }

      "get other value" in {
        val formRequest = requestWithForm(Map("value" -> "other", "otherSelect" -> "shippingContainers"))
        val result = controller.onSubmit(formRequest)
        status(result) mustBe 303
      }

      "BadRequest when no form data" in {
        val result = controller.onSubmit(authenticatedFakeRequest)
        status(result) mustBe 400
      }
    }

  }

}
