package bleep
package scalacliimport

import bleep.internal.{BleepTemplateLogger, GeneratedFilesScript}
import bleep.logging.Logger
import bleep.rewrites.{normalizeBuild, Defaults}
import bleep.templates.templatesInfer

import java.nio.file.Path

object generateBuild {
  def apply(
      sbtBuildDir: Path,
      destinationPaths: BuildPaths,
      logger: Logger,
      options: ImportOptions,
      bleepVersion: model.BleepVersion,
      inputData: ImportInputData,
      bleepTasksVersion: model.BleepVersion,
      maybeExistingBuildFile: Option[model.BuildFile]
  ): Map[Path, String] = {

    val build0 = buildFromBloopFiles(logger, sbtBuildDir, destinationPaths, inputData, bleepVersion)
    val normalizedBuild = normalizeBuild(build0)

    val buildFile = templatesInfer(new BleepTemplateLogger(logger), normalizedBuild, options.ignoreWhenInferringTemplates)

    val buildFile1 =
      maybeExistingBuildFile match {
        case Some(existingBuild) => buildFile.copy(scripts = existingBuild.scripts)
        case None                => buildFile
      }

    // complain if we have done illegal rewrites during templating
    model.Build.diffProjects(Defaults.add(normalizedBuild), model.Build.FileBacked(buildFile1).dropBuildFile.dropTemplates) match {
      case empty if empty.isEmpty => ()
      case diffs =>
        logger.error("Project templating did illegal rewrites. Please report this as a bug")
        diffs.foreach { case (projectName, msg) => logger.withContext(projectName).error(msg) }
    }

    logger.info(s"Imported ${build0.explodedProjects.size} cross targets for ${buildFile1.projects.value.size} projects")

    val scriptsPkg = List("scripts")
  }
}
