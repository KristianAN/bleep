package bleep.model

import bleep.internal.EnumCodec
import io.circe.Codec

sealed abstract class ModuleSplitStyleJS(val id: String)

object ModuleSplitStyleJS {
  case object FewestModules extends ModuleSplitStyleJS("FewestModules")

  case object SmallestModules extends ModuleSplitStyleJS("SmallestModules")

  case object SmallModulesFor extends ModuleSplitStyleJS("SmallModulesFor")

  val All: Seq[ModuleSplitStyleJS] = List(FewestModules, SmallestModules, SmallModulesFor)
  implicit val codec: Codec[ModuleSplitStyleJS] =
    EnumCodec.codec(All.map(x => x.id -> x).toMap)
}
