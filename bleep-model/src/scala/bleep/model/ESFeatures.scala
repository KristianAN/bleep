package bleep.model

import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.Encoder
import io.circe.Decoder

final case class ESFeatures(
    allowBigIntsForLongs: Boolean = false,
    avoidClasses: Boolean = true,
    avoidLetsAndConsts: Boolean = true,
    esVersion: String = ESVersion.default
)

object ESFeatures {
  implicit val encodes: Encoder[ESFeatures] = deriveEncoder
  implicit val decodes: Decoder[ESFeatures] = deriveDecoder
}

object ESVersion {
  val ES5_1 = "ES5_1"
  val ES2015 = "ES2015"
  val ES2016 = "ES2016"
  val ES2017 = "ES2017"
  val ES2018 = "ES2018"
  val ES2019 = "ES2019"
  val ES2020 = "ES2020"
  val ES2021 = "ES2021"

  def default = ES2015
}
