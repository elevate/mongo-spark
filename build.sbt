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
		"io.argonaut" %% "argonaut" % "6.0.4",
"org.scalaz" %% "scalaz-core" % "7.1.0"
)

retrieveManaged := true

resolvers += "Akka Repository" at "http://repo.akka.io/releases/"


