package misc

import scala.concurrent.Future
import scala.util.{Failure, Success}

import scala.concurrent.ExecutionContext.Implicits.global

object Futures extends App {
  andThen()

  private def andThen() = {

    val f = Future { 5 }
    f andThen {
      case r => {
        println(" Inside first andThen")
        sys.error("runtime exception")
      }
    } andThen {
      case Failure(t) => println("print failure" + t)
      case Success(v) => println(" print success: " + v)
    }
  }

}

