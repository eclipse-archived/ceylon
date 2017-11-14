
package org.eclipse.ceylon.common.tools.help;

class Html extends AbstractMl<Html> {

    public Html(Appendable out) {
        super(out);
    }
    
    public Html link(String linkText, String url) {
        return open("a href='" + url + "'").text(linkText).close("a");
    }
    
}
