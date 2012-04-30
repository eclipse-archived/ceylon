void objectArgumentNamedInvocation() {

   void callFunction(Object o) {
   }

   callFunction {
       object o extends Object() {
       }
   };
}