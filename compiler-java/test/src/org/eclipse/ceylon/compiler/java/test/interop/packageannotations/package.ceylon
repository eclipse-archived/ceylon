import javax.xml.bind.annotation.adapters { 
    xmlJavaTypeAdapters, 
    xmlJavaTypeAdapter
}

xmlJavaTypeAdapters { \ivalue=[xmlJavaTypeAdapter(`class IntegerAdaptor`)]; }
package org.eclipse.ceylon.compiler.java.test.interop.packageannotations;