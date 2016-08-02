package com.ideal.netcare.scalatest.multiThread

import java.net.{ServerSocket, Socket}
import java.util.concurrent.{ExecutorService, Executors}

/**
 * Created by root on 2016/5/19.
 */
class NetworkService(port:Int, poolSize:Int) extends Runnable{
  val serverSocket=new ServerSocket(port)
  val pool:ExecutorService = Executors.newFixedThreadPool(2)
  override def run(){
    try {
      while (true) {
        val socket=serverSocket.accept()
        pool.execute(new Handler(socket))
      }
    }
    finally{
      pool.shutdown()
    }
  }
}

class Handler(socket:Socket) extends Runnable{
  def message=(Thread.currentThread().getName()+"\n").getBytes()
  override def run(){
    socket.getOutputStream.write(message)
    socket.getOutputStream.close()
  }
}

object MultiThread{
  def main(args: Array[String]) {
    (new NetworkService(30001,2)).run()
  }
}