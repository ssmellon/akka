package com.karl.pre.ckka.config;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.PoisonPill;
import akka.actor.Props;
import akka.cluster.singleton.ClusterSingletonManager;
import akka.cluster.singleton.ClusterSingletonManagerSettings;
import akka.cluster.singleton.ClusterSingletonProxy;
import akka.cluster.singleton.ClusterSingletonProxySettings;
import com.karl.pre.ckka.actor.InfoListener;
import com.karl.pre.ckka.actor.InfoSender;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CkkaConfiguration
{
    @Value("${ckka.pointPort}")
    private int port;

    @Value("${ckka.seedInfo}")
    private String seedInfo;

    @Bean
    public ActorSystem actorSystem(Config akkaConfig)
    {
        ActorSystem actorSystem = ActorSystem.create("ClusterSystem", akkaConfig);
        actorSystem.actorOf(Props.create(InfoListener.class), "infoListener");
        actorSystem.actorOf(ClusterSingletonManager.props(
                Props.create(InfoSender.class),
                PoisonPill.getInstance(),
                ClusterSingletonManagerSettings.create(actorSystem)), "infoSender");
        return actorSystem;
    }

    @Bean
    public Config akkaConfiguration()
    {
        String basicPort = "akka.remote.netty.tcp.port=" + port
                + "\n" +
        "akka.cluster.seed-nodes = [\"akka.tcp://ClusterSystem@" + seedInfo + "\"]";
        return ConfigFactory.parseString(basicPort).withFallback(
                ConfigFactory.load());
    }

    @Bean("infoSender")
    ActorRef infoSender(ActorSystem actorSystem)
    {
        return actorSystem.actorOf(
                ClusterSingletonProxy.props(
                        "/user/infoSender",
                        ClusterSingletonProxySettings.create(actorSystem)),
                "clusterSender");
    }
}
