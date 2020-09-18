package misc.concurent

import scala.concurrent.{Await, Future, blocking}
import scala.util.{Failure, Success}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.language.postfixOps
import scala.concurrent.duration._

// inspired by http://allaboutscala.com/tutorials/chapter-9-beginner-tutorial-using-scala-futures/
object ConcurrencyMain extends App {

  println("Step 1: Define a method which returns a Future")
  def sweetStock(donut: String): Future[Int] = Future {
    // assume some long running database operation
    Thread.sleep(300)
    println("inMethod: checking stock: "+donut)
    10
  }

  println("\nStep 2: Call method which returns a Future")


  println("v1. Using blocking Await.result:")
  val vanillaDonutStock = Await.result(sweetStock("v1. vanilla donut"), 5 seconds)
  println(s"v1. Stock of vanilla donut = $vanillaDonutStock")


  println("v2. Using onComplete (non blocking) ")
  sweetStock("v2.icecream sandwich").onComplete {
    case Success(stock) => println(s"v2.Stock for icecream sandwich = $stock")
    case Failure(e) => println(s"Fv2.ailed to find icecream sandwich stock, exception = $e")
  }

  println("v3.Using map ")
  sweetStock("v3.Thelma's icecream sandwich").map{ vol =>
    println("v3.Stock for Thelma's icecream sandwich is: "+vol)
    vol
  }

  val snookie: Future[Int] = blocking{  //blocking could be left out... ???
    val v = sweetStock("v4. Snookies vanilla cone ")
    Thread.sleep(321) // Success(10) or Future<Not Complete> depending on time
    v
  }
  println("v4. Snookies after blocking" + snookie) //Success(10)


  Thread.sleep(1000)

}
