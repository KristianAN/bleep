package bleep

import coursier.cache.shaded.dirs.dev.dirs.ProjectDirectories

import java.nio.file.{Path, Paths}

case class UserPaths(cacheDir: Path, configDir: Path) {
  val bspSocketDir = cacheDir / "bsp-socket"
  val bspSocketShared = bspSocketDir / "shared"
  val coursierRepositoriesJson = configDir / "coursier-repositories.json"
  val bloopCompileServerJson = configDir / "config.json"
}

object UserPaths {
  def fromAppDirs: UserPaths = {
    val dirs = ProjectDirectories.from("no", "arktekk", "bleep")
    val cacheDir = Paths.get(dirs.cacheDir)
    val configDir = Paths.get(dirs.configDir)

    UserPaths(cacheDir, configDir)
  }
}