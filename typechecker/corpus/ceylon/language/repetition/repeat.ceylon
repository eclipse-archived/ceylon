doc "Repeat the block the given number of times."
shared void repeat(Natural repetitions, void times()) {
    variable Natural n:=0;
    while (n<repetitions) {
        times();
        n++;
    }
}