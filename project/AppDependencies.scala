import sbt.*

object AppDependencies {

  private val bootstrapVersion = "9.19.0"
  private val hmrcMongoVersion = "2.7.0"
  private val enumeratumVersion = "1.9.0"

  val compile: Seq[ModuleID] = Seq(
    play.sbt.PlayImport.ws,
    "uk.gov.hmrc"       %% "play-frontend-hmrc-play-30"                         % "12.13.0",
    "uk.gov.hmrc"       %% "bootstrap-frontend-play-30"                         % bootstrapVersion,
    "uk.gov.hmrc.mongo" %% "hmrc-mongo-play-30"                                 % hmrcMongoVersion,
    "uk.gov.hmrc"       %% "play-conditional-form-mapping-play-30"              % "3.3.0",
    "com.beachape"      %% "enumeratum-play"                                    % enumeratumVersion,
    "uk.gov.hmrc"       %% "centralised-authorisation-resource-client-play-30"  % "1.10.0"
  )

  val test: Seq[ModuleID] = Seq(
    "uk.gov.hmrc"             %% "bootstrap-test-play-30"  % bootstrapVersion,
    "uk.gov.hmrc.mongo"       %% "hmrc-mongo-test-play-30" % hmrcMongoVersion,
    "org.scalatestplus"       %% "scalacheck-1-17"         % "3.2.18.0"
  ).map(_ % Test)

  def apply(): Seq[ModuleID] = compile ++ test
}
