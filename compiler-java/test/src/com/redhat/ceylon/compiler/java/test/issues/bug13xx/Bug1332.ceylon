@noanno
shared abstract class Bug1332<Attached>() 
{
    shared formal Attached m(Attached p);
    shared formal class Node(shared Attached attached){}
}
@noanno
shared class Bug1332_2() 
        extends Bug1332<String>() 
{
    shared actual String m(String p0){ return p0; }
    // Fine if attached0 => attached
    shared actual class Node(String attached0)
             extends super.Node(attached0){}
}
