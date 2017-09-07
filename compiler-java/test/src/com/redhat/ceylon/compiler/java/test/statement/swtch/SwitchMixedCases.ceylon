@noanno
void switchMixedCases(Integer|String|Float|Null arg) {
    
    switch(arg)
    case (Float) {
        Float a = arg;
    }
    case (String|1|2|3) {
        String|Integer a = arg;
    }
    else case (Integer|null) {
        Integer? a = arg;
    }
    
    Boolean? bool = null;
    switch (bool)
    case (true) {}
    case (false|Null) {}

}