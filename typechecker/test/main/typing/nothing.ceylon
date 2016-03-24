void nuthn() {
    @type:"String" String c = nothing(4, "");
    @type:"String" value s1 = "" + nothing;
    //@type:"String" TODO???
    @error value s2 = nothing + "";
    @error value x = nothing + nothing;
    @error value y = nothing.foo;
    String? ss = null;
    @type:"String" value d = ss else nothing;
    @error value d = nothing else "";
}