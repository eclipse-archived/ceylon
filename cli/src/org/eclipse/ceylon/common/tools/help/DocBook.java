/********************************************************************************
 * Copyright (c) {date} Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.common.tools.help;

import org.tautua.markdownpapers.ast.Node;

class DocBook extends AbstractMl<DocBook> {

    public DocBook(Appendable out) {
        super(out);
    }
    
    public DocBook markdown(Node doc) {
        DocBookMarkdownVisitor markdownVisitor = new DocBookMarkdownVisitor(out);
        doc.accept(markdownVisitor);
        return this;
    }
    
}
