package sample.stream

import scala.concurrent.duration._
import scala.util.Failure
import scala.util.Success
import akka.actor.ActorSystem
import akka.stream.FlowMaterializer
import akka.stream.MaterializerSettings
import akka.stream.scaladsl.Flow

object BasicTransformation {
  def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem("Sys")

    val text =
      """|Lorem Ipsum is simply dummy text of the printing and typesetting industry.
         |Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, 
         |when an unknown printer took a galley of type and scrambled it to make a type 
         |specimen book.""".stripMargin

    Flow(text.split("\\s").toVector).
      // transform
      map(line => line.toUpperCase).
      // print to console
      foreach(tranformedLine => println(tranformedLine)).
      onComplete(FlowMaterializer(MaterializerSettings())) {
        case Success(_) => system.shutdown()
        case Failure(e) =>
          println("Failure: " + e.getMessage)
          system.shutdown()
      }

  }
}