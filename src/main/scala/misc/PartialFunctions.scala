package misc

case class C1(name: String, id: Int)
case class C2(name: String)
case class C3(id: Int)
case class C4(name: String, id: Int)

object PartialFunctions extends App {

//  ????

//  def fun1(): String = {
//    case a : C1 => s"C1 matched ${a.id} = ${a.name}"
//    case b@Some(_:C1) => s"Some of C1 matched: ${b.fold(C1("none",0))(x =>x.asInstanceOf[C1] )}"
//    case b@Some(_:C2) => s"Some of C2 matched: $b"
//  }
//
//  def fun2(): String = {
//    case None => "There is a None"
//  }


  //??? not sure how to use



}
