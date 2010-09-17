class Optional () {

    Integer? one = next(1);
    Integer? two = null;


       public Integer next(Integer? a) {
           if (exists a) {
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
