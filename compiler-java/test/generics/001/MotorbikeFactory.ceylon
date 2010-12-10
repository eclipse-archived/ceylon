public class MotorbikeFactory() 
    satisfies Factory<Motorbike>
{
    Motorbike newInstance(String driverName, Integer numPassengers) {
        return MotorbikeImpl(driverName, numPassengers);
    }
    Motorbike newInstance(String driverName) {
        return MotorbikeImpl(driverName, 1);
    }
}
