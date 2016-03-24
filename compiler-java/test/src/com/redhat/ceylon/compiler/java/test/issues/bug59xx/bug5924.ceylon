
class Bug5924 {
    
    String socket;
    Integer writer;
    
    shared new connect(Integer port) {
        String? host = null;
        variable Exception? lastException = null;
        
        for (value i in 0..10) {
            try {
                value s = "";
                value w = 1;
                socket = s;
                writer = w;
                break;
            } catch (Exception e) {
                lastException = e;
            }
        }
        else {
            throw Exception("failed connect to port ``port``", lastException);
        }
        
    }
    
    shared void write(String data) {
        print(writer);
    }
    
    shared void close() {
        try {
            print(writer);
        } catch (Exception e) {
            // noop
        }
        try {
            print(socket);
        } catch (Exception e) {
            // noop
        }
    }
}