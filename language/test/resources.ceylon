import ceylon.language.meta { modules }

@test
shared void testResources() {
    assert(exists mod = modules.find("default","unversioned"));
    value res1 = mod.resourceByPath("default.src.sha1");
    value res2 = mod.resourceByPath("/default.src.sha1");
    check(res1 exists, "resource by relative path");
    check(res2 exists, "resource by full path");
    assert(exists r1=res1, exists r2=res2);
    check("/" in r1.uri, "resource uri 1");
    check(r1.uri.startsWith("file:/"), "resource uri 2");
    check(!"\\" in r1.uri, "resource uri 3");
    check(r1.name=="default.src.sha1", "resource name 1");
    check(r1.name==r2.name, "resource name 2");
    check(r1.size==40, "resource size 1");
    check(r2.size==r1.size, "resource size 2");
    value sha = r1.textContent();
    check(sha.size==40, "resource content");
    //Now for something inside a CAR
    if (runtime.name=="jvm") {
        if (exists carr = mod.resourceByPath("META-INF/mapping.txt")) {
            check(carr.name=="mapping.txt", "car resource name");
            check(carr.uri.endsWith("!META-INF/mapping.txt"), "car resource uri 1");
            check(carr.uri.startsWith("file:/"), "car resource uri 2");
            check(carr.size>1000, "car resource size");
            check(carr.textContent().size==carr.size, "car resource content");
        } else {
            fail("Couldn't find mapping.txt file in default.car");
        }
    }
}
