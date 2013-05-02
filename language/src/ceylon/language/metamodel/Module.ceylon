shared interface Module satisfies Identifiable {
    shared formal String name;
    shared formal String version;
    
    shared formal Package[] members;
}