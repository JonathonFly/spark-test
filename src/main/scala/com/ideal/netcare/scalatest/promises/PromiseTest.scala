package com.ideal.netcare.scalatest.promises

import scala.concurrent.{Future, Promise}
import scala.util.{Failure, Success}
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Created by syf on 2016/5/25.
 */
object PromiseTest1 {
  case class TaxCut(reduction:Int)

  def redeemCampaignPledge():Future[TaxCut]={
    val p=Promise[TaxCut]()
    Future{
      println("Starting the new legislative period")
      Thread.sleep(2000)
      p.success(TaxCut(20))
      println("We reduced the taxes, You must reelect us.")
    }
    p.future
  }

  def main(args: Array[String]) {
    val taxCutF:Future[TaxCut]=PromiseTest1.redeemCampaignPledge()
    println("now they're elected, let's see if they remember their promise...")
    taxCutF.onComplete{
      case Success(TaxCut(reduction))=>println(s"they really cut the taxes by $reduction")
      case Failure(ex)=>println(s"they broke their promises.")
    }
    Thread.sleep(4000)
  }

}

object PromiseTest2{
  case class TaxCut(reduction:Int)
  case class LameExcuse(msg:String)extends Exception(msg)
  def redeemCampaignPledge():Future[TaxCut]={
    val p=Promise[TaxCut]()
    Future{
      println("starting the new legislative period.")
      Thread.sleep(2000)
      p.failure(LameExcuse("global economy crisis"))
      println("We didn't fill our promises, but surely they'll understand.")
    }
    p.future
  }

  def main(args: Array[String]) {
    val taxCutF:Future[TaxCut]=PromiseTest2.redeemCampaignPledge()
    println("now they're elected, let's see if they remember their promise...")
    taxCutF.onComplete{
      case Success(TaxCut(reduction))=>println(s"they really cut the taxes by $reduction")
      case Failure(ex)=>println(s"they broke their promises.")
    }
    Thread.sleep(4000)
  }
}
