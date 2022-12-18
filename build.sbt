import org.scalajs.linker.interface.ModuleSplitStyle

ThisBuild / scalaVersion := "2.13.10"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "com.github.windbird123"
ThisBuild / organizationName := "example"

val projectName = "scalajs-vite"

val publicDev  = taskKey[String]("output directory for `npm run dev`")
val publicProd = taskKey[String]("output directory for `npm run build`")

lazy val root = (project in file("."))
  .enablePlugins(ScalaJSPlugin)
  .settings(
    name := projectName,
    libraryDependencies ++= Seq(
      "org.scala-js" %%% "scalajs-dom" % "2.1.0"
    ),
    scalacOptions ++= Seq("-encoding", "utf-8", "-deprecation", "-feature"),
    scalaJSUseMainModuleInitializer := true,
    scalaJSLinkerConfig ~= {
      _.withModuleKind(ModuleKind.ESModule)
        .withModuleSplitStyle(ModuleSplitStyle.SmallModulesFor(List(projectName)))
    },
    publicDev := linkerOutputDirectory((Compile / fastLinkJS).value).getAbsolutePath,
    publicProd := linkerOutputDirectory((Compile / fullLinkJS).value).getAbsolutePath
  )

def linkerOutputDirectory(v: Attributed[org.scalajs.linker.interface.Report]): File =
  v.get(scalaJSLinkerOutputDirectory.key).getOrElse {
    throw new MessageOnlyException(
      "Linking report was not attributed with output directory. " +
        "Please report this as a Scala.js bug."
    )
  }
