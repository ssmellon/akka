package com.karl.pre.ckka.actor;

import akka.actor.AbstractActor;
import akka.cluster.pubsub.DistributedPubSubMediator;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.karl.pre.ckka.message.ClusterMessage;

public class InfoListener extends AbstractActor
{
    private final LoggingAdapter log = Logging.getLogger(context().system(), this);
    public InfoListener()
    {
    }

    @Override
    public void preStart()
    {
    }

    @Override
    public void postStop()
    {
    }

    @Override
    public Receive createReceive()
    {
        return receiveBuilder()
                .match(String.class, message -> {
                    log.info("--------000000000000000-----------InfoListener------message--------  "+ message);
                })
                .build();
    }

}