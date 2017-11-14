"Some module doc"
module metamodel "0.1" {
    import check "0.1";
    import modules.imported "1";
    optional import modules.optional "1";
    import modules.required "1";
}
