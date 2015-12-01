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
package com.redhat.ceylon.compiler.java.util;

import com.redhat.ceylon.langtools.tools.javac.main.OptionName;
import com.redhat.ceylon.langtools.tools.javac.util.Context;
import com.redhat.ceylon.langtools.tools.javac.util.Log;
import com.redhat.ceylon.langtools.tools.javac.util.Options;

public class Timer extends com.redhat.ceylon.model.loader.Timer {

    private static final Context.Key<Timer> timerKey = new Context.Key<Timer>();
    
    public static Timer instance(Context context) {
        Timer instance = (Timer)context.get(timerKey);
        if (instance == null){
            instance = new Timer(context);
            context.put(timerKey, instance);
        }
        return instance;
    }
    
    private Timer(Context context) {
        Options options = Options.instance(context);
        verbose = options.get(OptionName.VERBOSE) != null 
                || options.get(OptionName.VERBOSE + ":benchmark" ) != null;
        out = context.get(Log.outKey);
    }
}
