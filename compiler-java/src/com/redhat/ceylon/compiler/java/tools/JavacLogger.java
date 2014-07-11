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
package com.redhat.ceylon.compiler.java.tools;

import com.redhat.ceylon.common.log.Logger;
import com.sun.tools.javac.main.OptionName;
import com.sun.tools.javac.util.Log;
import com.sun.tools.javac.util.Options;

public class JavacLogger implements Logger {

    private boolean debugEnabled;
    private Log log;

    public JavacLogger(Options options, Log log) {
        this.debugEnabled = options.get(OptionName.VERBOSE) != null || options.get(OptionName.VERBOSE + ":cmr") != null;
        this.log = log;
    }

    @Override
    public void error(String str) {
        log.error("ceylon", str);
    }

    @Override
    public void warning(String str) {
        log.warning("ceylon", str);
    }

    @Override
    public void info(String str) {
        log.note("ceylon", str);
    }

    @Override
    public void debug(String str) {
        if(debugEnabled)
            log.printLines(log.errWriter, "["+str+"]");
    }

}
