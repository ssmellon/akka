package com.karl.pre.actor;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.Address;
import akka.cluster.Cluster;
import akka.cluster.ClusterEvent;
import akka.cluster.pubsub.DistributedPubSub;
import akka.cluster.pubsub.DistributedPubSubMediator;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.routing.BroadcastRoutingLogic;
import akka.routing.Router;
import com.karl.pre.message.ClusterMessage;

import java.util.ArrayList;

import static akka.cluster.ClusterEvent.initialStateAsEvents;

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
                .match(ClusterMessage.class, message ->
                        {
                            log.info("ClusterMessage ----InfoListener---------------- : {}", message.getId());
                        }
                )
                .match(DistributedPubSubMediator.class, msg ->
                        {
                            log.info("subscribed    "+ msg.toString());
                        }
                )
                .build();
    }

}