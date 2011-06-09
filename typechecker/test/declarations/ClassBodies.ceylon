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
    
    class Good3WithCircular() {
        String name = "gavin";
        void x() { this.y(); }
        void y() { this.x(); }
    }
    
    object good3WithCircular {
        String name = "gavin";
        void x() { this.y(); }
        void y() { this.x(); }
    }
    
    class Good4WithCircular() {
        void x() { this.y(); }
        void y() { this.x(); }
    }
    
    object good4WithCircular {
        void x() { this.y(); }
        void y() { this.x(); }
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
    
    class Bad2WithCircular() {
        void x() { @error this.y(); }
        void y() { this.x(); }
        String name = "gavin";
    }
    
    object bad2WithCircular {
        void x() { @error this.y(); }
        void y() { this.x(); }
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
    
    class GoodWithInner() {
        String name = "gavin";
        class Inner() {
            x();
        }
        String x() { return "Hell"; }
    }
    
    class BadWithInner() {
        class Inner() {
            @error x();
        }
        String x() { return "Hell"; }
        String name = "gavin";
    }
    
    class Good2WithInner() {
        String name = "gavin";
        class Inner() {
            void y() {
                x();
            }
        }
        String x() { return "Hell"; }
    }
    
    class Bad2WithInner() {
        class Inner() {
            void y() {
                @error x();
            }
        }
        String x() { return "Hell"; }
        String name = "gavin";
    }
    
    object goodWithInner {
        String name = "gavin";
        class Inner() {
            x();
        }
        String x() { return "Hell"; }
    }
    
    object badWithInner {
        class Inner() {
            @error x();
        }
        String x() { return "Hell"; }
        String name = "gavin";
    }
    
    object good2WithInner {
        String name = "gavin";
        class Inner() {
            void y() {
                x();
            }
        }
        String x() { return "Hell"; }
    }
    
    object bad2WithInner {
        class Inner() {
            void y() {
                @error x();
            }
        }
        String x() { return "Hell"; }
        String name = "gavin";
    }
    
}