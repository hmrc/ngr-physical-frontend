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

import helpers.ControllerSpecSupport
import models.registration.CredId
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.when
import play.api.http.Status
import play.api.test.Helpers.*
import views.html.NotToldAnyChangesView

import scala.concurrent.Future

class NotToldAnyChangesControllerSpec
  extends ControllerSpecSupport {
  lazy val view: NotToldAnyChangesView = inject[NotToldAnyChangesView]
  private val controller = new NotToldAnyChangesController(fakeAuth, fakeData(None), mcc, view)(mockConfig)

  "GET /" should {
    "return 200 and HTML content with UTF-8 charset" in {
      val result = controller.show(assessmentId)(authenticatedFakeRequest)

      status(result) mustBe Status.OK
      contentType(result) mustBe Some("text/html")
      charset(result) mustBe Some("utf-8")
    }
  }
}
