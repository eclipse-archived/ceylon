/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.common.tools.help;

class Html extends AbstractMl<Html> {

    public Html(Appendable out) {
        super(out);
    }
    
    public Html link(String linkText, String url) {
        return open("a href='" + url + "'").text(linkText).close("a");
    }
    
}
