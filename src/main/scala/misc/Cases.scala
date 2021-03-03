package misc

object Cases extends App {

  def matcherHighToLow(someCreature: Any) = someCreature match {
    case e: Creature => println("matched Creature")
    case e: Human => println("matched Human")
    case e: Student => println("matched student")
  }

  //!!! - that's the correct order to match subtype first
  def matcherLowToHigh(someCreature: Any) = someCreature match {
    case e: Student => println("matched student")
    case e: Human => println("matched Human")
    case e: Creature => println("matched Creature")
  }

  matcherHighToLow(new Human())//matched Creature
  matcherLowToHigh(new Human)//matched Human !!! - that's the correct order to match subtype first
}

