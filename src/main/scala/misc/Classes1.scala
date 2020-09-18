package misc

case class Foo[A, B](f: A => B) {
  println(s"inside Foo, f is: $f")

  override def toString: String = s"This is Foo with param = $f"
}

case class Bar(name: String) {
  println(s"constructing a Bar named: $name")
}

object Classes1 extends App {

  case class Human(name: String, dob: String)

  val h1 = Human("John", "3/2/2010")
  println(Human.unapply(h1)) //Some((John,3/2/2010))

  println(Human.tupled(("Eugen", "1/1/1000"))) //Human(Eugen,1/1/1000)

  val foo1 = Foo((x: Int) => s"Hello, are you aged $x?")
  val foo2 = Foo((x: Int) => s"Hello, are you aged $x?")
  val foo3 = Foo((x: Int) => s"Hello, We are aged: $x?")

  println(foo1)
  println(foo2)
  println(foo3)
  println("is foo1 == foo2? " + (foo1 == foo2))

  def fun1(v: Int) = s"Hello fun1 is number $v"

  val foo4 = Foo(fun1)
  val foo5 = Foo(fun1)
  println("printing foo4: " + foo4)
  println("printing foo5: " + foo5)
  println("is foo4 == foo5? " + (foo4 == foo5)) //this will be false

  val bar1 = Bar("mars")
  val bar2 = Bar("mars")
  println("is bar1 == bar2? " + (bar1 == bar2))

  val foo6 = Foo { x: String => x.toInt }
  val myGreatCode = (x: String) => x.toInt
  println("where myGreatCode is used")
  val foo7 = Foo(myGreatCode)
  val foo8 = Foo {
    myGreatCode
  }
  println("Is foo7 == foo8? " + (foo7 == foo8)) // This will be true (unlike when the parameter was a function defined with def)

}
