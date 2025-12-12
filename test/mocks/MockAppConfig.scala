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

package mocks

import play.api.Configuration
import config.AppConfig
import config.features.Features

class MockAppConfig(runModeConfiguration: Configuration) extends AppConfig {
  
  override val features: Features = new Features()(runModeConfiguration)
  override val ngrLogoutUrl: String = "http://localhost:1503/ngr-dashboard-frontend/signout"
  override val nextGenerationRatesUrl: String = "https://localhost:1500"
  override val registrationUrl: String = "http://localhost:1502/ngr-login-register-frontend/register"
  override val dashboardUrl: String = "http://localhost:1503/ngr-dashboard-frontend/dashboard"
  override val upscanHost: String = "http://localhost:9570"
  override val timeout: Int = 900
  override val countdown: Int = 200

  override val ngrPhysicalFrontendUrl: String = "http://localhost:1506"
  override val callbackEndpointTarget: String = "http://localhost:1506/internal/callback-from-upscan"
  override val uploadRedirectTargetBase: String = "http://localhost:1504"
  override val nextGenerationRatesNotifyUrl: String = "http://localhost:1515"
  override val appName: String = "ngr-physical-frontend"
}
