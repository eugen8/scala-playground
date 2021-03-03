package misc


object ContextBound extends App {

  trait Transactional[F] {
    def transactionId: String
  }

  case class CardTransaction(txId: String, number: String)
  case class BankTransaction(transId: String, routing: String, acct: String)

  def logTransaction(t: Transactional[_]): Unit ={
    println(s"TransactionId: ${t.transactionId}")
  }

  //TBD....TODO to complete this



}
