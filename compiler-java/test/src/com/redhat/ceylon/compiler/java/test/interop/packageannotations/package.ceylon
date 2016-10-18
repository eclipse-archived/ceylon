import javax.xml.bind.annotation.adapters { 
    xmlJavaTypeAdapters, 
    xmlJavaTypeAdapter
}

xmlJavaTypeAdapters { \ivalue=[xmlJavaTypeAdapter(`class IntegerAdaptor`)]; }
package com.redhat.ceylon.compiler.java.test.interop.packageannotations;