package bleep
package scalacliimport

import bleep.internal.conversions
import bleep.logging.Logger
import bleep.nosbt.librarymanagement
import bloop.config.Config
import coursier.core.{Classifier, Configuration, Extension, Module, ModuleName, Organization, Publication, Type}
import org.typelevel.sbt.tpolecat.{DevMode, TpolecatPlugin}

import java.net.URI
import java.nio.file.{Path, Paths}
import scala.jdk.CollectionConverters.IteratorHasAsScala
object buildFromBloopFiles {

  val includeTestFramewrok: Set[String] =

    Config.Test.defaultConfiguration.frameworks.flatMap(_.names).toSet

  // we use this to remove last directory name in cross projects
  val crossProjectDirNames: Set[String] =
    Set("jvm", "js", "native").flatMap(str => List(str, s".$str"))


  private case class Sources(
      sourceLayout: model.SourceLayout,
      sources: model.JsonSet[RelPath],
      resources: model.JsonSet[RelPath]
  )

  def apply(
      logger: Logger,
      sourcesPath: Path,
      destinationPaths: BuildPaths,
      inputProjects: ImportInputData,
      bleepVersion: model.BleepVersion
  ): model.Build.Exploded = {
}
