package learn.akka.stream

import akka.Done
import akka.actor.{ActorRef, ActorSystem}
import akka.stream.scaladsl._
import akka.stream.testkit.scaladsl.{TestSink, TestSource}
import akka.stream.{CompletionStrategy, OverflowStrategy}
import akka.testkit.TestProbe
import org.scalatest.{BeforeAndAfter, BeforeAndAfterAll}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, Future}

/**
 * examples adapted from https://doc.akka.io/docs/akka/current/stream/stream-testkit.html
 * */
class SimpleStream1Spec extends AnyFlatSpec with Matchers  with BeforeAndAfterAll {
  implicit val system: ActorSystem = ActorSystem()

  override protected def afterAll(): Unit = {
    system.terminate()
    ()
  }

  "sink" should "work through flow" in {
    val sinkUnderTest = Flow[Int].map(_ * 2).toMat(Sink.fold(0)(_ + _))(Keep.right)

    val range = 1 to 4
    val future = Source(range).runWith(sinkUnderTest)
    val result = Await.result(future, 3.seconds)
    assert(result == 20)
  }

  "source" should "take to produce an infinite stream of elements" in {
    import scala.language.postfixOps

    //a source what will continually emmit the given element
    val sourceUnderTest = Source.repeat(1).map(_ * 2)
    val future = sourceUnderTest.take(10).runWith(Sink.seq)
    val result = Await.result(future, 2 seconds)
    result shouldBe Seq.fill(10)(2) //result = Vector(2, 2, 2, 2, 2, 2, 2, 2, 2, 2)
  }

  "flow" should "takeWhile" in {
    val flowUnderTest = Flow[Int].takeWhile(_ < 5)
    val future = Source(1 to 10).via(flowUnderTest).runWith(Sink.fold(Seq.empty[Int])(_ :+ _)) //append all together
    val result = Await.result(future, 3.seconds)
    //    println(result) //List(1, 2, 3, 4)
    assert(result == (1 to 4))
  }

  it should "takeWhile using Sink.seq" in {
    val flowUnderTest = Flow[Int].takeWhile(_ < 5)
    val future = Source(1 to 10).via(flowUnderTest).runWith(Sink.seq) //append all together
    val result = Await.result(future, 3.seconds)
    //    println(result) //Vector(1, 2, 3, 4)
    assert(result == (1 to 4))
  }

  behavior of "TestKit"
  it should "materialize to Future and pipe results to a probe" in {
    import akka.pattern.pipe
    import system.dispatcher

    val sourceUnderTest = Source(1 to 4).grouped(2) //will generate Vector(Vector(1, 2), Vector(3, 4))
    val probe = TestProbe()
    val actorRef: ActorRef = probe.ref // ref:ActorRef Shorthand to get the testActor.

    sourceUnderTest.runWith(Sink.seq).pipeTo(actorRef)
    //equivalent to:
    sourceUnderTest.toMat(Sink.seq)(Keep.right).run().pipeTo(actorRef)

    probe.expectMsg(3.seconds, Seq(Seq(1, 2), Seq(3, 4)))
  }

  it should "allow control over received elemnts with Sink.acotrRef and complete stream" in {
    case object Tick
    val sourceUnderTest = Source.tick(0.seconds, 200.millis, Tick)

    val probe = TestProbe()
    val cancellableStream = sourceUnderTest
      .to(Sink.actorRef(probe.ref, onCompleteMessage = "completed", onFailureMessage = _ => "failed"))
      .run

    //    val cancellableStream = sourceUnderTest.runWith(Sink.actorRef(...)) //won't work
    probe.expectMsg(1.seconds, Tick)
    probe.expectNoMessage(100.millis)
    probe.expectMsg(3.seconds, Tick)
    cancellableStream.cancel()
    probe.expectMsg(3.seconds, "completed")
  }

  it should "allow control over elements to be sent with Source.actorRef" in {
    val sinkUnderTest = Flow[Int].map(_.toString).toMat(Sink.fold("")(_ + _))(Keep.right)
    //equivalent
    //    val sinkUnderTest = Sink.fold[String, Int]("")((acc, nextVal) => acc + nextVal.toString)

    val (ref, future) = Source
      .actorRef(
        completionMatcher = {
          case Done =>
            CompletionStrategy.draining
        },
        // Never fail the stream because of a message:
        failureMatcher = PartialFunction.empty,
        bufferSize = 8,
        overflowStrategy = OverflowStrategy.fail)
      .toMat(sinkUnderTest)(Keep.both)
      .run()

    ref ! 1
    ref ! 2
    ref ! 3
    ref ! Done

    val result = Await.result(future, 3.seconds)
    assert(result == "123")
  }

  behavior of "Streams TestKit"
  it should "allow control over demand & assertions over elements coming downstream with sink returned by TestSink.probe " in {
    val sourceUnderTest = Source(1 to 4).filter(_ % 2 == 0).map(_ * 2)
    sourceUnderTest.runWith(TestSink[Int]()).request(2).expectNext(4, 8).expectComplete()
  }

  it should "assert demand or control when stream is completed or ended with an error with TestSource" in {
    val sinkUnderTest = Sink.cancelled

    TestSource.probe[Int].toMat(sinkUnderTest)(Keep.left).run().expectCancellation()
  }

  it should "allow injecting exceptions" in {
    import akka.pattern
    import system.dispatcher

    //flowUnderTest: Flow[Int, Int, NotUsed] (Flow[-In, +Out, +Mat]
    val flowUnderTest = Flow[Int].mapAsyncUnordered(2) { sleep =>
      pattern.after(10.millis * sleep, using = system.scheduler)(Future.successful(sleep))
    }

    //pub: TestPublisher.probe[Int], sub: TestSubscriber.probe[Int]
    val (pub, sub) = TestSource.probe[Int].via(flowUnderTest).toMat(TestSink[Int]())(Keep.both).run()

    //No events will be sent by a Publisher until demand is signaled via this method.
    sub.request(n = 3) //requests next 3 elements

    pub.sendNext(3)
    pub.sendNext(2)
    pub.sendNext(1)
    sub.expectNextUnordered(1, 2, 3)

    pub.sendError(new Exception("Power surge in the linear subroutine C-47!"))
    val ex = sub.expectError()
    assert(ex.getMessage.contains("C-47"))
  }

}
