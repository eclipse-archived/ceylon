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
package com.redhat.ceylon.compiler;

import java.text.ChoiceFormat;
import java.text.Format;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import com.redhat.ceylon.common.Messages;

public class CeylonCompileMessages extends Messages {

    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("com.redhat.ceylon.compiler.resources.messages");

    public static String msg(String msgKey, Object... msgArgs) {
        return msg(RESOURCE_BUNDLE, msgKey, msgArgs);
    }
    
    public static String msgCompilerErrors(int numErrors) {
        MessageFormat fmt = new MessageFormat("");
        fmt.setLocale(Locale.getDefault());
        double[] limits = {1, 2};
        String[] keys = {RESOURCE_BUNDLE.getString("compile.errors.one"), 
                RESOURCE_BUNDLE.getString("compile.errors.several")};
        ChoiceFormat choice = new ChoiceFormat(limits, keys);
        fmt.applyPattern(RESOURCE_BUNDLE.getString("compile.errors.pattern"));
        Format[] formats = {choice, NumberFormat.getInstance()};
        fmt.setFormats(formats);
        return fmt.format(new Object[]{numErrors});
    }
    
    public static String msgSystemError() {
        return msg("error.system") 
                + msg("error.report.url")
                + msg("error.report.list")
                + msg("error.report.description")
                + msg("error.report.thanks");
    }
    
    public static String msgBug(int code, Throwable cause) {
        String message = msg("error.bug", code) + msg("error.report.url");
        message += msg("error.report.list");
        if (cause != null) {
            message += msg("error.report.stacktrace");
        }
        message += msg("error.report.description");
        message += msg("error.report.thanks");
        return message;
    }
}