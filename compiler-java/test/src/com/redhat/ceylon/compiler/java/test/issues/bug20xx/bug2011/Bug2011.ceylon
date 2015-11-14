import javax.swing.tree {
    DefaultMutableTreeNode,
    MutableTreeNode
}
class MWETreeNode() extends DefaultMutableTreeNode() {
    // error: inherited member is refined by intervening superclass: 'DefaultMutableTreeNode' refines 'remove' declared by 'DefaultMutableTreeNode'
    shared actual void remove(Integer int) => super.remove(int);
    // error: inherited member is refined by intervening superclass: 'DefaultMutableTreeNode' refines 'remove' declared by 'DefaultMutableTreeNode'
    shared actual void remove(MutableTreeNode? mutableTreeNode) =>
            super.remove(mutableTreeNode);
    shared actual void setUserObject(Object? obj) {}
}