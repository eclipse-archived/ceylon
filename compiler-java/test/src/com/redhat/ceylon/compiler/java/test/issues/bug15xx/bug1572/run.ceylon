import ceylon.collection { MutableList, ArrayList }
import com.redhat.ceylon.compiler.typechecker.tree { Tree { ... }, Node, VisitorAdaptor }

shared class FormattingVisitor() extends VisitorAdaptor() {}

class MyList<Element>({Element*} elements) {
    if (is MyList<Element> elements) {
        // testing for Summable<Element> also throws an exception
    }
}

void run() {
    MutableList<StaticType|BaseMemberExpression> l = ArrayList<StaticType|BaseMemberExpression>();
    MyList<String|Node> l2 = MyList<String|Node>({});
    FormattingVisitor();
}
