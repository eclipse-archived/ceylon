void bug1953() {
    <String->{<[String]->String>+}>
    |
    <String->{<[String, String]->String>+}> x = ""->{ [""]->"" };
    value it = x.key.split();
}