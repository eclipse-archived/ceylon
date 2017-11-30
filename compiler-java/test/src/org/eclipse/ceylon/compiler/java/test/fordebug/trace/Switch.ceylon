shared void swtch() {
    String s = "xyz";
    switch(s)
    case ("abc") {
        
    }
    case ("xyz") {
        print(s);
    }
    else {
        
    }
    
    String? s2 = "xyz";
    switch(s2)
    case ("abc") {
        
    }
    case ("xyz") {
        print(s2);
    }
    else {
        
    }
    
    String? s3 = "xyz";
    switch(s3)
    case (null) {
        
    }
    case ("abc") {
        
    }
    case ("xyz") {
        print(s3);
    }
    else {
        
    }
    
    Integer i = 42;
    switch(i)
    case (2) {
        
    }
    case (42) {
        print(i+1);
    }
    else {
        
    }
    
    Integer? i2 = 42;
    switch(i2)
    case (2) {
        
    }
    case (42) {
        print(i2);
    }
    else {
        
    }
    
    Integer? i3 = 42;
    switch(i3)
    case (null) {
        
    }
    case (2) {
        
    }
    case (42) {
        print(i3);
    }
    else {
        
    }
    
    Integer|String is1 = 42;
    switch(is1)
    case (is String) {
        
    }
    case (is Integer) {
        print(i3);
    }
}