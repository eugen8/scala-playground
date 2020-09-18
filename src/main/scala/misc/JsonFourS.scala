package misc

import org.json4s.JsonDSL.WithBigDecimal._
import org.json4s._
import org.json4s.native.JsonMethods._
import org.json4s.native.Serialization
import org.json4s.native.Serialization.{read, write}

case class SomeJsonFourCase1(name: String, value: Int, significance: Option[String] = None, case2: Option[SomeJsonFourCase2] = None)

case class SomeJsonFourCase2(name: String, value: BigDecimal, significance: Option[String] = None)

class SomeJsonFourRegularClass(var firstName: String, val lastName: String) {
  def say: Unit = println(s"${this.getClass.getSimpleName} says names are: $firstName $lastName")
}

object JsonFourS extends App {
  //https://github.com/json4s/json4s

  def initial: Unit = {
    println(parse(""" { "numbers" : [1, 2, 3, 4] } """))

    val json = List(1, 2, 3)
    println(compact(render(json)))

    println(("hello" -> "23"))
    println(("hello" -> "23" -> 23))
    println((("hello", "23"), 23) == ("hello" -> "23" -> 23))

    val json2 = ("name" -> "joe") ~ ("age" -> 35)
    println(compact(render(json2)))

    implicit val formats = Serialization.formats(NoTypeHints)
    val ser = write(SomeJsonFourCase1("age", 32))

    println(ser)
    println(write(SomeJsonFourCase1("age", 32, significance = Some("small"))))

    val serRead = read[SomeJsonFourCase1](ser)
    println(serRead)

    println("with different ordering of json values it still works: "+ read[SomeJsonFourCase1](
      """{"value":35, "name":"age","significance":"somewhat"}""".stripMargin))

    println("With serializing embedded case classes: " + write(SomeJsonFourCase1("age", 35, Some("somewhat"),
      Some(SomeJsonFourCase2("smileSize", 6.435)))))

    val regClass = new SomeJsonFourRegularClass("Joe", "Boe")
    val regSer = write(regClass)
    println(regSer)
    read[SomeJsonFourRegularClass](regSer).say
  }

  def serializePolimorphicLists: Unit = {
    println("=========== serializePolimorphicLists ===========")
    trait Animal
    case class Dog(name: String) extends Animal
    case class Fish(weight: Double) extends Animal
    case class Animals(animals: List[Animal])

    implicit val formats = Serialization.formats(ShortTypeHints(List(classOf[Dog], classOf[Fish])))

    val ser3 = write(Animals(Dog("pluto") :: Fish(1.2) :: Nil))
    println(ser3)
    //  String = {"animals":[{"jsonClass":"Dog","name":"pluto"},{"jsonClass":"Fish","weight":1.2}]}

    val read3 = read[Animals](ser3)
    //  Animals = Animals(List(Dog(pluto), Fish(1.2)))
    println(read3)
  }

  def extracts: Unit = {

    val a1 = """{"animal_nm":"dog","age":3}"""
    val a2 = """{"animal_nm":"dog","age":3, "name":"Boomer"}"""
    val a3 = """{"age":3,"animal_nm":"dog", "name":"Boomer"}"""

    case class Creature(animal: String, age: Int, name: Option[String])

    class CreatureSerializerV1(implicit formats: Formats) extends CustomSerializer[Creature](format => ( {

      case jObj: JObject =>
        val a = (jObj \ "animal_nm").extract[String]
        val age = (jObj \ "age").extract[Int]
        val name = (jObj \ "name").extractOpt[String]
        Creature(a, age, name)
      //This below is very brittle
      //      case JObject(
      //      JField("animal_nm", JString(a))::
      //      JField("age", JInt(age)) ::
      //      JField("name", JString(n)) :: Nil ) =>
      //        Creature(a, age.toInt, Some(n))
    }, {
      case c: Creature =>
        //        another way to do it is use tuple ~ tuple
        //        ("animal" -> c.animal) ~
        //          ("age" -> c.age)
        JObject(
          JField("animal", c.animal) ::
            JField("age", c.age) ::
            JField("name", c.name) :: Nil

        )
    }
    ))
    val defaultFormats = Serialization.formats(NoTypeHints)
    implicit val fmt1 = defaultFormats + new CreatureSerializerV1()(defaultFormats)

    println(read[Creature](a1)) //Creature(dog,3,None)
    println(read[Creature](a2)) //Creature(dog,3,Some(Boomer))
    println(read[Creature](a3)) //still works with different order Creature(dog,3,Some(Boomer))

    println(write(Creature("cat", 4, None))) //{"animal":"cat","age":4} None doesn't show up in json!

  }

  initial
  serializePolimorphicLists
  //  extracts
}
