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
  override val ngrDashboardUrl: String = "http://localhost:1503/ngr-dashboard-frontend/dashboard"
  override val ngrLogoutUrl: String = "http://localhost:1503/ngr-dashboard-frontend/signout"
  override val nextGenerationRatesUrl: String = ""
  override val ngrLoginUrl: String = "http://localhost:1502/ngr-login-register-frontend/register"
}
