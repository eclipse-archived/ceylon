native Integer nativeAttributeMismatch1;

native("jvm") Integer nativeAttributeMismatch1 => 0;

$error native("js") shared Integer nativeAttributeMismatch1 => 1;

native Integer nativeAttributeMismatch2;

$error native("jvm") String nativeAttributeMismatch2 => "";

native("js") Integer nativeAttributeMismatch2 => 1;
