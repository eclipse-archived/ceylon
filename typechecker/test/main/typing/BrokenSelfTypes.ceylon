@error abstract class Tree() satisfies Summable<Tree|Integer>{ 
    shared actual Tree plus(Tree|Integer other) {
        return Addition(this, other);
    }
    shared formal Integer evaluate();
}

class BinaryOperator(Tree|Integer left, Tree|Integer right, Integer f(Integer a, Integer b)) 
        extends Tree() {
    Integer resolve(Tree|Integer node) {
        switch(node)
        case (is Tree) {
            return node.evaluate();
        }
        case (is Integer) {
            return node;
        }
    }

    shared actual Integer evaluate() {
        return f(resolve(left), resolve(right));
    }

    shared actual default String string = "Binary(``left``, ``right``)";
}

class Multiplication(Tree|Integer left, Tree|Integer right) 
        extends BinaryOperator(left, right, (Integer a, Integer b) => a * b) {
    shared actual default String string = "(``left`` * ``right``)";
}

class Addition(Tree|Integer left, Tree|Integer right) 
        extends BinaryOperator(left, right, (Integer a, Integer b) => a + b) {
    shared actual default String string = "(``left`` + ``right``)";
}