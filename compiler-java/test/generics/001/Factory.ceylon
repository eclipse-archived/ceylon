public interface Factory<T>
    given T satisfies Vehicle
{
    T newInstance(String driverName, Integer numPassengers);
}
