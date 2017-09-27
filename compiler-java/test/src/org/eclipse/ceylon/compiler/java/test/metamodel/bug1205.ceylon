import ceylon.language.meta { modules }

@noanno
class Bug1205() {}

@noanno
void bug1205() {
    `Bug1205`();
    `bug1205`();
}
