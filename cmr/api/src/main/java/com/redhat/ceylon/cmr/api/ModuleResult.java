package com.redhat.ceylon.cmr.api;

import java.util.SortedSet;
import java.util.TreeSet;

public class ModuleResult {
    private SortedSet<String> results = new TreeSet<String>();

    public SortedSet<String> getResults() {
        return results;
    }

    public void addResult(String newResult) {
        for(String existingResult : results){
            if(results.equals(newResult))
                return;
            // figure out if the new entry is a shorter path to one that exists
            if(existingResult.startsWith(newResult)){
                // keep the shorter version
                results.remove(existingResult);
                results.add(newResult);
                return;
            }
            // perhaps the new result is longer than an existing result
            if(newResult.startsWith(existingResult)){
                // ignore it then
                return;
            }
        }
        // just add it
        results.add(newResult);
    }
}
