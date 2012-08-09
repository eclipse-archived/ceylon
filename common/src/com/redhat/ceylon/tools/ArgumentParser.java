package com.redhat.ceylon.tools;

interface ArgumentParser<A> {

    public A parse(String argument);
    
}
