@nomodel
shared class MutableList<T>(){

    shared T item(Cell cell) {
        return cell.car;
    }
    
    shared class Cell(T car, Cell? cdr) {
        shared T car = car;
        shared Cell? cdr = cdr;
    }
}
