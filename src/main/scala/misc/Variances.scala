package misc

object Variances extends App {

  val aHuman: Option[Human] = Option(new Human())
  val aNoHuman: Option[Human] = None

  val a1: Creature = aHuman.getOrElse(new Creature())
  val a2: Human = aHuman.getOrElse(new Student()) //Student parameter is a subtype of A so it goes to A as it is bounded by Avvvcdeucbgedhvehtuhjjbiftvcnrgrdvvbvhuijirhn

  val creaOpt: Option[Creature] = Some(new Creature())
  val creaOpt1: Option[Human] = Some(new Human())
  val creaOpt2: Option[Student] = Some(new Student())

  println(creaOpt1.getClass)

  def x[A <: Option[Human]](a: A): Option[Human] = a //a being a child of Option[Human], it is ok for it to be a subclass, e.g. Student that's also to Human
  //  x(creaOpt)//parameter needs to be a subclass of Option[Human], and Option[Creature] is not
  x(creaOpt1) //Option[Human]
  x(creaOpt2) //Option[Student]
  def xcontra[A >: Option[Human]](a: A): Option[Human] = { //can't return "a" as A is a superclass(parent) of Option[Human] , so it can't be e.g. Creature that might not be Human
    a.asInstanceOf[Option[Human]]
  }

  xcontra(creaOpt)
  xcontra(creaOpt1)
  xcontra(creaOpt2)

  val v: Seq[Int] = 1 to 10 //this is infix notation
  println(v.toList) //List(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
  //  println( v toList) //won't work if postfix operator notation not enabled and not recommended; https://docs.scala-lang.org/style/method-invocation.html#postfix-notation

  def xl[T <: Human](a: T): T = {println(s"sub human = ${a}"); a}
//  xl(a1) //needs to be a subclass
  xl(a2)
  val s1 = new Student
  xl(s1)

}
