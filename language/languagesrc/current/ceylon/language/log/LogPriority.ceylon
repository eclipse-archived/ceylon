shared object trace extends LogPriority("Trace", 100) {}
shared object info extends LogPriority("Info", 200) {}
shared object warn extends LogPriority("Warn", 300) {}
shared object error extends LogPriority("Error", 400) {}

doc "The severity of a log message."
shared abstract class LogPriority(String string, Natural priority) {
    shared actual String string = string;
    shared Natural priority = priority;
}