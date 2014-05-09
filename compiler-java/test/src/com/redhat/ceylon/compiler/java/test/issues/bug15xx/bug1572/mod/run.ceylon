import com.redhat.ceylon.compiler.typechecker.tree { Tree { ... }, Node, VisitorAdaptor }

shared class FormattingVisitor() extends VisitorAdaptor() {}

class MyList<Element>({Element*} elements) {
    if (is MyList<Element> elements) {
        // testing for Summable<Element> also throws an exception
    }
}

void run() {
    MyList<String|Node> list = MyList<String|Node>({});
}
