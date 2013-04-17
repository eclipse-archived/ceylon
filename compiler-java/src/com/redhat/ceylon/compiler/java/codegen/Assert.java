/*
 * Copyright Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the authors tag. All rights reserved.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License version 2.
 * 
 * This particular file is subject to the "Classpath" exception as provided in the 
 * LICENSE file that accompanied this code.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */
package com.redhat.ceylon.compiler.java.codegen;

import com.redhat.ceylon.compiler.typechecker.tree.Node;

class Assert {
    private static String formatMessage(String message, Node node) {
        String result = "";
        if (message != null) {
            result += message + " ";
        }
        if (node != null) {
            result += "source code location: " + node.getUnit().getFilename() + ":" + node.getLocation() + " ";
        }
        return result.trim();
    }
    
    static RuntimeException fail(String message, Node node) {
        throw new RuntimeException(formatMessage(message, node));
    }
    static RuntimeException fail(String message) {
        return fail(message, null);
    }
    static RuntimeException fail() {
        return fail(null, null);
    }
    static void that(boolean cond, String message, Node node) {
        if (!cond) {
            fail(message, node);
        }
    }
    static void that(boolean cond, String message) {
        that(cond, message, null);
    }
    static void that(boolean cond) {
        that(cond, null);
    }
    static void not(boolean cond, String message, Node node) {
        if (cond) {
            fail(message, node);
        }
    }
    static void not(boolean cond, String message) {
        not(cond, message, null);
    }
    static void not(boolean cond) {
        not(cond, null);
    }
}
