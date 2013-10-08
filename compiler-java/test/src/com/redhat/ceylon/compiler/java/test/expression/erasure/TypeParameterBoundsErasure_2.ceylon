@noanno
void bug1327user2(){
    Empty a0 = Bug1327NoBounds([]).t;
    Empty a1 = Bug1327Inv([]).t;
    Empty a2 = Bug1327Cov([]).t;
    Bug1327Con([]);
    Empty a3 = bug1327inv([]);
    Empty a4 = bug1327cov([]);
    Empty a5 = bug1327con([]);
    Empty a6 = Bug1327Inv2().t;
    Empty a7 = Bug1327Cov2().t;
    Bug1327Con2();
}
