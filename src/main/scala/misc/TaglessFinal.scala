package misc

import cats.Monad

class TaglessFinal extends App{


  trait Console[F[_]] {
    def putStrLn(str: String): F[Unit]
    def readLn: F[String]
  }

//  def program[F[_]: Monad](implicit C: Console[F]): F[Unit] = {
//    for {
//      _ <- C.putStrLn("Enter your name")
//      n <- C.readLn
//      _ <- C.putStrLn(s"Hello $n!")
//    } yield ()
//  }

}



