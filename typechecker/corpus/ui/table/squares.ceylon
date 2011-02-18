Table squares(Natural min=1, Natural max) { 
    title="Squares of natural numbers"; 
    rows=max-min+1;
    Border border {
        padding=2;
        weight=1;
    }
    Column { 
        heading="x"; 
        width=10; 
        String content(Natural row) {
            return $(row+min);
        }
    },
    Column { 
        heading="x**2"; 
        width=10; 
        String content(Natural row) {
            return $((row+min)**2);
        }
    } 
}
