void withNestedImport() {
    import ceylon.language.meta { type }
    import ceylon.language { 
        String { len=size, sub=substring }, 
        Int=Integer,
        operatingSystem { fs=fileSeparator }
    }
    print(type(""));
    print("".len);
    print("".sub(0, 2));
    Int i = 0;
    print(fs);
}

void withoutImport() {
    @error print(type(""));
    @error print("".len);
    @error print("".sub(0, 2));
    @error Int i = 0;
    @error print(fs);
}