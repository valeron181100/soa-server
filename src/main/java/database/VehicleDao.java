package database;

import model.Vehicle;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public interface VehicleDao {
    public Vehicle findById(int id);
    public void save(Vehicle vehicle);
    public void update(Vehicle vehicle);
    public void delete(Vehicle vehicle);
    public List<Vehicle> findAll();
}
