package com.lightbend.akka.sample;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;


public class Greeter extends AbstractActor
{
    private LoggingAdapter logger = Logging.getLogger(getContext().getSystem(), this);

    @Override
    public Receive createReceive()
    {
        return receiveBuilder()
                .match(Message.class, message ->
                {
                    logger.info("Greeter   " + message.getInfo());
//                    sender().tell(new Message("from Greeter"), self());
                })
                .build();
    }
}
