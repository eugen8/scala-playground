package misc

import scala.collection.mutable

object CollisionSimulation extends App {

  val range = 1000000000L //1 billion
  val filledCollectionSize = 10000000L //10 million
  val nextManyNumbers = 100

  val r = scala.util.Random
  def nextLong = (r.nextFloat() * range).toLong
  val md = new mutable.HashMap[Long, Long]()
  var x = 0l
  for ( x <- 1l to filledCollectionSize) {
    var nextInt = nextLong
    while(md.get(nextInt).isDefined){
      nextInt = nextLong
    }
    md.put(nextInt, x)
  }

  println("size of map is: "+md.size)
  val collisions = 1 to nextManyNumbers map { x =>
    md.get((r.nextFloat() * range).toInt)
  }
  println(s"On a collection of $filledCollectionSize random elements in range 1 to $range the next $nextManyNumbers collide as follows:")
  println("Collisions : "+collisions.filter(_.isDefined).size)
  println("Unique: "+collisions.filter(_.isEmpty).size)

}
/*
* Example output:
size of map is: 10000000
On a collection of 10000000 random elements in range 1 to 1000000000 the next 100 collide as follows:
Collisions : 59
Unique: 41
* */