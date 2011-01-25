Table table { 
    title="Squares"; 
    rows=5;
    Border border {
        padding=2;
        weight=1;
    }
    Column { 
        title="x"; 
        width=10; 
        String content(Natural row) {
            return $row
        }
    },
    Column { 
        title="x**2"; 
        width=10; 
        String content(Natural row) {
            return $row**2
        }
    } 
}
