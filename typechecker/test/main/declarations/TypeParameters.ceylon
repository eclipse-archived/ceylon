class BrokenClass1<T>() @error given S satisfies String {}
class BrokenClass2<T>() @error given S satisfies MissingType {}
class BrokenClass3<T>() @error given T satisfies MissingType {}
@error MissingType brokenMethod1<T>() @error given S satisfies MissingType { throw; }
String brokenMethod2<T>() @error given S satisfies String { throw; }
@error MissingType brokenMethod3<T>() @error given T satisfies MissingType { throw; }
