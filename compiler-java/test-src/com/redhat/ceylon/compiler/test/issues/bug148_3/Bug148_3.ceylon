@nomodel 
Natural f {return 2;}
assign f { }

@nomodel
shared void bug148_3() {
    print(f.string);
    f := 3;
}
