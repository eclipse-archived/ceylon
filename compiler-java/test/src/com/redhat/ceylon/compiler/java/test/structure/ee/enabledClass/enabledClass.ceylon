import javax.xml.bind.annotation{xmlAccessorType}
import javax.persistence{entity}
import javax.inject{inject}


xmlAccessorType
shared class EnabledByXmlAccessorType(s) {
    shared String? s;
}

entity
shared class EnabledByEntity(s) {
    shared String? s;
}

shared class EnabledByCtorInject {
    shared String? s = "";
    inject
    shared new (String s) {
    }
}

shared class EnabledByAttrInject(s) {
    inject
    shared String? s;
}

shared class EnabledByMethodInject(s) {
    shared String? s;
    inject
    shared String m() => s else "";
}

shared class Disabled(s) {
    shared String? s;
}