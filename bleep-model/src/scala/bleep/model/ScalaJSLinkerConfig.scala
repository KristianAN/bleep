package bleep.model

import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}

final case class ScalaJSLinkerConfig(
    moduleKind: ModuleKindJS,
    checkIR: Boolean,
    moduleSplitStyle: ModuleSplitStyleJS,
    smallModuleForPackage: List[String],
    esFeatures: ESFeatures,
    jsHeader: Option[String],
    prettyPrint: Boolean,
    relativizeSourceMapBase: Option[String],
    remapEsModuleImportMap: Option[String] // TODO: Should be some path
)

object ScalaJSLinkerConfig {
  implicit val decodes: Decoder[ScalaJSLinkerConfig] = deriveDecoder
  implicit val encodes: Encoder[ScalaJSLinkerConfig] = deriveEncoder
}
