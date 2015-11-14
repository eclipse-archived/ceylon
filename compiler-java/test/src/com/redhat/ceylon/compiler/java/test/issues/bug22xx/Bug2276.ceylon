@noanno
Float bug2276_accumulator(){
    Boolean b = true;
    return 1.0 + (switch(b)
                case (true) 1.0
                case (false) 0.0);
}

@noanno
shared void bug2276() {
    bug2276_accumulator();
}
