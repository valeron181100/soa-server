package database;

import model.FuelType;
import model.Vehicle;
import model.VehicleFields;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public interface VehicleDao {
    public Vehicle findById(int id);
    public void save(Vehicle vehicle);
    public void update(Vehicle vehicle);
    public void delete(int id);
    public void deleteAll(FuelType fuelType);
    public List<Vehicle> findAll(VehicleFields sortField, boolean isDistinctOrder, String filters);
    public List<Vehicle> findAll(Integer startIndex, Integer maxResults, VehicleFields sortField, boolean isDistinctOrder, String filters);
    public Long totalCount();
    public double countAvgNumberOfWheels();
    public List<Vehicle> findByName(String nameInput, VehicleFields sortField, boolean isDistinctOrder);
    public List<Vehicle> findByName(String nameInput, Integer startIndex, Integer maxResults, VehicleFields sortField, boolean isDistinctOrder);
}
