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
    if (1==1) {
        import ceylon.language.meta.model { 
            Int=Interface, 
            Att=Attribute
        }
        Att<Object>? withInterface(Int<Object> i) {
            return i.getAttribute<Object>("name");
        }
    }
}

void withoutImport() {
    $error print(type(""));
    $error print("".len);
    $error print("".sub(0, 2));
    $error Int i = 0;
    $error print(fs);
}

void hiding() {
    import ceylon.language.meta { 
        $error type, 
        typeLiteral 
    }
    String type;
    if (1==1) {
        String typeLiteral = "";
    }
}