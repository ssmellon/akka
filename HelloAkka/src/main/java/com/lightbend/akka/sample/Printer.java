package com.lightbend.akka.sample;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class Printer extends AbstractActor
{
    private LoggingAdapter logger = Logging.getLogger(getContext().getSystem(), this);

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Message.class, message ->
                {
                    logger.info("Printer   " + message.getInfo());
                    sender().tell(new Message("from Printer"), self());
                })
                .build();
    }

}

