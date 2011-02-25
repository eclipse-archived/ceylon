shared class Log(LogChannel channel, String category) {

    shared void log(LogPriority priority, Gettable<String> message) {
        if ( channel.enabled(priority) ) {
            send(priority, message);
        }
    }

    shared void log(LogPriority priority, String message) {
        if ( channel.enabled(priority) ) {
            send(priority, message);
        }
    }

    void send(LogPriority priority, String message) {
        channel.send( priority, LogMessage(priority, category, message) );
    }

}