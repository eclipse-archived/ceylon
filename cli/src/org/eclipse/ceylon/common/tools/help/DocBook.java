
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
