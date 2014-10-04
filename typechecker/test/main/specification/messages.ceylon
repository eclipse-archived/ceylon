class CheckMessages() {
    @error:"not yet declared" print(s);
    @error:"not yet declared" print(x);
    @error:"not yet declared" print(f());
    @error:"not yet declared" s = "";
    @error:"not yet declared" x => "";
    @error:"not yet declared" f() => "";
    String s;
    void m() {
        @error:"declared in outer scope" s = "";
    }
    s = "";
    String x {
        @error:"within its own body" x => "";
        return "";
    }
    String f() {
        @error:"within its own body" f() => "";
        return "";
    }
    @error:"aready definitely specified" s = "";
    @error:"aready definitely specified" x => "";
    @error:"aready definitely specified" f() => "";
    print(s);
    print(x);
    print(f());
}

void checkMessages() {
    @error:"not yet declared" print(s);
    @error:"not yet declared" print(x);
    @error:"not yet declared" s = "";
    @error:"not yet declared" x =>  "";
    @error:"not yet declared" print(f());
    @error:"not yet declared" f() => "";
    String s;
    void m() {
        @error:"declared in outer scope" s = "";
    }
    s = "";
    String x {
        @error:"within its own body" x => "";
        return "";
    }
    String f() {
        @error:"within its own body" f() => "";
        return "";
    }
    @error:"aready definitely specified" s = "";
    @error:"aready definitely specified" x => "";
    @error:"aready definitely specified" f() => "";
    print(s);
    print(x);
    print(f());
}