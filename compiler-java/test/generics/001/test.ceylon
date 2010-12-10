void test(Process p) {
    Collection<Vehicle> myVehicles = Collection<Vehicle>();
    CarFactory audi = CarFactory();
    MotorbikeFactory ducati = MotorbikeFactory();

    myVehicles.add(audi.newInstance("Andrew", 4));
    myVehicles.add(ducati.newInstance("Gavin"));

    mutable Iterator<Vehicle> iter = myVehicles.iterator();
    while (exists Vehicle foo = iter.head()) {
        p.writeLine("" foo.driver() "\'s car carries " foo.passengers() 
                    " passengers");
        iter := iter.tail();
    }
}
