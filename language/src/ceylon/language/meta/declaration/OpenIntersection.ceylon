"An open intersection type."
shared interface OpenIntersection satisfies OpenType {
    
    "This intersection's list of satisfied open types."
    shared formal List<OpenType> satisfiedTypes;
}
