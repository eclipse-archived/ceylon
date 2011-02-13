shared abstract class Component() {
            
    OpenList<Observer> observers = none;
    
    shared Observer[] currentObservers {
    	return observers;
    }
    
    shared void addObserver(Observer o)() {
        observers.append(o);
        void remove() {
        	observers.remove(o);
        }
        return remove;
    }
    
    shared void fire(Event event) {
        for (Observer o in observers) {
            o.observe(event);
        }
    }
    
}