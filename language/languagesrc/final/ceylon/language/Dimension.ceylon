shared interface Dimension of Zero | Successor<Nat> {}
shared interface Zero satisfies Dimension {}
shared interface Successor<out N> satisfies Dimension given N satisfies Dimension {}