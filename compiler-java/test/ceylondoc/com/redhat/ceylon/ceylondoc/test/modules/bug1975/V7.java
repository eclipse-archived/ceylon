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

package com.redhat.ceylon.ceylondoc.test.modules.bug1975;

@com.redhat.ceylon.compiler.java.metadata.Ceylon(major = 8)
class V7 implements com.redhat.ceylon.compiler.java.runtime.model.ReifiedType {
    
    V7(@com.redhat.ceylon.compiler.java.metadata.Name("f")
    @com.redhat.ceylon.compiler.java.metadata.FunctionalParameter("(a)")
    @com.redhat.ceylon.compiler.java.metadata.TypeInfo("ceylon.language::Callable<ceylon.language::Integer,ceylon.language::Tuple<ceylon.language::String,ceylon.language::String,ceylon.language::Empty>>")
    final ceylon.language.Callable<? extends ceylon.language.Integer> f, @com.redhat.ceylon.compiler.java.metadata.Name("g")
    @com.redhat.ceylon.compiler.java.metadata.FunctionalParameter("(a)")
    @com.redhat.ceylon.compiler.java.metadata.TypeInfo("ceylon.language::Callable<ceylon.language::Integer,ceylon.language::Tuple<ceylon.language::String,ceylon.language::String,ceylon.language::Empty>>")
    final ceylon.language.Callable<? extends ceylon.language.Integer> g, @com.redhat.ceylon.compiler.java.metadata.Name("h")
    @com.redhat.ceylon.compiler.java.metadata.FunctionalParameter("(a)")
    @com.redhat.ceylon.compiler.java.metadata.TypeInfo("ceylon.language::Callable<ceylon.language::Integer,ceylon.language::Tuple<ceylon.language::String,ceylon.language::String,ceylon.language::Empty>>")
    final ceylon.language.Callable<? extends ceylon.language.Integer> h, @com.redhat.ceylon.compiler.java.metadata.Name("i")
    @com.redhat.ceylon.compiler.java.metadata.FunctionalParameter("(a)")
    @com.redhat.ceylon.compiler.java.metadata.TypeInfo("ceylon.language::Callable<ceylon.language::Integer,ceylon.language::Tuple<ceylon.language::String,ceylon.language::String,ceylon.language::Empty>>")
    final ceylon.language.Callable<? extends ceylon.language.Integer> i) {
        this.g = g;
        this.h = h;
        this.i = i;
    }
    @com.redhat.ceylon.compiler.java.metadata.Ignore
    private final ceylon.language.Callable<? extends ceylon.language.Integer> g;
    
    @ceylon.language.SharedAnnotation$annotation$
    @com.redhat.ceylon.compiler.java.metadata.Annotations({@com.redhat.ceylon.compiler.java.metadata.Annotation("shared")})
    @com.redhat.ceylon.compiler.java.metadata.TypeInfo("ceylon.language::Callable<ceylon.language::Integer,ceylon.language::Tuple<ceylon.language::String,ceylon.language::String,ceylon.language::Empty>>")
    public final long g(@com.redhat.ceylon.compiler.java.metadata.Name("a")
    @com.redhat.ceylon.compiler.java.metadata.TypeInfo("ceylon.language::String")
    final java.lang.String a) {
        return g.$call$(ceylon.language.String.instance(a)).longValue();
    }
    @com.redhat.ceylon.compiler.java.metadata.Ignore
    private final ceylon.language.Callable<? extends ceylon.language.Integer> h;
    
    @ceylon.language.SharedAnnotation$annotation$
    @com.redhat.ceylon.compiler.java.metadata.Annotations({@com.redhat.ceylon.compiler.java.metadata.Annotation("shared")})
    @com.redhat.ceylon.compiler.java.metadata.TypeInfo("ceylon.language::Callable<ceylon.language::Integer,ceylon.language::Tuple<ceylon.language::String,ceylon.language::String,ceylon.language::Empty>>")
    public final long h(@com.redhat.ceylon.compiler.java.metadata.Name("a")
    @com.redhat.ceylon.compiler.java.metadata.TypeInfo("ceylon.language::String")
    final java.lang.String a) {
        return h.$call$(ceylon.language.String.instance(a)).longValue();
    }
    
    @com.redhat.ceylon.compiler.java.metadata.Ignore
    private final ceylon.language.Callable<? extends ceylon.language.Integer> i;
    
    @com.redhat.ceylon.compiler.java.metadata.TypeInfo("ceylon.language::Callable<ceylon.language::Integer,ceylon.language::Tuple<ceylon.language::String,ceylon.language::String,ceylon.language::Empty>>")
    private final long i$priv$(@com.redhat.ceylon.compiler.java.metadata.Name("a")
    @com.redhat.ceylon.compiler.java.metadata.TypeInfo("ceylon.language::String")
    final java.lang.String a) {
        return i.$call$(ceylon.language.String.instance(a)).longValue();
    }
    
    @com.redhat.ceylon.compiler.java.metadata.TypeInfo("ceylon.language::Anything")
    private final void capture$priv$() {
        i$priv$("");
    }

    @java.lang.Override
    @com.redhat.ceylon.compiler.java.metadata.Ignore
    public com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor $getType$() {
        return com.redhat.ceylon.ceylondoc.test.modules.bug1975.V7.$TypeDescriptor$;
    }
    @com.redhat.ceylon.compiler.java.metadata.Ignore
    public static final com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor $TypeDescriptor$ = com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor.klass(com.redhat.ceylon.ceylondoc.test.modules.bug1975.V7.class);
}