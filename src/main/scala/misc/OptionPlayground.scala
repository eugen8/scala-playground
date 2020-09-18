package misc

object OptionPlayground extends App {

  case class ApplicationResponse(id: Long, appId: Option[Long], recursiveApp: Option[ApplicationResponse] = None)

  println("map on none: "+ApplicationResponse(120, None).appId.map(x => x))//None
  println("map on some: "+ApplicationResponse(120, Some(42)).appId.map(x => x))//Some(42)

  println("some flatMap on none: "+ApplicationResponse(230, None, Some(ApplicationResponse(120, None))).recursiveApp.flatMap(x => x.appId)   ) //None
  println("some flatMap on some: "+ApplicationResponse(230, None, Some(ApplicationResponse(120, Option(3)))).recursiveApp.flatMap(x => x.appId) ) //Some(3)
  //flatMap gets rid of Option[Option[... and just simply makes it into one Option


  //using get on None
//  println(ApplicationResponse(120, None, None).appId.get )//will cause Exception in thread "main" java.util.NoSuchElementException: None.get
  println(ApplicationResponse(120, None, None).appId.fold(0L)(f=>f) ) //0
  println(ApplicationResponse(120, Some(32l), None).appId.fold(0L)(f=>f) ) //32


}
