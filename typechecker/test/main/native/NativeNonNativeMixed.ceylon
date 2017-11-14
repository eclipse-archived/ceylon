shared class NativeNonNativeMixed1() {}
$error native("jvm") shared class NativeNonNativeMixed1() {}
$error native("js") shared class NativeNonNativeMixed1() {}

shared variable String nativeNonNativeMixed2 = "Foo";
$error native shared variable String nativeNonNativeMixed2;
$error native("jvm") shared variable String nativeNonNativeMixed2 = "Bar";
$error native("js") shared variable String nativeNonNativeMixed2 = "Baz";

native shared variable String nativeNonNativeMixed3;
$error shared variable String nativeNonNativeMixed3 = "Foo";
native("jvm") shared variable String nativeNonNativeMixed3 = "Bar";
native("js") shared variable String nativeNonNativeMixed3 = "Baz";
