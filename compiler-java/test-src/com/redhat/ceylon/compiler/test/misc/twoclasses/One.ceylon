class One () satisfies Two {
 shared actual void f(Process process){
  process.writeLine("Hello World from two classes");
 }
}