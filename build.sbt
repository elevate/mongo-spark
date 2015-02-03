name := "mongo-spark"

version := "1.0"

scalaVersion := "2.10.3"

//	"org.mongodb" %% "casbah" % "2.8.0",
//	"com.novus" %% "salat" % "1.9.9",
//	"com.typesafe.play" %% "play-json" % "2.3.0"

libraryDependencies ++= Seq(
	"org.apache.spark" %% "spark-core" % "1.2.0",
	"org.apache.hadoop" % "hadoop-client" % "2.2.0",
	"org.mongodb" % "mongo-java-driver" % "2.11.4",
	"com.fasterxml.jackson.module" % "jackson-module-scala_2.10" % "2.3.3",
    "com.fasterxml.jackson.core" % "jackson-core" % "2.3.3",
    "com.fasterxml.jackson.core" % "jackson-databind" % "2.3.3",
    "com.fasterxml.jackson.core" % "jackson-annotations" % "2.3.3",
		"io.argonaut" %% "argonaut" % "6.0.4"

)

retrieveManaged := true

resolvers += "Akka Repository" at "http://repo.akka.io/releases/"


