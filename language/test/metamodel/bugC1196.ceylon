import ceylon.language.meta { modules }

void bugC1196<T>(T t) {
    print(`List<T>`);
}

@test
shared void bugC1196test(){
    bugC1196(true then 1 else 1.0);
}