"A module to test documenting JS modules elements
 that import third-party NPM modules"
native("js")
module org.eclipse.ceylon.ceylondoc.test.modules.importnpm "1.0.0" {
    shared import npm:mithril "1.1.6";
}
