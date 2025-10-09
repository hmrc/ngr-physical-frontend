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

package config

import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.i18n.Lang
import play.api.mvc.{AnyContentAsEmpty, RequestHeader}
import play.api.test.FakeRequest
import play.mvc.Http
import play.test.Helpers
import play.test.Helpers.fakeRequest
import uk.gov.hmrc.http.client.RequestBuilder

class FrontendAppConfigSpec extends AnyFreeSpec with Matchers with GuiceOneAppPerSuite {

  lazy val appConfig: FrontendAppConfig = app.injector.instanceOf[FrontendAppConfig]

  "FrontendAppConfig" - {
    "should load configuration" in {
      appConfig.appName mustBe "ngr-physical-frontend"
      appConfig.host mustBe "http://localhost:1506"
      appConfig.registrationUrl mustBe "http://localhost:1502/ngr-login-register-frontend/register"
      appConfig.ngrPhysicalFrontendUrl mustBe "http://localhost:1506/ngr-physical-frontend"
      appConfig.dashboardUrl mustBe "http://localhost:1503/ngr-dashboard-frontend/dashboard"
      appConfig.ngrLogoutUrl mustBe "http://localhost:1503/ngr-dashboard-frontend/signout"
      appConfig.uploadRedirectTargetBase mustBe "http://localhost:1506"
      appConfig.upscanHost mustBe "http://localhost:9570"
      appConfig.callbackEndpointTarget mustBe "http://localhost:1506/internal/upscan-callback"
    }

    "have languageMap" in {
      appConfig.languageMap mustBe Map(
        "en" -> Lang("en"),
        "cy" -> Lang("cy"))
    }

    "have welsh language enabled" in {
      appConfig.languageTranslationEnabled mustBe true
    }

    "return the correct language map" in {
      appConfig.languageMap mustBe Map("en" -> Lang("en"), "cy" -> Lang("cy"))
    }

    "return the correct timeout values" in {
      appConfig.timeout mustBe 900
      appConfig.countdown mustBe 120
    }

    "return the correct cacheTtl" in {
      appConfig.cacheTtl mustBe 900
    }
    
    "throw an error if the config is missing" in {
      val exception = intercept[com.typesafe.config.ConfigException.Missing] {
        appConfig.getString("non-existent-config")
      }
      exception.getMessage must include("No configuration setting found for key 'non-existent-config'")
    }
  }

}
