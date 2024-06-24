package bleep
package commands

import bleep.logging.Logger

import java.nio.file.Path

case class ImportScalaCli(
    existingBuild: Option[model.BuildFile],
    sourceDir: Path,
    fetchJvm: FetchJvm,
    destinationPaths: BuildPaths,
    logger: Logger,
    options: scalacliimport.ImportOptions,
    bleepVersion: model.BleepVersion
) extends BleepCommand {
  override def run(): Either[BleepException, Unit] = {
    val resolvedJvm = fetchJvm(model.Jvm.system)
    scalacliimport.runScalaCli(logger, sourceDir, destinationPaths, resolvedJvm, options.sbtPath)

    val inputData = sbtimport.ImportInputData.collectFromFileSystem(destinationPaths, logger)

    FileSync
      .syncStrings(destinationPaths.buildDir, generatedBuildFiles, deleteUnknowns = FileSync.DeleteUnknowns.No, soft = false)
      .log(logger, "Wrote build files")

    Right(())
  }
}
