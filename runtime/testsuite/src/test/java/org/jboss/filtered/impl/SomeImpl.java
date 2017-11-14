

package org.jboss.filtered.impl;

import org.jboss.filtered.api.SomeAPI;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class SomeImpl extends SomeAPI {
    protected String onward() {
        return "Onward ";
    }

    public String go(String arg) {
        return onward() + arg + "!";
    }
}
