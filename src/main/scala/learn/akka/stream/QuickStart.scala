package learn.akka.stream

import java.nio.file.Paths

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream._
import akka.stream.scaladsl._
import akka.util.ByteString

import scala.concurrent._

/**
 * https://doc.akka.io/docs/akka/current/stream/stream-quickstart.html
 */
class QuickStart {

  implicit val system: ActorSystem = ActorSystem("QuickStart")
  implicit val ec = system.dispatcher

  def ex1() = {
    val source: Source[Int, NotUsed] = Source(1 to 100)
    //  source.runForeach(i => println(i)) ///equivalent to:
    //  source.runWith(Sink.foreach(println))
    val done = source.toMat(Sink.foreach(println))(Keep.right).run()
    done.onComplete(_ => system.terminate())
  }

  def ex2Files() = {
    val source: Source[Int, NotUsed] = Source(1 to 100)
    // source.scan -> Similar to fold but is not a terminal operation,
    val factorials = source.scan(BigInt(1))((acc, next) => acc * next)

    val result: Future[IOResult] = factorials.map(num => ByteString(s"$num\n"))
      //      .map { x => // prints x is: ByteString(49, 10) \n x is: ByteString(49, 10) \n ....
      //          println(s"x is: ${x}")
      //          x
      //      }
      .mapAsync (1){ x =>
        println(s"x is $x")
        Future.successful(x)
      }
      .runWith(FileIO.toPath(Paths.get("factorials.txt")))

    result.onComplete(_ => system.terminate())
  }

}

object QuickStart extends App {
  val qs = new QuickStart
  //  qs.ex1()
  qs.ex2Files()

}
