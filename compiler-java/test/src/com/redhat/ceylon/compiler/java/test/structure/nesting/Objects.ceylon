@nomodel
class Objects() {
    abstract class Dummy() {}
    object objects_o extends Dummy() {
        class DummyC() {
        }
        interface DummyI {
        }
    }
    class ObjectsC() {
        object objectsc_o extends Dummy() {
        }
        class ObjectsCC() {
            object objectscc_o extends Dummy() {
            }
        }
    }
    
    interface ObjectsI {
        class ObjectsIC() {
            object objectsic_o extends Dummy() {
            }
        }
    }
    
    void local() {
        object local_o extends Dummy() {
        }
        try {
            object local_o2 extends Dummy() {
            }
        } finally {}
    }
}
