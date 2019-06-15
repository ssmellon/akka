package com.karl.pre;

import akka.actor.*;
import akka.cluster.singleton.ClusterSingletonManager;
import akka.cluster.singleton.ClusterSingletonManagerSettings;
import akka.cluster.singleton.ClusterSingletonProxy;
import akka.cluster.singleton.ClusterSingletonProxySettings;
import com.karl.pre.actor.ClusterController;
import com.karl.pre.message.ClusterMessage;

public class ClusterMain
{
    public static void main(String[] args)
    {
        ActorSystem actorSystem = ActorSystem.create("ClusterSystem");

        actorSystem.actorOf(Props.create(com.karl.pre.actor.InfoListener.class), "infoListener");

        actorSystem.actorOf(ClusterSingletonManager.props(
                Props.create(ClusterController.class),
                PoisonPill.getInstance(),
                ClusterSingletonManagerSettings.create(actorSystem)), "clusterController");

//        actorSystem.eventStream().publish(new ClusterMessage("Parov Stelar"));


//        clusterController.tell(new ClusterMessage("ippp****pd"), ActorRef.noSender());

//        clusterController.tell(new ClusterMessage("id"), ActorRef.noSender());
//        final String path = "akka.tcp://ClusterSystem@127.0.0.1:2555/user/clusterController";
//        final ActorSelection destination = actorSystem.actorSelection(path);
//        destination.tell(ClusterEvent.CurrentClusterState,ActorRef.noSender());
//        final ActorRef myPersistentActor = actorSystem.actorOf(destination);
//        destination.tell(ClusterEvent.MemberUp.apply(new Member(null,1, MemberStatus.up(), null)), ActorRef.noSender());

        ActorRef clusterRef = actorSystem.actorOf(
                ClusterSingletonProxy.props(
                        "/user/clusterController",
                        ClusterSingletonProxySettings.create(actorSystem)),
                "clusterControllerSender");

        try
        {
            for(int i = 0 ; i< 360 ; i++)
            {
                Thread.sleep(2000);
                clusterRef.tell(new ClusterMessage("iii----ii" + i), ActorRef.noSender());
            }
        }
        catch(InterruptedException e)
        {
            e.printStackTrace();
        }

    }
}
