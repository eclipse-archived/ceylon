T f<T>() => nothing;
void bug6096() {
    value y = 1 * sum({ 1.0 });
    value z = 1 * sum { 1.0 };
    value x = 1 ^ sum { 1.0 };
}