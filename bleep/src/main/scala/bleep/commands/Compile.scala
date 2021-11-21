package bleep
package commands

import cats.data.NonEmptyList
import ch.epfl.scala.bsp4j

import scala.build.bloop.BloopServer

case class Compile(started: Started, opts: CommonOpts, projects: Option[NonEmptyList[model.ProjectName]]) extends BleepCommandRemote {
  override def runWithServer(bloop: BloopServer): Unit = {
    val targets = chosenTargets(started, projects)
    val result = bloop.server.buildTargetCompile(new bsp4j.CompileParams(targets)).get()

    result.getStatusCode match {
      case bsp4j.StatusCode.OK        => started.logger.info("Compilation succeeded")
      case bsp4j.StatusCode.ERROR     => started.logger.warn("Compilation failed")
      case bsp4j.StatusCode.CANCELLED => started.logger.warn("Compilation cancelled")
    }
  }
}