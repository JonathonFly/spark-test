package com.ideal.netcare.scalatest.future

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success, Random}

/**
 * Created by syf on 2016/5/25.
 */
object FutureTest1 {
  //咖啡豆
  type CoffeeBeans = String
  //研磨咖啡
  type GroundCoffee = String

  //水
  case class Water(temperature: Int)

  //牛奶
  type Milk = String
  //发泡牛奶
  type FrothedMilk = String
  //浓缩咖啡
  type Espresso = String
  //卡布奇诺
  type Cappuccino = String

  //研磨
  def grind(beans: CoffeeBeans): Future[GroundCoffee] = Future {
    println("start grinding...")
    Thread.sleep(Random.nextInt(2000))
    if (beans == "baked beans") throw GrindingException("joking")
    println("finished grinding...")
    s"ground coffee of $beans"
  }

  //热水
  def heatWater(water: Water): Future[Water] = Future {
    println("start heating water...")
    Thread.sleep(Random.nextInt(2000))
    println("finished heating...")
    water.copy(temperature = 85)
  }

  //打奶泡
  def frothMilk(milk: Milk): Future[FrothedMilk] = Future {
    println("start frothing milk...")
    Thread.sleep(Random.nextInt(2000))
    println("finished frothing...")
    s"frothed $milk"
  }

  //酿造
  def brew(coffee: GroundCoffee, heatedWater: Water): Future[Espresso] = Future {
    println("start brewing now...")
    Thread.sleep(Random.nextInt(2000))
    println("finished brewing...")
    "espresso"
  }

  //混合
  def combine(espresso: Espresso, frothedMilk: FrothedMilk): Cappuccino = "cappuccino"

  //研磨异常
  case class GrindingException(msg: String) extends Exception(msg)

  //发泡异常
  case class ForthingException(msg: String) extends Exception(msg)

  //水沸异常
  case class WaterBoilingException(msg: String) extends Exception(msg)

  //酿造异常
  case class BrewingException(msg: String) extends Exception(msg)

  def prepareCappuccino(): Future[Cappuccino] = {
    val groundCoffee = grind("arabica beans")
    val heatedWater = heatWater(Water(25))
    val frothedMilk = frothMilk("milk")
    for {
      ground <- groundCoffee
      water <- heatedWater
      foam <- frothedMilk
      espresso <- brew(ground, water)
    } yield combine(espresso, foam)
  }

  def main(args: Array[String]) {
    val res = prepareCappuccino()
    Thread.sleep(7000)
    println(res)
  }
}

object FutureTest2 {
  //咖啡豆
  type CoffeeBeans = String
  //研磨咖啡
  type GroundCoffee = String

  //水
  case class Water(temperature: Int)

  def grind(beans: CoffeeBeans): Future[GroundCoffee] = Future {
    println("start grinding...")
    Thread.sleep(Random.nextInt(2000))
    if (beans == "baked beans") throw GrindingException("joking")
    println("finished grinding...")
    s"ground coffee of $beans"
  }

  //热水
  def heatWater(water: Water): Future[Water] = Future {
    println("start heating water...")
    Thread.sleep(Random.nextInt(2000))
    println("finished heating...")
    water.copy(temperature = 85)
  }

  //研磨异常
  case class GrindingException(msg: String) extends Exception(msg)

  def main(args: Array[String]) {
    val res = grind("dfsa beans").onComplete {
      case Success(ground) => println(s"got my $ground")
      case Failure(ex) => println(s"failure, error message:${ex.getMessage}")
    }
    Thread.sleep(5000)

    println()
    println()

    val tempreatureOkay: Future[Boolean] = heatWater(Water(25)) map {
      water => println("in the future!")
        (80 to 85) contains (water.temperature)
    }
    Thread.sleep(3000)
    println(tempreatureOkay)
  }

}

object FutureTest3{
  //咖啡豆
  type CoffeeBeans = String
  //研磨咖啡
  type GroundCoffee = String

  //水
  case class Water(temperature: Int)

  //热水
  def heatWater(water: Water): Future[Water] = Future {
    println("start heating water...")
    Thread.sleep(Random.nextInt(2000))
    println("finished heating...")
    water.copy(temperature = 85)
  }

  def temperatureOkay(water:Water):Future[Boolean]=Future{
    (80 to 85) contains (water.temperature)
  }

  def main(args: Array[String]) {
    val nestedFuture:Future[Future[Boolean]]=heatWater(Water(25)) map {
      water=>temperatureOkay(water)
    }
    Thread.sleep(3000)
    println(nestedFuture)

    println()
    println()

    val flatFuture:Future[Boolean]=heatWater(Water(25)) flatMap{
      water=>temperatureOkay(water)
    }
    Thread.sleep(3000)
    println(flatFuture)

    println()
    println()

    val acceptable:Future[Boolean]=for{
      heatedWater<-heatWater(Water(25))
      okay<-temperatureOkay(heatedWater)
    }yield okay
    Thread.sleep(3000)
    acceptable.foreach(println)
  }
}
