native shared Integer nativeSetterInvalid;
$error assign nativeSetterInvalid;

native("jvm") shared Integer nativeSetterInvalid => 1;
$error assign nativeSetterInvalid {}

native("js") shared Integer nativeSetterInvalid => 1;
$error assign nativeSetterInvalid {}

shared Integer nativeSetterInvalid2 => 1;
$error native assign nativeSetterInvalid2;
