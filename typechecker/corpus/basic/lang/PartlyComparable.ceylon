shared interface PartlyComparable<in T>
        given T satisfies PartlyComparable<T>
    shared formal PartialComparison compare(T other);
}