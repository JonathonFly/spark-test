package com.ideal.netcare.scalatest.pattern

/**
 * Created by syf on 2016/5/23.
 */


object PatternTest1 {

  case class Player(name: String, score: Int)
  /*
   *模式匹配，按从上至下的顺序优先匹配，匹配了一个值之后，后续的就不再匹配*/
  def message(player: Player) = player match {
    case Player(_, score) if score > 90 => s"good job !"
    case Player(name, _) => s"hello,$name"
  }

  def main(args: Array[String]) {
    val player: Player = new Player("Tom", 95)
    println(message(player))
  }
}

object PatternTest2 {
  def main(args: Array[String]) {
    def gameResults():Seq[(String,Int)]=("Tom",1000)::("Jonathan",2000)::("Lich",3000)::Nil
    def hallOfFame=for{
      (name,score)<-gameResults()
      if(score>1500)
    }yield name
    println(hallOfFame)
  }
}

object PatternTest3{
  def main (args: Array[String]) {
    val lists=List(1,2,3)::List.empty::List(4,5)::List.empty::List(6)::Nil
    val notEmptySize=for{
      list @ head::_<-lists
    }yield list.size

    print(notEmptySize)
  }
}

object PatternTest4{
  case class Player(name: String, score: Int)
  def main (args: Array[String]) {
    def currentPlayer():Player=Player("Tom",500)
    val Player(name,_)=currentPlayer()
    println(name)
  }
}

object PatternTest5{
  case class Player(name: String, score: Int)
  def main (args: Array[String]) {
    def scores:List[Int]=List(10,12,34,45)
    val best::rest=scores
    println("The score of our champion is "+best)
    println("The rest is "+rest)
  }
}

object PatternTest6{
  case class Player(name: String, score: Int)
  def main (args: Array[String]) {
    def gameResult():(String,Int)=("Tom",1000)

    val result=gameResult()
    println("name = "+result._1+", and score = "+result._2)

    val (name,score)=gameResult()
    println(s"name = $name, and score = $score")
  }
}
