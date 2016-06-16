@noanno
suppressWarnings("unusedDeclaration")
interface InvokingObjectMethods {
    /* The supertype of an interface is Object
      so super.string, is allowed in any 
      ceylon interface, but javac won't 
      let us use super in that way
     */
    void callMethodsOnObject() {
        variable String s = super.string;
        s = (super of Object).string;
    }
}
@noanno
suppressWarnings("unusedDeclaration")
interface InvokingIdentifiableMethods satisfies Identifiable {
    /* The supertype of an interface is Object
      so super.string, super.hash and super.equals() are 
      all allowed in a ceylon interface, but javac won't 
      let use use super in that way
     */
    void callMethodsOnObject() {
        String s = (super of Object).string;
        Integer h = super.hash;
         variable Boolean e = super.equals(this);
         //e = super.equals{
            //that=this;
         //};
         //value fuck = super.equals;
    }
}