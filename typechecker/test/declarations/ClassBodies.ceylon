interface ClassBodies {
    
    class GoodWithCircular() {
        String name = "gavin";
        void x() { y(); }
        void y() { x(); }
    }
    
    object goodWithCircular {
        String name = "gavin";
        void x() { y(); }
        void y() { x(); }
    }
    
    class Good2WithCircular() {
        void x() { y(); }
        void y() { x(); }
    }
    
    object good2WithCircular {
        void x() { y(); }
        void y() { x(); }
    }
    
    class BadWithCircular() {
        void x() { @error y(); }
        void y() { x(); }
        String name = "gavin";
    }
    
    object badWithCircular {
        void x() { @error y(); }
        void y() { x(); }
        String name = "gavin";
    }
    
    class GoodWithThis() {
        String name = "gavin";
        local get { return this; }
    }
    
    object goodWithThis {
        String name = "gavin";
        local get { return this; }
    }
    
    class BadWithThis() {
        local get { @error return this; }
        String name = "gavin";
    }
    
    object badWithThis {
        local get { @error return this; }
        String name = "gavin";
    }
    
}