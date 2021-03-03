package misc

import akka.stream.Outlet

//https://docs.scala-lang.org/tour/self-types.html
trait User {
  def username: String
}

trait Tweeter {
  self: User =>  // reassign this
  def tweet(tweetText: String) = println(s"$username: $tweetText")
  val d = this //Twitter with user
  class Blah {
    val d = this //Blah
    val e = self //Twitter with User
  }
}

class VerifiedTweeter(val username_ : String) extends Tweeter with User {
  def username = s"real $username_"
}

object Classes2 extends App {

  val realBeyoncé = new VerifiedTweeter("Beyoncé")
  realBeyoncé.tweet("Just spilled my glass of lemonade")  // prints "real Beyoncé: Just spilled my glass of lemonade"

  val o: Outlet[Int] = new Outlet[Int]("hi")
  val o1: Outlet[Nothing] = new Outlet("hi")
  val o2: Outlet[Boolean] = Outlet[Boolean]("hello")

//  val o = new OutPort {} can't do since it requires the Outlet mixin

}


//akka streams
abstract class OutPort { self: Outlet[_] =>
  private val outlet: Outlet[_] = this
}

final class Outlet[T](val s: String) extends OutPort {

}
//class OutletTwo extends OutPort {} //can't do either

object Outlet {
  def apply[T](name: String): Outlet[T] = new Outlet[T](name)
  def create[T](name: String): Outlet[T] = Outlet(name)
}
