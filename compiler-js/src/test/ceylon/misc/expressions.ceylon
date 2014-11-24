import check { check }

Iterator<T> objectExpression1<T>(T element)
  => object satisfies Iterator<T> {
       variable T|Finished e=element;
       shared actual T|Finished next() {
         value c=e;
         e=finished;
         return c;
       }
};

void testObjectExpressions() {
  check(objectExpression1("Hey").next()=="Hey", "Object expressions 1");
  check((object {
    hash=449;
  }).hash==449, "Object expressions 2");
  check("``(object {
    string="HEY";
  })``"=="HEY", "Object expressions 3");
}

void testIfExpressions() {
  for (i in 1..4) {
    check(if (i%2==0) then true else i%2==1, "If expressions 1");
  }
  for (i in ["Hey", null]) {
    check(if (is String i) then i.uppercased=="HEY" else true, "If expressions 2");
    check(if (exists i) then i.reversed=="yeH" else true, "If expressions 3");
  }
  Object obj="Hey";
  //TODO wasn't the "else" optional?
  check(!(if (is Integer obj) then obj.successor else null) exists, "If expressions 4");
  check(if (is String obj, exists c=obj[2], c=='y') then true else false, "If expression with multiple conditions");
  check(if (obj=="HEY") then false else if (obj==1) then false else true, "If-else-if expression 2");
  check(if (is Integer obj) then false else if (is String obj, exists c=obj[2], c=='y') then true else false, "if-else-if expression 2");
  check(if (obj=="nay") then false else if (is String obj, exists c=obj[2], c=='y') then true else false, "if-else-if expression 3");
}

Boolean testSwitchExpression(Anything x) =>
  switch(x)
  case (is Integer) x==1
  case (is Null) false
  else x is String;
