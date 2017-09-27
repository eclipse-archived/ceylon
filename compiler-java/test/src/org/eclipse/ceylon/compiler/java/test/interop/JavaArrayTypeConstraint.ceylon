import java.lang{ObjectArray, IntArray}

class JavaArrayTypeConstraint<X,Y>()
        @error
        given X satisfies ObjectArray<Object>
        @error
        given Y satisfies IntArray {
    
}