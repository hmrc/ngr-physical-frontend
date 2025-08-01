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

import helpers.{ControllerSpecSupport, TestData}
import models.registration.CredId
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.when
import play.api.http.Status
import play.api.test.Helpers.*
import views.html.ChangedFeaturesOrSpaceView
import scala.concurrent.Future

class ChangedFeaturesOrSpaceControllerSpec
  extends ControllerSpecSupport {
  lazy val view: ChangedFeaturesOrSpaceView = inject[ChangedFeaturesOrSpaceView]
  private val controller = new ChangedFeaturesOrSpaceController(mcc, view, fakeAuth, fakeReg, fakeData(None))(mockConfig)

  "GET /" should :
    
    when(mockNGRConnector.getLinkedProperty(any[CredId])(any())).thenReturn(Future.successful(Some(property)))
    "return 200" in :
      val result = controller.show(authenticatedFakeRequest)
      status(result) mustBe Status.OK

    "return HTML" in :
      val result = controller.show(authenticatedFakeRequest)
      contentType(result) mustBe Some("text/html")
      charset(result) mustBe Some("utf-8")
      
}
