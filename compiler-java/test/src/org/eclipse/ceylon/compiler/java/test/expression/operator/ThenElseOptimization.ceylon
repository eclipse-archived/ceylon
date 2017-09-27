@noanno
void thenElseOptimization(Boolean b) {
    /*value then0 = b then 2;
    value then1 = true then 2;
    
    Anything opt = null;
    value else0 = opt else 2;
    value else1 = opt else null;*/
    
    value thenElse0 = b then 2 else 3;
    value thenElse1 = b then "true" else "false";
    value thenElse2 = b then "true" else 2;
    value thenElse3 = b then "true" else null;
    value thenElse4 = b then true else false;
    value thenElse5 = b then true else null;
}