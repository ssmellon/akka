package com.lightbend.akka.sample;

import java.io.IOException;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

public class AkkaQuickstart
{
  public static void main(String[] args)
  {
    final ActorSystem system = ActorSystem.create("helloakka");
    try
    {
      final ActorRef printerActor =
              system.actorOf(Props.create(Printer.class), "printerActor");
      final ActorRef howdyGreeter =
              system.actorOf(Props.create(Greeter.class), "howdyGreeter");

      printerActor.tell(new Message("Akka"), howdyGreeter);

      System.out.println(">>> Press ENTER to exit <<<");
    }
    finally
    {
      system.terminate();
    }
  }
}
