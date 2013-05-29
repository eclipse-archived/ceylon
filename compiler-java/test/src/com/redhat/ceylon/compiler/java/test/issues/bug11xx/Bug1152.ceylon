@noanno
void bug1152() {
    void enum(){
        void g(){
            enum();
        }
        enum();
    }
    enum();
    
    Integer getter {
        Integer g {
            return getter + g;
        }
        return getter + g;
    }
    Integer g = getter;
}