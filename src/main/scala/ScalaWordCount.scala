/*
 * ScalaWordCount.scala
 * Written in 2014 by Sampo Niskanen / Mobile Wellness Solutions MWS Ltd
 * 
 * To the extent possible under law, the author(s) have dedicated all copyright and
 * related and neighboring rights to this software to the public domain worldwide.
 * This software is distributed without any warranty.
 * 
 * See <http://creativecommons.org/publicdomain/zero/1.0/> for full details.
 */
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.hadoop.conf.Configuration
import org.bson.BSONObject
import org.bson.BasicBSONObject
import com.mongodb._

import scalaz._
import Scalaz._

//import com.fasterxml.jackson.module.scala.DefaultScalaModule
//import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper
//import com.fasterxml.jackson.databind.ObjectMapper
//import com.fasterxml.jackson.databind.DeserializationFeature


// import com.mongodb.casbah.conversions.scala._
// import com.mongodb.casbah.commons._
// import com.mongodb.casbah.Imports._
// import com.novus.salat._
// import com.novus.salat.global._
// import com.novus.salat.annotations._

//return companies keyed.

//flatten can be used to return all skills
//return skills keyed

case class SkillTag (
  _id: Option[Int],
  Name: Option[String]
)

case class Experience (
  CompanyName: Option[String],
  Skills: List[SkillTag]
)

case class Profile (
  _id: String,
  EmailAddress: Option[String],
  Experience: List[Experience]
)


object ScalaWordCount {

  val format = new java.text.SimpleDateFormat("dd-MM-yyyy", java.util.Locale.forLanguageTag("en"))

  // def extract(x: Option[MongoDBList]): Array[String] = x match {
  //   case Some(s) => {
      
  //     return s.toArray.map(e => { e.get("CompanyName") });
  //     //format.parse(s).toString

  //   }
  //   case None => Array[String]()
  // }

  def format(x: Option[String]): String = x match {
    case Some(s) => format.parse(s).toString
    case None => "UNKNOWN"
  }

  def getSkillTag(dbo: BSONObject): Option[SkillTag] = {
    None
  }

  def getExperience(dbo: BSONObject): Option[Experience] = {
    None
  }

  //def buildObject(mapper: ObjectMapper, dbo: BSONObject): Option[Profile] = {
  def buildObject(dbo: BSONObject): Option[Profile] = {
    // grater[Profile].asObject(dbo)

    for {
      id <- Option(dbo.get("_id").asInstanceOf[String])
      name = Option(dbo.get("EmailAddress").asInstanceOf[String])
      exp <- Option(dbo.get("Experience").asInstanceOf[BasicDBList])
      ee <- exp.traverse(e => )
    } yield new Profile(id, name, null)

 //   val id = dbo.get("_id").asInstanceOf[String]
   // val name = Option(dbo.get("EmailAddress").asInstanceOf[String])

//    val experience = Option(dbo.get("Experience").asInstanceOf[BasicDBList]) match {
//      case Some(e: BasicDBList) => {
//
//        val list = e.asInstanceOf[List[Object]]
//        var x = list.map((x: BSONObject) => getExperience(x))
//
//        //List[Experience]()
//      }
//      case None => List[Experience]()
//    }




//    Some(new Profile(id, name, experience))
  }

  def main(args: Array[String]) {

    val sc = new SparkContext("local", "Scala Word Count")

    val config = new Configuration()
    config.set("mongo.input.uri", "mongodb://127.0.0.1:27017/elevate.Recruit.Candidate.Profiles")
    config.set("mongo.output.uri", "mongodb://127.0.0.1:27017/elevate.Recruit.Candidate.ProfilesFeatures")
    //config.set("mongo.input.split.create_input_splits", "false")

    // val mapper = new ObjectMapper with ScalaObjectMapper
    // mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    // mapper.registerModule(DefaultScalaModule)

    val mongoRDD = sc.newAPIHadoopRDD(config, classOf[com.mongodb.hadoop.MongoInputFormat], classOf[Object], classOf[BSONObject])

    // Input contains tuples of (ObjectId, BSONObject)
    val countsRDD = mongoRDD.flatMap(arg => {
      //arg._2.get("UpdatedAt").toString
      //extract(arg._2.get("Experience") as BasicDBList)
      //extract(arg._2)

      buildObject(arg._2) match {
        case Some(profile: Profile) => profile.EmailAddress
        case None => "unknown"
      }

      // val profile = buildObject(arg._2)

      // profile.Experience.flatMap(e => e.CompanyName)

      // arg._2.getAs[MongoDBList]("Experience") match {
      //   case e: MongoDBList => extract(Some(e))
      //   case _ => Some(new MongoDBList())
      // }

    })
    .map(word => (word, 1))
    .reduceByKey((a, b) => a + b)
    

    // Output contains tuples of (null, BSONObject) - ObjectId will be generated by Mongo driver if null
    val saveRDD = countsRDD.map((tuple) => {
      var bson = new BasicBSONObject()
      bson.put("date", tuple._1)
      bson.put("count", tuple._2)
      (null, bson)
    })
    
    // Only MongoOutputFormat and config are relevant
    saveRDD.saveAsNewAPIHadoopFile("file:///bogus", classOf[Any], classOf[Any], classOf[com.mongodb.hadoop.MongoOutputFormat[Any, Any]], config)

  }
}
