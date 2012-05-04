doc "Thrown when name could not be initialized due to recursive access during initialization."
shared class RecursiveInitializationException()
        extends Exception("Name could not be initialized due to recursive access during initialization", null) {
}