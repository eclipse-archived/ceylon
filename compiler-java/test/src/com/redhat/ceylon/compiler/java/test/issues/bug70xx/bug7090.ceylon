import java.util {
    Date
}

class Bug7090Test(shared Date dt) {}

shared void bug7090(){
    value attr = `Bug7090Test.dt`;
    print(attr.type.string);
}