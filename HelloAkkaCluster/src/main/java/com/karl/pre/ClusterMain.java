package com.karl.pre;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.PoisonPill;
import akka.actor.Props;
import akka.cluster.singleton.ClusterSingletonManager;
import akka.cluster.singleton.ClusterSingletonManagerSettings;
import akka.cluster.singleton.ClusterSingletonProxy;
import akka.cluster.singleton.ClusterSingletonProxySettings;
import com.karl.pre.actor.InfoSender;
import com.karl.pre.message.ClusterMessage;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class ClusterMain
{
    public static void main(String[] args)
    {
        Config config = ConfigFactory.load();
//        ConfigFactory.parseString(
//                "akka.remote.netty.tcp.port=2555").withFallback(
//                ConfigFactory.load());

        ActorSystem actorSystem = ActorSystem.create("ClusterSystem", config);

//        ActorRef clusterRef = actorSystem.actorOf(Props.create(ClusterController.class), "clusterController");
        ActorRef infoListener =  actorSystem.actorOf(Props.create(com.karl.pre.actor.InfoListener.class), "infoListener");

        final ClusterSingletonManagerSettings settings =
                ClusterSingletonManagerSettings.create(actorSystem);

        actorSystem.actorOf(ClusterSingletonManager.props(
                Props.create(InfoSender.class),
                PoisonPill.getInstance(),
                ClusterSingletonManagerSettings.create(actorSystem)), "infoSender");


        ActorRef clusterRef = actorSystem.actorOf(
                ClusterSingletonProxy.props(
                        "/user/infoSender",
                        ClusterSingletonProxySettings.create(actorSystem)),
                "clusterControllerSender");

        try
        {
            for(int i = 0 ; i< 360 ; i++)
            {
                Thread.sleep(2000);
                clusterRef.tell(new ClusterMessage("iiiii" + i), ActorRef.noSender());
            }
        }
        catch(InterruptedException e)
        {
            e.printStackTrace();
        }

//        ShardRegion.MessageExtractor messageExtractor =
//                new ShardRegion.MessageExtractor() {
//
//                    @Override
//                    public String entityId(Object message) {
//                        if (message instanceof ClusterMessage)
//                            return ((ClusterMessage) message).getId();
//                        return null;
//                    }
//
//                    @Override
//                    public Object entityMessage(Object message) {
//                        if (message instanceof ClusterMessage)
//                            return message;
//                        return message;
//                    }
//
//                    @Override
//                    public String shardId(Object message) {
//                        if (message instanceof ClusterMessage)
//                            return ((ClusterMessage) message).getId();
//                        return null;
//                    }
//                };
//
//        ActorRef startedCounterRegion = ClusterSharding.get(system).start("clusterController", Props.create(ClusterController.class),
//                messageExtractor);
//
//        ActorRef counterRegion = ClusterSharding.get(system).shardRegion("clusterController");
//
//        counterRegion.tell(new ClusterMessage("ip--------------pppd"), ActorRef.noSender());
//
//
//
//        final ClusterSingletonManagerSettings settings =
//                ClusterSingletonManagerSettings.create(system).withRole("worker");

    }
}
