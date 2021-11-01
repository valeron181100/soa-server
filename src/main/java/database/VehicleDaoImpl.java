package database;

import model.FuelType;
import model.Vehicle;
import model.VehicleFields;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class VehicleDaoImpl implements VehicleDao{

    @Override
    public Vehicle findById(int id) {
        Vehicle vehicle = null;
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            vehicle = session.get(Vehicle.class, id);
        }
        return vehicle;
    }

    @Override
    public void save(Vehicle vehicle) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.save(vehicle);
        tx1.commit();
        session.close();
    }

    @Override
    public void update(Vehicle vehicle) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.update(vehicle);
        tx1.commit();
        session.close();
    }

    @Override
    public void delete(int id) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        Vehicle vehicle = session.get(Vehicle.class, id);
        session.delete(vehicle);
        tx1.commit();
        session.close();
    }

    @Override
    public void deleteAll(FuelType fuelType) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        Query query = session.createQuery("delete Vehicle where fuelType = :vehicleFuelType");
        query.setParameter("vehicleFuelType", fuelType);
        query.executeUpdate();
        tx1.commit();
        session.close();
    }

    @Override
    public List<Vehicle> findAll(VehicleFields sortField, boolean isDistinctOrder, String filters) {
        if (filters == null)
            filters = "";
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        String orderStr = isDistinctOrder ? " desc " : " ";
        List<Vehicle> vehicles = (List<Vehicle>) session.createQuery("From Vehicle " + filters + " order by " + sortField.getFieldName() + orderStr).list();
        return vehicles;
    }

    @Override
    public List<Vehicle> findAll(Integer startIndex, Integer maxResults, VehicleFields sortField, boolean isDistinctOrder, String filters) {
        if (filters == null)
            filters = "";
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        String orderStr = isDistinctOrder ? " desc" : "";
        String queryString = "From Vehicle " + filters + " order by " + sortField.getFieldName() + orderStr;
        Query query= session.createQuery(queryString);
        if (startIndex != null)
            query.setFirstResult(startIndex);
        if (maxResults != null)
            query.setMaxResults(maxResults);
        List<Vehicle> vehicles = (List<Vehicle>) query.list();
        return vehicles;
    }

    @Override
    public Long totalCount() {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Query query= session.createQuery("select count(*) from Vehicle");
        return (Long)query.uniqueResult();
    }

    @Override
    public double countAvgNumberOfWheels() {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Query query= session.createQuery("select avg(numberOfWheels) from Vehicle");
        return (double)query.uniqueResult();
    }

    @Override
    public List<Vehicle> findByName(String nameInput, VehicleFields sortField, boolean isDistinctOrder) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        String orderStr = isDistinctOrder ? " desc" : "";
        List<Vehicle> vehicles = (List<Vehicle>) session.createQuery("From Vehicle where name like '" + nameInput + "%'" + " order by " + sortField.getFieldName() + orderStr).list();
        return vehicles;
    }

    @Override
    public List<Vehicle> findByName(String nameInput, Integer startIndex, Integer maxResults, VehicleFields sortField, boolean isDistinctOrder) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        String orderStr = isDistinctOrder ? " desc" : "";
        String queryString = "From Vehicle where name like '" + nameInput + "%'" + " order by " + sortField.getFieldName() + orderStr;
        Query query = session.createQuery(queryString);
        if (startIndex != null)
            query.setFirstResult(startIndex);
        if (maxResults != null)
            query.setMaxResults(maxResults);
        List<Vehicle> vehicles = (List<Vehicle>) query.list();
        return vehicles;
    }
}
