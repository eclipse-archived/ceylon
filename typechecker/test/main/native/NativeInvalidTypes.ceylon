native shared Integer nativeInvalidTypes();

$error native("jvm") shared Integer nativeInvalidTypes => 1;

$error native("js") shared class \InativeInvalidTypes() {
}

$error native("foo") shared Integer nativeInvalidTypes() => 2;
