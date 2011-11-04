@nomodel 
variable Natural f := 2;

@nomodel
shared void bug148_2() {
    print(f.string);
    f := 3;
}
