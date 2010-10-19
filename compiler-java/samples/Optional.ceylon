class Optional () {

    Integer? one = next(1);
    mutable Integer? two = null;

    two := 3;

       public Integer next(Integer? a) {
           if (exists a) {	   
               mutable Integer b := a;
               b++;
               return a + 1;
           } else {	 
               return -1;
           }
       }
}

public class TestOptional(Process p) {

       Optional o = Optional();
       p.writeLine(o.next(2));
}
