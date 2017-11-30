void destructuring2([Integer,Integer,String] tuple, String->Float entry, String[] strings) {
    let ([i,j,str] = tuple);
    Integer ii = i;
    Integer jj = j;
    String s = str;
    let ([Integer i_,value j_,@error Float str_] = tuple);
    @error let ([x,y] = tuple);
    let ([a,b,c,@error d,@error e] = tuple);
    @error let ([z] = "");
    let (name->quantity = entry);
    String n = name;
    Float q = quantity;
    let (@error Integer name_->Float quantity_ = entry);
    let ([@type:"Tuple<Integer|String,Integer,Tuple<Integer|String,Integer,Tuple<String,String,Empty>>>" *tup] = tuple);
    let ([@type:"Sequential<String>" *ss] = strings);
}

void variadicDestructuring2([String, String, String*] strings, 
    [Integer, Float, String] tup, 
    [Float+] floats) {
    let ([@type:"String" x, @type:"String" y, @type:"Sequential<String>" *rest] = strings);
    String[] s_ = rest;
    let ([@type:"Integer" i, @type:"Tuple<Float|String,Float,Tuple<String,String,Empty>>" *pair] = tup);
    let ([Float ff, String ss] = pair);
    let ([@type:"Float" z, @type:"Sequential<Float>" *zs] = floats);
    Float[] xs_ = zs;
    
}

void destructureTupleInEntry2(String->[Float,Float] entry) {
    let (@type:"String" s->[@type:"Float" x, @type:"Float" y] = entry);
    value z = let (@type:"String" s_->[@type:"Float" x_, @type:"Float" y_] = entry) x_*y_;
    let(String ss = s,
        Float xx = x,
        Float yy = y);
}

void destructureNestedTuple2([String,[Integer,Float],String->String] tuple) {
    let ([@type:"String" s, [@type:"Integer" i, @type:"Float" f], @type:"String" k -> @type:"String" v] = tuple);
    value x = let ([@type:"String" s_, [@type:"Integer" i_,@type:"Float" f_], @type:"String" k_ -> @type:"String" v_] = tuple) k_+v_;
    let(String ss = s,
        Integer ii = i,
        Float ff = f,
        String kk = k,
        String vv = v);
    let ([[xx1,yy1],zz1] = [[1,2],4]);
    let ([xx2,yy2]->zz2 = [1,2]->4);
}

void brokenDestructuring2([String,[Integer,Float],String->String] tuple) {
    @error let ([s,[i,f],k->v]);
    //@error let (x = let ([s_,[i_,f_],k_->v_] ) k_+v_);
}

void destructureInFor2({[String, Float, String->String]*} iter) {
    for ([x, y, s1->s2] in iter) {
        String s = x;
        Float f = y;
        String->String e = s1->s2;
    }
    for ([String x, Float y, String s1 -> String s2] in iter) {
        String s = x;
        Float f = y;
        String->String e = s1->s2;
    }
}

void destructureIf2([Float, Integer]? maybePair, String[] names, <String->Object>? maybeEntry) {
    if (exists [@type:"Float" x, @type:"Integer" i] = maybePair) {
        Float c = x;
        Integer j = i;
    }
    if (nonempty [@type:"String" name, *rest] = names) {
        String n = name;
        String[] ns = rest;
    }
    if (exists @type:"String" k -> @type:"Object" v = maybeEntry) {
        String key = k;
        Object item = v;
    }
    assert (exists [@type:"Float" float,@type:"Integer" int] = maybePair, 
            exists @type:"String" key->@type:"Object" item = maybeEntry);
    Float float_ = float;
    Integer int_ = int;
    String key_ = key;
    Object item_ = item;
    while (exists [x_, i_] = maybePair, 
           exists s_->o_ = maybeEntry, 
           nonempty [f_,*r_] = names) {
        Float x__ = x_;
        Integer i__ = i_;
        String s__ = s_;
        Object o__ = o_;
        String f__ = f_;
        String[] r__ = r_;
    }
}

void buggy2() {
    [Integer, Integer]|[Integer] foo = [42];
    @error let ([x,y] = foo);
    [Integer, Integer+] bar = [42, 53];
    @error let ([a,b] = bar);
    [Integer, Integer*] baz = [42, 53];
    @error let ([w,z] = baz);
    [Integer,Integer+] list = [42, 53];
    let ([@type:"Integer" first, @type:"Sequence<Integer>" *rest] = list);
}

void unknownTail3<First,Rest>(Tuple<Anything,First,Rest> tup,
    String(*Tuple<Anything,First,Rest>) fun)
        given Rest satisfies Anything[]
        given First satisfies Object {
    let ([@type:"First" x, @type:"Rest" *ys] = tup);
    @type:"String" fun(*tup);
    @type:"String" fun(x,*ys);
    let ([@type:"Rest" *zs] = ys);
}

void unknownTail4<First,Rest>(Tuple<Anything,First,Rest> tup,
    String(*Tuple<Anything,First,Rest>) fun)
        given Rest satisfies [Object,Object]
        given First satisfies Object {
    let ([@type:"First" x, @type:"Rest" *ys] = tup);
    @type:"String" fun(*tup);
    @type:"String" fun(x,*ys);
    let ([@type:"Rest" *zs] = ys);
}

shared void tupleLengths2(){
    let ([a0,@error b0,@error *c0] = [1],
         [a1,@error *c1] = [1],
         [a2,*c2] = [1,2],
         [a3,c3] = [1,2],
         [*a4] = [1,2],
         [*a5] = [1],
         [a6] = [1],
         [a7,@error *c7] = [1],
         [a8,@error c8] = [1],
         [a9,b9,@error *c9] = [1,2],
         [a10,b10,@error c10] = [1,2]);
    @error let ([a11] = []);
    @error let ([*a12] = []);
    @error let ([*s] = "hello");
    @error let (x1->y1 = [1,2]);
    @error let ([x2,y2] = 1->2);
}

shared void nothings2() {
    @error for (k->v in {}) {
        //value kk = k;
    }
    @error for (k->[v] in {}) {
        //value vv = v;
    }
    @error for ([x,y] in {}) {
        //value xx = x;
    }
}

void syntax3() {
    let (seq = {""->[1, 2]});
    for (value str -> [value x, value y] in seq) {}
    let (value str -> [value x, value y] = seq.first);
    let (seq2 = {[1,2,3]});
    for ([value a, value b, value c] in seq2) {}
    let ([value a, value b, value c] = seq2.first);
}

void syntax4() {
    let (seq = {""->[1, 2]});
    for (str -> [x, y] in seq) {}
    let (str -> [x, y] = seq.first);
    let (seq2 = {[1,2,3]});
    for ([a, b, c] in seq2) {}
    let ([a, b, c] = seq2.first);
}

void letStatement1([String, Integer->Float] pair, Float[3] point) {
    let ([key, pos->intensity] = pair, 
         [x, y, z] = point,
         d = (x^2+y^2+z^2)^0.5,
         sum = x + y + z);
    
}

void letStatement2([String, Integer->Float] pair, Float[3] point) {
    let ([String key, Integer pos->Float intensity] = pair, 
         [Float x, Float y, Float z] = point,
         Float d = (x^2+y^2+z^2)^0.5,
         value sum = x + y + z);
    
}