import ceylon.language.metamodel { modules }

void bug1196<T>(T t) {
    print(`List<T>`);
}

void bug1196test(){
    bug1196(true then 1 else 1.0);
}