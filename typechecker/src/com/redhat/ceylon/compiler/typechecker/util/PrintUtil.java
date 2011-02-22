package com.redhat.ceylon.compiler.typechecker.util;

import com.redhat.ceylon.compiler.typechecker.tree.Node;

import java.util.List;

/**
 * @author Emmanuel Bernard <emmanuel@hibernate.org>
 */
public class PrintUtil {

    public static String importNodeToString(List<? extends Node> nodes) {
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

    public static String importPathToString(List<String> path) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for( String node : path ) {
            if (first) {
                first = false;
            }
            else {
                sb.append(".");
            }
            sb.append( node );
        }
        return sb.toString();
    }

    public static String importPathToString(String[] path) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for( String node : path ) {
            if (first) {
                first = false;
            }
            else {
                sb.append(".");
            }
            sb.append( node );
        }
        return sb.toString();
    }
}
