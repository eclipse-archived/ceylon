@nomodel
class MangledMembers_class() {
    //shared String equals(Void other) {
    //    return "";
    //}
    shared String clone() {
        return "";
    }
    shared String hashCode() {
        return "";
    }
    shared Boolean toString() {
        return true;
    }
    shared String notify() {
        return "";
    }
    shared String notifyAll() {
        return "";
    }
    shared String getClass() {
        return "";
    }
    shared String finalize() {
        return "";
    }
}
@nomodel
class MangledMembers_clone()  {
    shared String clone => "";
}
@nomodel 
interface MangledMembers_interface {
    //shared formal String equals(Void other);
    shared formal String clone();
    shared formal String hashCode();
    shared formal Boolean toString();
    shared formal String notify();
    shared formal String notifyAll();
    shared formal String getClass();
    shared formal String finalize();
}