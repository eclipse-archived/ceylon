import ceylon.collection{HashMap}

void bug6068() {
    noop(HashMap { 1->1 }.keys);
}