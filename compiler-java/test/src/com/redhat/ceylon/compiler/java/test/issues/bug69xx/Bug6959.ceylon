shared void bug6959() {
    assert (exists Resource resource 
        = `module`.resourceByPath("/com/redhat/ceylon/compiler/java/test/issues/bug69xx/bug6959.properties"));
    String text = resource.textContent();
    print(text);
    assert (exists Resource resource2 
        = `module`.resourceByPath("bug69xx/bug6959.properties"));
    String text2 = resource2.textContent();
    print(text2);
}