package com.redhat.ceylon.common.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import com.redhat.ceylon.common.config.Authentication.PasswordPrompt;

class MockPasswordPrompt implements PasswordPrompt {
    
    final LinkedHashMap<String, String> prompts = new LinkedHashMap<String, String>();
    final List<String> seenPrompts = new ArrayList<String>();
    
    public MockPasswordPrompt() {
    }
    
    @Override
    public char[] getPassword(String prompt) {
        this.seenPrompts.add(prompt);
        if (!prompts.containsKey(prompt)) {
            Assert.fail("Unexpected prompt: " + prompt);
        }
        String password = prompts.get(prompt);
        return password != null ? password.toCharArray() : null;
    }
    
    /** Asserts that only the given prompts have been seen */
    public void assertSeenPrompts(List<String> prompts) {
        Assert.assertEquals(prompts, seenPrompts);
    }
    
    /** Asserts that only the given prompts have been seen */
    public void assertSeenPrompts(String... prompts) {
        Assert.assertEquals(Arrays.asList(prompts), seenPrompts);
    }
    
    /** Asserts that only the prompts in the {@link #prompts} map have been 
     * seen
     */
    public void assertSeenOnlyGivenPrompts() {
        assertSeenPrompts(new ArrayList(prompts.keySet()));
    }
    
    public void assertSeenNoPrompts() {
        assertSeenPrompts(Collections.<String>emptyList());
    }
}