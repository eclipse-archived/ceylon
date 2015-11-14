import ceylon.language.meta { modules }

@test
shared void testResources() {
    assert(exists mod = modules.find("default","unversioned"));
    value res1 = mod.resourceByPath("resource.txt");
    value res2 = mod.resourceByPath("/resource.txt");
    check(res1 exists, "resource by relative path");
    check(res2 exists, "resource by full path");
    if (exists r1=res1, exists r2=res2) {
        check("/" in r1.uri, "resource uri 1");
        if (runtime.name=="jvm") {
            check(r1.uri.startsWith("classpath:/"), "resource uri 2 doesn't start with 'classpath': ``r1.uri``");
        } else {
            check(r1.uri.startsWith("file:/"), "resource uri 2 doesn't start with 'file': ``r1.uri``");
        }
        check(!"\\" in r1.uri, "resource uri 3");
        check(r1.name=="resource.txt", "resource name 1");
        check(r1.name==r2.name, "resource name 2");
        check(r1.size==15, "resource size 1");
        check(r2.size==r1.size, "resource size 2");
        value txt = r1.textContent();
        check(txt=="A resource test", "resource content");
    } else {
        fail("resource.txt or /resource.txt not found");
    }
    //Now for something inside a CAR
    if (runtime.name=="jvm") {
        if (exists carr = mod.resourceByPath("/META-INF/mapping.txt")) {
            check(carr.name=="mapping.txt", "car resource name");
            check(carr.uri.endsWith("!META-INF/mapping.txt"), "car resource uri 1");
            check(carr.uri.startsWith("classpath:/"), "car resource uri 2");
            check(carr.size>1000, "car resource size");
            check(carr.textContent().size==carr.size, "car resource content");
        } else {
            fail("Couldn't find mapping.txt file in default.car using absolute path");
        }
        if (exists carr = mod.resourceByPath("META-INF/mapping.txt")) {
        } else {
            fail("Couldn't find mapping.txt file in default.car using relative path");
        }
    }
}
