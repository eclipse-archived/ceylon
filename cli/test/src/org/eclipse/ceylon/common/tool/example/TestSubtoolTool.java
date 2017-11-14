/********************************************************************************
 * Copyright (c) {date} Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.common.tool.example;

import org.eclipse.ceylon.common.tool.Option;
import org.eclipse.ceylon.common.tool.Subtool;
import org.eclipse.ceylon.common.tool.Tool;
import org.eclipse.ceylon.common.tools.CeylonTool;

public class TestSubtoolTool implements Tool {

    public static class Subtool1 implements Tool {
        private boolean foo;
        @Option
        public void setFoo(boolean foo) {
            this.foo = foo;
        }
        public boolean getFoo() {
            return foo;
        }
        @Override
        public void initialize(CeylonTool mainTool) {
        }
        @Override
        public void run() throws Exception {
            
        }
    }
    
    public class Subtool2 implements Tool {
        private boolean bar;
        @Option
        public void setBar(boolean bar) {
            this.bar = bar;
        }
        public boolean getBar() {
            return bar;
        }
        @Override
        public void initialize(CeylonTool mainTool) {
        }
        @Override
        public void run() throws Exception {
            
        }
    }

    private Tool action;
    
    @Subtool(argumentName="action", order=1,
            classes={Subtool1.class, Subtool2.class})
    public void setAction(Tool tool) {
        this.action = tool;
    }

    public Tool getAction() {
        return action;
    }

    @Override
    public void initialize(CeylonTool mainTool) {
    }

    @Override
    public void run() throws Exception {
        
    }
    
}
