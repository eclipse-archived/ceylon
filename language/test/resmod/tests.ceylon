import check { check, results, fail }
import ceylon.language.meta { modules }

@test
shared void testModResources() {
    assert(exists mod = modules.find("resmod","0.1"));
    value res1 = mod.resourceByPath("modres.txt");
    value res2 = mod.resourceByPath("/resmod/modres.txt");
    check(res1 exists, "mod resource by relative path");
    check(res2 exists, "mod resource by full path");
    assert(exists r1=res1, exists r2=res2);
    check("/" in r1.uri, "mod resource uri 1");
    if(runtime.name == "jvm"){
        check(r1.uri.startsWith("classpath:/"), "mod resource uri 2");
    }else if(runtime.name == "node.js"){
        check(r1.uri.startsWith("file:/"), "mod resource uri 2");
    }
    check(!"\\" in r1.uri, "mod resource uri 3");
    check(r1.name=="modres.txt", "mod resource name 1");
    check(r1.name==r2.name, "mod resource name 2");
    check(r1.size==15, "mod resource size 1");
    check(r2.size==r1.size, "mod resource size 2");
    value txt = r1.textContent();
    check(txt=="A resource test", "mod resource content");
    //Now for something inside a CAR
    if (runtime.name=="jvm") {
        if (exists carr = mod.resourceByPath("/META-INF/mapping.txt")) {
            check(carr.name=="mapping.txt", "mod car resource name");
            check(carr.uri.endsWith("!META-INF/mapping.txt"), "mod car resource uri 1");
            check(carr.uri.startsWith("classpath:/"), "mod car resource uri 2");
            check(carr.size>100, "mod car resource size");
            check(carr.textContent().size==carr.size, "mod car resource content");
        } else {
            fail("Couldn't find mapping.txt file in resmod-0.1.car");
        }
        if (exists carr = mod.resourceByPath("META-INF/mapping.txt")) {
            fail("Shouldn't be able to find mapping.txt file in default.car using relative path");
        }
    }
}
shared void run() {
    testModResources();
}
shared void test() { run(); }

