public class CarFactory()
    satisfies Factory<Car>
{
    Car newInstance(String driverName, Integer numPassengers) {
        return CarImpl(driverName, numPassengers);
    }
}
