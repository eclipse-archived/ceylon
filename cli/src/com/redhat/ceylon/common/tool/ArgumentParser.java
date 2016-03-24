package com.redhat.ceylon.common.tool;

public interface ArgumentParser<A> {

    public A parse(String argument, Tool tool) throws Exception;
    
}
