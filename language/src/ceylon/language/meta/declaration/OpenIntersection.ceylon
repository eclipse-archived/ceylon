"An open intersection type."
shared sealed interface OpenIntersection satisfies OpenType {
    
    "This intersection's list of satisfied open types."
    shared formal List<OpenType> satisfiedTypes;
}
