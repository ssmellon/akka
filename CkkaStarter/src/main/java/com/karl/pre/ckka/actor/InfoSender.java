package com.karl.pre.ckka.actor;
import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.Address;
import akka.cluster.Cluster;
import static akka.cluster.ClusterEvent.*;
import akka.cluster.ClusterEvent;
import akka.cluster.pubsub.DistributedPubSubMediator;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.routing.BroadcastRoutingLogic;
import akka.routing.Router;
import com.karl.pre.ckka.message.ClusterMessage;

import java.util.ArrayList;

public class InfoSender extends AbstractActor
{
    private final LoggingAdapter log = Logging.getLogger(context().system(), this);
    private Cluster cluster = Cluster.get(getContext().system());

    /**
     * RoundRobinRoutingLogic: 轮询
     * BroadcastRoutingLogic: 广播
     * RandomRoutingLogic: 随机
     * SmallestMailboxRoutingLogic: 空闲
     * */
    private Router router = new Router(new BroadcastRoutingLogic(), new ArrayList<>());

    public InfoSender()
    {
    }

    @Override
    public void preStart()
    {
        cluster.subscribe(self(), initialStateAsEvents(),
                MemberEvent.class, UnreachableMember.class, ClusterEvent.MemberEvent.class,  ReachabilityEvent.class);
    }

    @Override
    public void postStop()
    {
        cluster.unsubscribe(self());
    }

    @Override
    public Receive createReceive()
    {
        return receiveBuilder()
                .match(ClusterEvent.MemberUp.class, message -> {
                    Address address = message.member().address();
                    String memberPath = address + "/user/infoListener";
                    System.out.println("memberPath  " + memberPath);

                    ActorSelection selection = getContext().actorSelection(memberPath);
                    router = router.addRoutee(selection);
                    log.info("ClusterEvent: {}", message);
                })
                .match(ClusterEvent.UnreachableMember.class, mRemoved-> {
                    router = router.removeRoutee(getContext().actorSelection(mRemoved.member().address() +  "/user/infoListener"));
                    log.info("Routee is removed");
                })
                .match(UnreachableMember.class, message -> {
                            log.info("UnreachableMember: {}", message);
                        }
                )
                .match(ReachabilityEvent.class, message -> {
                            log.info("ReachableMember: {}", message.member().address());
                        }
                )
                .match(ClusterMessage.class, message ->
                        {
                            router.route(message.getId(), getSender());
                            log.info("ClusterMessage -------------------- : {}", message.getId());
                        }
                )
                .match(String.class, message -> {
                    log.info("--------000000000000000-----------------message--------  "+ message);
                })
                .match(DistributedPubSubMediator.class, msg ->
                        {
                            log.info("subscribed    "+ msg.toString());
                        }
                )
                .match(DistributedPubSubMediator.SubscribeAck.class, msg ->
                        {
                            log.info("subscribed    "+ msg.toString());
                        }
                )
                .build();
    }

}
