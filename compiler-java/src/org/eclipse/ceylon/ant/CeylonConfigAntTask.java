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
package org.eclipse.ceylon.ant;

import org.apache.tools.ant.BuildException;
import org.eclipse.ceylon.common.config.CeylonConfig;

public class CeylonConfigAntTask extends CeylonConfigBaseTask {
    
    private String prefix;
    private String infix;
    
    public String getPrefix() {
        return (prefix != null) ? prefix : "CEYLON.";
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getInfix() {
        return (infix != null) ? infix : ",";
    }

    public void setInfix(String infix) {
        this.infix = infix;
    }

    @Override
    public void execute() throws BuildException {
        Java7Checker.check();
        CeylonConfig config = getConfig();
        for (String key : config.getOptionNames(null)) {
            String[] values = config.getOptionValues(key);
            setConfigValueAsProperty(values, getPrefix() + key);
        }
    }

}
