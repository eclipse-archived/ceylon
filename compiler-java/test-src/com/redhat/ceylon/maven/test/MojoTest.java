package com.redhat.ceylon.maven.test;

import java.io.File;
import java.util.Arrays;

import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.DefaultInvoker;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.InvocationResult;
import org.apache.maven.shared.invoker.Invoker;
import org.apache.maven.shared.invoker.MavenInvocationException;
import org.junit.Assert;

public abstract class MojoTest {

    public MojoTest() {
        super();
    }

    protected void mvn(String pomFile, String... goals)
            throws MavenInvocationException {
        InvocationRequest request = new DefaultInvocationRequest();
        request.setPomFile(new File(pomFile));
        request.setGoals(Arrays.asList(goals));
        request.setDebug(true);
    
        Invoker invoker = new DefaultInvoker();
        if (System.getProperty("maven.home") == null
                && System.getenv("M2_HOME") == null) {
            Assert.fail("Unable to run maven tests without either maven.home " +
            		"system property or M2_HOME environment variable set to " +
            		"the location of a maven installation");
        }
        InvocationResult result = invoker.execute(request);
    
        if (result.getExitCode() != 0) {
            Assert.fail("Maven invocation returned exit code " + result.getExitCode());
        }
    }

}