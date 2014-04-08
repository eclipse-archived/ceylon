import com.redhat.ceylon.compiler.java.test.interop { JavaEnum { one = \iONE, two = \iTWO } }
import java.lang {
    Thread {
        State {
            runnable=\iRUNNABLE,
            neww=\iNEW,
            blocked=\iBLOCKED
        }
    }
}

class EnumSwitch() {
    void exhaustive(JavaEnum e){
        switch(e)
        case (one) {
        }
        case (two) {
        }
    }
    void notExhaustive(JavaEnum e){
        switch(e)
        case (one) {
        }
        else {
        }
    }
    void optionalExhaustive(JavaEnum? e){
        switch(e)
        case (one) {
        }
        case (two) {
        }
        case (null) {
        }
    }
    void optionalExhaustive2(JavaEnum? e){
        switch(e)
        case (one,two) {
        }
        case (null) {
        }
    }
    void union(JavaEnum|Thread.State e){
        switch(e)
        case (one) {
        }
        case (two) {
        }
        case (neww) {
            
        }
        case (runnable) {
            
        }
        case (blocked) {
            
        }
        else {
        }
    }
}