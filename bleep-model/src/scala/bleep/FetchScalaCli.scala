package bleep

import coursier.cache.{CacheLogger, FileCache}
import coursier.util.{Artifact, Task}

import java.nio.file.Path
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext}

object FetchSalaCli {
  def apply(cacheLogger: CacheLogger, ec: ExecutionContext, version: String): Path = {

    val fileCache = FileCache[Task]().withLogger(cacheLogger)

    val artifact = Artifact.apply(s"https://github.com/VirtusLab/scala-cli/releases/download/v$version/scala-cli.jar")

    val bin = Await.result(fileCache.file(artifact).run.value(ec), Duration.Inf)

    bin match {
      case Left(err) => throw new BleepException.ArtifactResolveError(err, "scala-cli")
      case Right(file) =>
        file.setExecutable(false)
        file.toPath
    }
  }
}
