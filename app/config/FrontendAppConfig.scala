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

import com.google.inject.{Inject, Singleton}
import config.features.Features
import play.api.Configuration
import play.api.i18n.Lang
import play.api.mvc.RequestHeader
import uk.gov.hmrc.play.bootstrap.config.ServicesConfig

trait AppConfig {
  val appName: String
  val registrationUrl: String
  val dashboardUrl: String
  val ngrPhysicalFrontendUrl: String
  val ngrLogoutUrl: String
  val nextGenerationRatesUrl: String
  val features: Features
  val uploadRedirectTargetBase: String
  val upscanHost: String
  val callbackEndpointTarget: String
  val timeout: Int
  val countdown: Int
  val nextGenerationRatesNotifyUrl: String
}

@Singleton
class FrontendAppConfig @Inject() (configuration: Configuration, sc: ServicesConfig) extends AppConfig {

  val host: String    = configuration.get[String]("host")
  val appName: String = configuration.get[String]("appName")

  private lazy val dashboardHost: String = getString("microservice.services.ngr-dashboard-frontend.host")
  private lazy val registrationHost: String = getString("microservice.services.ngr-login-register-frontend.host")
  private lazy val physicalHost: String = getString("microservice.services.physical-frontend.host")

  val languageTranslationEnabled: Boolean =
    configuration.get[Boolean]("features.welsh-translation")

  def languageMap: Map[String, Lang] = Map(
    "en" -> Lang("en"),
    "cy" -> Lang("cy")
  )

  override val timeout: Int   = configuration.get[Int]("timeout-dialog.timeout")
  override val countdown: Int = configuration.get[Int]("timeout-dialog.countdown")
  val cacheTtl: Long = configuration.get[Int]("mongodb.timeToLiveInSeconds")

  override val registrationUrl: String = s"$registrationHost/ngr-login-register-frontend/register"
  override val ngrPhysicalFrontendUrl: String = s"$physicalHost/ngr-physical-frontend"
  override val dashboardUrl: String = s"$dashboardHost/ngr-dashboard-frontend/dashboard"
  override val ngrLogoutUrl: String = s"$dashboardHost/ngr-dashboard-frontend/signout"
  override val nextGenerationRatesUrl: String = sc.baseUrl("next-generation-rates")
  override val nextGenerationRatesNotifyUrl: String = sc.baseUrl("ngr-notify")
  override val features = new Features()(configuration)
  override val uploadRedirectTargetBase: String = getString("upscan.upload-redirect-target-base")
  override val upscanHost: String = sc.baseUrl("upscan")
  override val callbackEndpointTarget: String = getString("upscan.callback-endpoint")

  def getString(key: String): String =
    configuration.get[String](key)

}
