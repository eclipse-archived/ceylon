package com.redhat.ceylon.common.tool;

interface ArgumentParser<A> {

    public A parse(String argument);
    
}
