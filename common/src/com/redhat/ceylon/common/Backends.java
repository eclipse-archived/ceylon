package com.redhat.ceylon.common;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Backends implements Iterable<Backend>, BackendSupport {
    private final Set<Backend> backends;
    
    public final static Backends NONE = new Backends(Collections.<Backend>emptySet());
    
    private static final Map<String, Backends> collections;
    
    static {
        collections = new HashMap<String, Backends>();
    }
    
    public Backends(Set<Backend> backends) {
        this.backends = backends;
    }

    @Override
    public Set<String> getSupportedBackends() {
        return null;
    }

    public boolean none() {
        return backends.isEmpty();
    }
    
    public boolean supports(Backends backends) {
        return !supported(backends).none();
    }
    
    public Backends supported(Backends backends) {
        if (this.backends.containsAll(backends.backends)) {
            return backends;
        } else {
            HashSet<Backend> result = new HashSet<Backend>(backends.backends);
            result.retainAll(this.backends);
            if (result.isEmpty()) {
                return NONE;
            } else {
                return new Backends(result);
            }
        }
    }

    @Override
    public Iterator<Backend> iterator() {
        return backends.iterator();
    }

    public static Backends fromAnnotations(Collection<String> backends) {
        if (backends == null) {
            return null;
        } else if (backends.isEmpty()) {
            return Backends.NONE;
        } else if (backends.size() == 1) {
            String backend = backends.iterator().next();
            return fromAnnotation(backend);
        } else {
            HashSet<Backend> bs = new HashSet<Backend>(backends.size());
            for (String backend : backends) {
                Backend b = Backend.fromAnnotation(backend);
                bs.add(b);
            }
            return new Backends(bs);
        }
    }

    static Backends fromAnnotation(String backend) {
        Backends bs = collections.get(backend);
        if (bs == null) {
            bs = new Backends(Collections.singleton(Backend.fromAnnotation(backend)));
            collections.put(backend, bs);
        }
        return bs;
    }

    @Override
    public String toString() {
        return backends.toString();
    }
}
