public class MotorbikeImpl(String driver, Integer passengers) 
   satisfies Motorbike
{
    mutable Integer numPassengers = passengers;

    public void setPassenegers(Integer i) {
        numPassengers := i;
    }

    public Integer passengers() {
        return numPassengers;
    }

    public String driver() {
        return driver;
    }
}
