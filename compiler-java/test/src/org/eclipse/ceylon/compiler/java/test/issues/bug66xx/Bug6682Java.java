package org.eclipse.ceylon.compiler.java.test.issues.bug66xx;

import java.util.List;
import java.util.Map;

class Bug6682Java<X>{
    
    Bug6682Java(X x) {}
        
    public <T> T getParameterValueNo1(Bug6682Java<T> param){ return null; }
    public <T> T getParameterValueNo2(Bug6682Java<T> param, T t){ return null; }
    public <T> T getParameterValueNo3(Bug6682Java<? extends T> param){ return null; }
    public <T> T getParameterValueNo4(Bug6682Java<? super T> param){ return null; }

    public <T> Bug6682Java<T> getParameterValue0(Bug6682Java<? extends T> param){ return null; }
    public <T> Bug6682Java<T> getParameterValue1(Bug6682Java<T> param){ return null; }
    public <T> Bug6682Java<? extends T> getParameterValue2(Bug6682Java<T> param){ return null; }    
    public <T> Bug6682Java<? super T> getParameterValue3(Bug6682Java<T> param){ return null; }
    public <T> List<Bug6682Java<T>> getParameterValue4(Bug6682Java<T> param){ return null; }
    public <T> Bug6682Java<List<T>> getParameterValue5(Bug6682Java<T> param){ return null; }
    
    public <T> Bug6682Java<? extends List<T>> getParameterValue6(Bug6682Java<T> param){ return null; }
    public <T> Bug6682Java<? super List<T>> getParameterValue7(Bug6682Java<T> param){ return null; }
    
    public <T,S> Map<T,S> getParameterValues(Bug6682Java<T> p, Bug6682Java<S> q){ return null; }
    
}

class Bug6682List<X> extends Bug6682Java<X>{
    Bug6682List(List<X> list) {
        super(list.get(0));
    }
}