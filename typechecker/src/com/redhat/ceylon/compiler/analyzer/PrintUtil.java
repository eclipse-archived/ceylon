package com.redhat.ceylon.compiler.analyzer;

import com.redhat.ceylon.compiler.tree.Node;

import java.util.List;

/**
 * @author Emmanuel Bernard <emmanuel@hibernate.org>
 */
public class PrintUtil {

    public static String importPathToString(List<? extends Node> nodes) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for( Node node : nodes ) {
            if (first) {
                first = false;
            }
            else {
                sb.append(".");
            }
            sb.append( node.getText() );
        }
        return sb.toString();
    }
}
