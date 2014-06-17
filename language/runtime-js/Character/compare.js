function(other) {
    return this.value===other.value ? getEqual()
                                    : (this.value<other.value ? getSmaller():getLarger());
}
