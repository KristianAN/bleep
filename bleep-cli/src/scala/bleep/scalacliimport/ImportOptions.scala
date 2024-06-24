package bleep
package scalacliimport

import bleep.model.Jvm
import cats.data.{Validated, ValidatedNel}
import cats.syntax.apply.*
import com.monovore.decline.{Argument, Opts}

case class ImportOptions(
    jvm: model.Jvm,
    scalaCliPath: Option[String]
)

object ImportOptions {

  implicit val jvmArgument: Argument[model.Jvm] = new Argument[Jvm] {
    override def read(string: String): ValidatedNel[String, Jvm] = Validated.Valid(model.Jvm(string, None))
    override def defaultMetavar: String = "metavar-jvm"
  }

  val jvm: Opts[model.Jvm] =
    Opts
      .flagOption("jvm", "pick JVM to use for import. Valid nam in index file at https://github.com/coursier/jvm-index/raw/master/index.json")
      .withDefault(Some(model.Jvm.system))
      .map(_.get)

  val scalaCliPath: Opts[Option[String]] =
    Opts
      .option[String]("sbt-path", "optional path to sbt executable if sbt provided by coursier can not be used.")
      .orNone

  val opts: Opts[ImportOptions] =
    (jvm, scalaCliPath).mapN(ImportOptions.apply)
}
