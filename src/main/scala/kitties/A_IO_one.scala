package kitties
//https://typelevel.org/cats-effect/docs/2.x/datatypes/io
import cats.effect.IO
import cats.effect.unsafe.implicits.global

object A_IO_one extends App {
  val ioa = IO {println("Hey!")}

  val program: IO[Unit] =
    for {
      _ <- ioa
      _ <- ioa
    } yield ()

  program.unsafeRunSync() //requires import cats.effect.unsafe.implicits.global
  ()
}
