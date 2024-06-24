package bleep
package scalacliimport

import bleep.internal.FileUtils
import bleep.logging.Logger

import scala.concurrent.ExecutionContext

object runScalaCli {

  def apply(logger: Logger, cwd: Path, sourcePath: Path, destinationPaths: BuildPaths, jvm: ResolvedJvm, providedScalaCliPath: Option[String]): Unit = {

    val version = "1.3.0" // Can this be read from the files, maybe?

    val scalaCliPath = providedScalaCliPath.getOrElse {
      FetchSalaCli(new BleepCacheLogger(logger), ExecutionContext.global, version)
    }

    FileUtils.deleteDirectory(destinationPaths.bleepImportDir)

    val scalaCli: List[String] =
      List(
        scalaCliPath.toString,
        "--power",
        "export",
        sourcePath.toAbsolutePath.toString,
        "--json",
        s"--output=${destinationPaths.bleepImportScalaCliExportDir}"
      )

    cli(
      action = "scala-cli export",
      cwd = cwd,
      cmd = scalaCli,
      logger = logger,
      out = cli.Out.ViaLogger(logger),
      in = cli.In.No
    )

    ()
  }

}
