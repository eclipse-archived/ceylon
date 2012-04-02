@nomodel
shared class MutableList<T>(){

    shared T item(Cell<T> cell) {
        return cell.car;
    }
    
    shared class Cell<T>(T car, Cell<T>? cdr) {
        shared T car = car;
        shared Cell<T>? cdr = cdr;
    }
}
