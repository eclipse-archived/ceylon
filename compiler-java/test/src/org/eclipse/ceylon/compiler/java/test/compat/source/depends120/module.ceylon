"A module to be compiled and run 
 with a compiler > 1.2.0 which depends 
 on a module compiled using 1.2.0."
native("jvm") module depends120 "1.0.0" {
    import compat120 "1.0.0";
}