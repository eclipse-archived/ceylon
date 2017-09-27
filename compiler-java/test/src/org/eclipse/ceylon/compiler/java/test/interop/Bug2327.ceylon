void bug2327() {
    @error:"class cannot be instantiated: 'Bug2327Java' does not have a default constructor"
    Bug2327Java inst = Bug2327Java();
}