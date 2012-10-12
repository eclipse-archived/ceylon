@nomodel
class AssertVariableScopes(Object x, y) {
    Object y;
    
    void m() {
        void m_local() {
            assert(is String x);
            x.terminal(1);
            assert(is String y);
            y.terminal(1);
        }
        
        void masking_local(Object x, y) {
            Object y;
            assert(is String x);
            x.terminal(1);
            assert(is String y);
            y.terminal(1);
        }
        
        assert(is String x);
        x.terminal(1);
        assert(is String y);
        y.terminal(1);
    }
    void masking(Object x, y) {
        void m_local() {
            assert(is String x);
            x.terminal(1);
            assert(is String y);
            y.terminal(1);
        }
        void masking_local(Object x, y) {
            Object y;
            assert(is String x);
            x.terminal(1);
            assert(is String y);
            y.terminal(1);
        }
        
        Object y;
        assert(is String x);
        x.terminal(1);
        assert(is String y);
        y.terminal(1);
    }
    
    assert(is String x);
    x.terminal(1);
    assert(is String y);
    y.terminal(1);
}
