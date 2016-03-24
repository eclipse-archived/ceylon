"This module is compiled with the 1.2.99 compiler but
 has a dependency on a module compiled with the 1.2.1 compiler"
native("jvm") module compiled1299depends121 "1.0.0" {
    import compiled121 "1.0.0";
}