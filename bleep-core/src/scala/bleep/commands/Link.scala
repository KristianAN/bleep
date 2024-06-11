package bleep
package commands

import bleep.bsp.BspCommandFailed
import bleep.internal.{DoSourceGen, TransitiveProjects}
import ch.epfl.scala.bsp4j

import bloop.rifle.BuildServer
import bleep.model.ScalaJSLinkerConfig

case class Link(
    project: model.CrossProjectName,
    args: List[String],
    scalaJSLinkerConfig: ScalaJSLinkerConfig,
    watch: Boolean
) extends BleepCommandRemote(watch) {

  override def watchableProjects(started: Started): TransitiveProjects =
    TransitiveProjects(started.build, Array(project))

  override def runWithServer(started: Started, bloop: BuildServer): Either[BleepException, Unit] =
    DoSourceGen(started, bloop, watchableProjects(started)).flatMap { _ =>
      val targets = BleepCommandRemote.buildTargets(started.buildPaths, Array(project))

      val result = bloop.buildTargetCompile(new bsp4j.CompileParams(targets)).get()

      result.getStatusCode match {
        case bsp4j.StatusCode.OK => Right(started.logger.info("Compilation succeeded"))
        // Insert linker action here
        case other => Left(new BspCommandFailed(s"compile", Array(project), BspCommandFailed.StatusCode(other)))
      }
    }
}
