abstract class FooBar() of foo | bar {}
object foo extends FooBar() {
    shared Float y = 2.0;
}
object bar extends FooBar() {
    shared Integer x = 1;
}

void switchFooBar() {
    FooBar fb = foo;
    switch (fb) 
    case (foo) { @error print(fb.y); } //TODO: instance cases should narrow!
    case (bar) { @error print(fb.x); } //TODO: instance cases should narrow!
    switch (fb) 
    case (foo, bar) {}
    switch (fb) 
    case (foo|bar) {}
}
