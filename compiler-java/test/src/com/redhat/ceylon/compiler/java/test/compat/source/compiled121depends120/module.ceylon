"This module is compiled with the 1.2.1 compiler but
 has a dependency on a module compiled with the 1.2.0 compiler"
native("jvm") module compiled121depends120 "1.0.0" {
    import compiled120 "1.0.0";
}