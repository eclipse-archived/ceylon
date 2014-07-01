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

package com.sun.tools.javac.util;

import java.util.ArrayList;

public class SourceLanguage {
    
    public static final Context.Key<SourceLanguage> sourceLanguageKey = new Context.Key<SourceLanguage>();

    public static SourceLanguage instance(Context context) {
        SourceLanguage instance = context.get(sourceLanguageKey);
        if (instance == null) {
            instance = new SourceLanguage();
            context.put(sourceLanguageKey, instance);
        }
        return instance;
    }

    public enum Language {
        JAVA, CEYLON
    }

    ArrayList<SourceLanguage.Language> l = new ArrayList<SourceLanguage.Language>();

    SourceLanguage(){
        l.add(Language.JAVA);
    }

    private ArrayList<SourceLanguage.Language> stack() {
        return l;
    }

    public void push(final SourceLanguage.Language lang) {
        stack().add(lang);
    }
    public SourceLanguage.Language pop() {
        return stack().remove(stack().size() - 1);
    }
    public SourceLanguage.Language current() {
        return stack().get(stack().size() - 1);
    }
    
    public boolean isCeylon() {
        return current() == SourceLanguage.Language.CEYLON;
    }
}