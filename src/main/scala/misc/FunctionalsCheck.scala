package misc

class FunctionalsCheck extends App {

  def q1(x: => Boolean): Boolean = {
    val v: Boolean = x
    v
  }

  def q2(x: String => Boolean): Boolean = {
    !x("Hello")
  }

  def q3empty(x: () => Boolean): Boolean = {
    x()
  }

  def q4StudentCheckOn10(fn: (String, Int) => Boolean): Boolean = {
    fn("Student", 10)
  }

  //executions
  val l: FunctionalsCheck = new FunctionalsCheck()

  println("and it says: " + l.q1(true))

  println(" then " + l.q2((s) => (true)))

  println("Same Function0 as q1, but requires function in the request : " + l.q3empty(() => true))

  println("With higher order function of 2 params:  " + l.q4StudentCheckOn10((a, b) => b / 2 == 0 && a.size > 3))
}


