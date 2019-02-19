package com.lightbend.akka.sample;

public class Message
{
    private String info;

    public Message()
    {
    }

    public Message(String info)
    {
        this.info = info;
    }

    public String getInfo()
    {
        return info;
    }

    public void setInfo(String info)
    {
        this.info = info;
    }
}
