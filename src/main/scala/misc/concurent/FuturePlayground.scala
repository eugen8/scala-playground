package misc.concurent

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.language.postfixOps

object FuturePlayground extends App {

  import scala.concurrent.Future

  import scala.concurrent.ExecutionContext.Implicits.global
  import scala.concurrent.blocking

  def someFuture(): Future[String] = {
    println("Starting someFuture")
    val f1 = Future {
      blocking {
        Thread.sleep(500)
        println(s"done thread f1")
        "Done"
      }
    }
    val f2 = Future {
      blocking {
        Thread.sleep(500)
        println(s"done thread f2")
        "Done"
      }
    }

    Future {
      blocking {
        Thread.sleep(990)
        println(s"done thread f3")
        "Done"
      }
    }
    Future {
      blocking {
        Thread.sleep(990)
        println(s"done thread f4")
        "Done"
      }
    }

    f1.flatMap(resF1 => f2.map(resF2 => resF1 +" - "+resF2))
  }

  val f = someFuture()
  println(f)
  Await.result(f, 2000  millis)
  println(f)

}
