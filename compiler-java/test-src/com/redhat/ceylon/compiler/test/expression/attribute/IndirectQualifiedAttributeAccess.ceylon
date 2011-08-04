@nomodel
class IndirectQualifiedAttributeAccess(){
   Boolean b = true;
   IndirectQualifiedAttributeAccess q = IndirectQualifiedAttributeAccess();
   
    Boolean test(){
        // Typechecker should figure out b is captured as well!   
        return q.b;
    }
}