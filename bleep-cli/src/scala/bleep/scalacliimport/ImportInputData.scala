package bleep
package scalacliimport

import java.nio.file.Path
import bloop.config.Config

case class ImportInputData(
    bloopFileStrings: Vector[(Path, String)]
)
