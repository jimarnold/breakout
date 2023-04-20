libraryDependencies ++= Seq(
  "org.lwjgl" %  "lwjgl" % "3.3.2",
  "org.lwjgl" %  "lwjgl-assimp" % "3.3.2",
  "org.lwjgl" %  "lwjgl-glfw" % "3.3.2",
  "org.lwjgl" %  "lwjgl-openal" % "3.3.2",
  "org.lwjgl" %  "lwjgl-opengl" % "3.3.2",
  "org.lwjgl" %  "lwjgl-stb" % "3.3.2",
  "org.lwjgl" %  "lwjgl" % "3.3.2" classifier "natives-macos-arm64",
  "org.lwjgl" %  "lwjgl-assimp" % "3.3.2" classifier "natives-macos-arm64",
  "org.lwjgl" %  "lwjgl-glfw" % "3.3.2" classifier "natives-macos-arm64",
  "org.lwjgl" %  "lwjgl-openal" % "3.3.2" classifier "natives-macos-arm64",
  "org.lwjgl" %  "lwjgl-opengl" % "3.3.2" classifier "natives-macos-arm64",
  "org.lwjgl" %  "lwjgl-stb" % "3.3.2" classifier "natives-macos-arm64",
  "org.scalatest" %% "scalatest" % "3.2.7" % "test",
)

scalaVersion := "2.13.10"

run / javaOptions := Seq("-XstartOnFirstThread")
fork := true

assembly / assemblyMergeStrategy := {
  case "META-INF/MANIFEST.MF" => MergeStrategy.discard
  case x =>
    MergeStrategy.first
}
