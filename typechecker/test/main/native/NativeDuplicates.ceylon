native shared Integer nativeDuplicates1();

$error native shared Integer nativeDuplicates1();

native shared Integer nativeDuplicates2();

native("jvm") shared Integer nativeDuplicates2() => 1;

$error native("jvm") shared Integer nativeDuplicates2() => 1;

native shared Integer nativeDuplicates3();

native("js") shared Integer nativeDuplicates3() => 1;

$error native("js") shared Integer nativeDuplicates3() => 1;

native Integer nativeDuplicates4();

native("jvm") Integer nativeDuplicates4() => 1;

$error native("jvm") Integer nativeDuplicates4() => 1;

native shared Integer nativeDuplicates5();

native("js") shared Integer nativeDuplicates5() => 1;

$error native("js") shared Integer nativeDuplicates5() => 1;

class NativeDuplicates6() {
    native("jvm") void foo() => print("impl 1");
    $error native("jvm") void foo() => print("impl 2");
    native("js") void foo() => print("impl 1");
    $error native("js") void foo() => print("impl 2");
}

class NativeDuplicates7() {
    native("jvm") void foo() => print("impl 1");
    $error native("jvm") void foo() => print("impl 2");
    $error native("jvm") void foo() => print("impl 3");
    native("js") void foo() => print("impl 1");
    $error native("js") void foo() => print("impl 2");
    $error native("js") void foo() => print("impl 3");
}
