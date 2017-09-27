
@noanno
shared void bug7125() {
    value sub = Bug7125Java.Sub();
    
    Bug7125Java.C x = sub.foo(Bug7125Java.A());
    Bug7125Java.C y = sub.foo(Bug7125Java.B() of Bug7125Java.A); 
}
