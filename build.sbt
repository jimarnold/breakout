libraryDependencies ++= Seq(
  "org.lwjgl" %  "lwjgl" % "3.4.1",
  "org.lwjgl" %  "lwjgl-assimp" % "3.4.1",
  "org.lwjgl" %  "lwjgl-glfw" % "3.4.1",
  "org.lwjgl" %  "lwjgl-openal" % "3.4.1",
  "org.lwjgl" %  "lwjgl-opengl" % "3.4.1",
  "org.lwjgl" %  "lwjgl-stb" % "3.4.1",
  "org.lwjgl" %  "lwjgl" % "3.4.1" classifier "natives-macos-arm64",
  "org.lwjgl" %  "lwjgl-assimp" % "3.4.1" classifier "natives-macos-arm64",
  "org.lwjgl" %  "lwjgl-glfw" % "3.4.1" classifier "natives-macos-arm64",
  "org.lwjgl" %  "lwjgl-openal" % "3.4.1" classifier "natives-macos-arm64",
  "org.lwjgl" %  "lwjgl-opengl" % "3.4.1" classifier "natives-macos-arm64",
  "org.lwjgl" %  "lwjgl-stb" % "3.4.1" classifier "natives-macos-arm64",
  "org.scalatest" %% "scalatest" % "3.2.19" % "test",
)

scalaVersion := "3.6.3"

run / javaOptions := Seq("-XstartOnFirstThread", "--enable-native-access=ALL-UNNAMED")
fork := true

assembly / assemblyMergeStrategy := {
  case "META-INF/MANIFEST.MF" => MergeStrategy.discard
  case x =>
    MergeStrategy.first
}
