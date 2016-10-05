package com.redhat.ceylon.compiler.java.test.issues.bug65xx;

interface Channel{}

interface ChannelListener<X extends Channel>{
}
interface A extends ChannelListener<Channel> {}
interface B extends ChannelListener<Channel> {}

public class Bug6548<X extends Channel> {
    public void set(ChannelListener<? super X> listener){}
    public void set(String str){}
}
