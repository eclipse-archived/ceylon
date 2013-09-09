void testVariadic() {
    VariadicStar();
    VariadicStar("");
    VariadicStar("", "");
    
    @error
    VariadicPlus();
    VariadicPlus("");
    VariadicPlus("", "");
}