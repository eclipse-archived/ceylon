shared class LogMessage(LogPriority priority, String category, String message) {
    shared Priority priority = priority;
    shared String category = category;
    shared String message = message;
    shared Datetime datetime = currentDatetime();
}