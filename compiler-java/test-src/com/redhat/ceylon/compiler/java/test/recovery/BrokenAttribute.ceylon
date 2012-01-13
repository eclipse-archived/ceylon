@error MissingType brokenGetter {
    @error fuuuuuu()();
}

@error MissingType brokenAttribute = fuuuuuu()();

assign brokenGetter {
    @error fuuuuuu()();
}

@error
MissingType obj {
    fu=bar;
}