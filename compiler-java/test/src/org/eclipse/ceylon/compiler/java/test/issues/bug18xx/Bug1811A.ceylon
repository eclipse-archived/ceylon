shared interface AbstractBug1811 {
    shared formal {String*} changes;
}
interface Bug1811A satisfies AbstractBug1811 {
    shared formal actual [String]|[] changes;
}
