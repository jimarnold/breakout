libraryDependencies ++= Seq(
  "org.lwjgl" %  "lwjgl" % "3.2.3",
  "org.lwjgl" %  "lwjgl-assimp" % "3.2.3",
  "org.lwjgl" %  "lwjgl-glfw" % "3.2.3",
  "org.lwjgl" %  "lwjgl-openal" % "3.2.3",
  "org.lwjgl" %  "lwjgl-opengl" % "3.2.3",
  "org.lwjgl" %  "lwjgl-stb" % "3.2.3",
  "org.lwjgl" %  "lwjgl" % "3.2.3" classifier "natives-macos",
  "org.lwjgl" %  "lwjgl-assimp" % "3.2.3" classifier "natives-macos",
  "org.lwjgl" %  "lwjgl-glfw" % "3.2.3" classifier "natives-macos",
  "org.lwjgl" %  "lwjgl-openal" % "3.2.3" classifier "natives-macos",
  "org.lwjgl" %  "lwjgl-opengl" % "3.2.3" classifier "natives-macos",
  "org.lwjgl" %  "lwjgl-stb" % "3.2.3" classifier "natives-macos",
  "org.scalatest" %% "scalatest" % "3.2.7" % "test",
)

scalaVersion := "2.11.12"

javaOptions in run := Seq("-XstartOnFirstThread")
fork := true

assemblyMergeStrategy in assembly := {
  case "META-INF/MANIFEST.MF" => MergeStrategy.discard
  case x =>
    MergeStrategy.first
}
