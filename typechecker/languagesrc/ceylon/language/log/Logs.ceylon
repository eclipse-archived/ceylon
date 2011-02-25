shared extension class Logs(Log this) {
    shared void debug(Gettable<String> message) { this.log(debug, message); }
    shared void debug(String message) { this.log(debug, message); }
    shared void info(Gettable<String> message) { this.info(debug, message); }
    shared void info(String message) { this.info(debug, message); }
    shared void warn(Gettable<String> message) { this.warn(debug, message); }
    shared void warn(String message) { this.warn(debug, message); }
    shared void error(Gettable<String> message) { this.error(debug, message); }
    shared void error(String message) { this.error(debug, message); }
}