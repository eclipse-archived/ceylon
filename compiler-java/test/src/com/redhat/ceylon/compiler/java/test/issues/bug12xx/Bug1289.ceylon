@noanno
interface Bug1289_Bar<Key>{
    shared formal void m(Key x, Key y, Key z);
}
@noanno
class Bug1289_Foo() satisfies Bug1289_Bar<Integer> {
    shared actual void m(Integer xboxed, Integer yboxed, Integer zboxed) {
        Integer x = nothing;
        Integer y = nothing;
        Integer z = nothing;
        variable Boolean b;
        b = x < y < z;
        b = x < z < y;
        b = y < x < z;
        b = y < z < x;
        b = z < x < y;
        b = z < y < x;
        
        b = xboxed < y < z;
        b = xboxed < z < y;
        b = y < xboxed < z;
        b = y < z < xboxed;
        b = z < xboxed < y;
        b = z < y < xboxed;
        
        b = xboxed < yboxed < z;
        b = xboxed < z < yboxed;
        b = yboxed < xboxed < z;
        b = yboxed < z < xboxed;
        b = z < xboxed < yboxed;
        b = z < yboxed < xboxed;
        
        b = xboxed < yboxed < zboxed;
        b = xboxed < zboxed < yboxed;
        b = yboxed < xboxed < zboxed;
        b = yboxed < zboxed < xboxed;
        b = zboxed < xboxed < yboxed;
        b = zboxed < yboxed < xboxed;
    }
}
@noanno
class Bug1289_Foo2() satisfies Bug1289_Bar<Float> {
    shared actual void m(Float xboxed, Float yboxed, Float zboxed) {
        Float x = nothing;
        Float y = nothing;
        Float z = nothing;
        variable Boolean b;
        b = x < y < z;
        b = x < z < y;
        b = y < x < z;
        b = y < z < x;
        b = z < x < y;
        b = z < y < x;
        
        b = xboxed < y < z;
        b = xboxed < z < y;
        b = y < xboxed < z;
        b = y < z < xboxed;
        b = z < xboxed < y;
        b = z < y < xboxed;
        
        b = xboxed < yboxed < z;
        b = xboxed < z < yboxed;
        b = yboxed < xboxed < z;
        b = yboxed < z < xboxed;
        b = z < xboxed < yboxed;
        b = z < yboxed < xboxed;
        
        b = xboxed < yboxed < zboxed;
        b = xboxed < zboxed < yboxed;
        b = yboxed < xboxed < zboxed;
        b = yboxed < zboxed < xboxed;
        b = zboxed < xboxed < yboxed;
        b = zboxed < yboxed < xboxed;
    }
}
@noanno
class Bug1289_Foo3<Element>() satisfies Bug1289_Bar<Element> 
        given Element satisfies Comparable<Element> {
    shared actual void m(Element xboxed, Element yboxed, Element zboxed) {
        Element x = nothing;
        Element y = nothing;
        Element z = nothing;
        variable Boolean b;
        b = x < y < z;
        b = x < z < y;
        b = y < x < z;
        b = y < z < x;
        b = z < x < y;
        b = z < y < x;
        
        b = xboxed < y < z;
        b = xboxed < z < y;
        b = y < xboxed < z;
        b = y < z < xboxed;
        b = z < xboxed < y;
        b = z < y < xboxed;
        
        b = xboxed < yboxed < z;
        b = xboxed < z < yboxed;
        b = yboxed < xboxed < z;
        b = yboxed < z < xboxed;
        b = z < xboxed < yboxed;
        b = z < yboxed < xboxed;
        
        b = xboxed < yboxed < zboxed;
        b = xboxed < zboxed < yboxed;
        b = yboxed < xboxed < zboxed;
        b = yboxed < zboxed < xboxed;
        b = zboxed < xboxed < yboxed;
        b = zboxed < yboxed < xboxed;
    }
}