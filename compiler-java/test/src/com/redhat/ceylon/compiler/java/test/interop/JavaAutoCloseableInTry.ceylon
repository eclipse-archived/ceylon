import java.lang{AutoCloseable}

void javaAutoCloseableInTry() {
    AutoCloseable c =  nothing;
    try (c) {
        print("try");
    }
    
    try (c) {
        print("try/catch");
    } catch (Exception e) {
        print("try/catch (catch)");
    }
    
    try (c) {
        print("try/finally");
    } finally {
        print("try/finally (finally)");
    }
    
    try (c) {
        print("try/catch/finally");
    } catch(Exception e) {
        print("try/catch/finally (catch)");
    }finally {
        print("try/catch/finally (finally)");
    }
}