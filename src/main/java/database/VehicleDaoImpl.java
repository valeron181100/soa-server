package database;

import model.FuelType;
import model.Vehicle;
import model.VehicleFields;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.postgresql.util.PSQLException;

import java.util.List;

public class VehicleDaoImpl implements VehicleDao{

    @Override
    public Vehicle findById(int id) throws HibernateException {
        Vehicle vehicle = null;
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        vehicle = session.get(Vehicle.class, id);
        session.close();
        return vehicle;
    }

    @Override
    public void save(Vehicle vehicle) throws HibernateException {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.save(vehicle);
        tx1.commit();
        session.close();
    }

    @Override
    public void update(Vehicle vehicle) throws HibernateException {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.update(vehicle);
        tx1.commit();
        session.close();
    }

    @Override
    public void delete(int id) throws HibernateException {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        Vehicle vehicle = session.get(Vehicle.class, id);
        session.delete(vehicle);
        tx1.commit();
        session.close();
    }

    @Override
    public void deleteAll(FuelType fuelType) throws HibernateException {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        Query query = session.createQuery("delete Vehicle where fuelType = :vehicleFuelType");
        query.setParameter("vehicleFuelType", fuelType);
        query.executeUpdate();
        tx1.commit();
        session.close();
    }

    @Override
    public List<Vehicle> findAll(VehicleFields sortField, boolean isDistinctOrder, String filters) throws HibernateException {
        if (filters == null)
            filters = "";
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        String orderStr = isDistinctOrder ? " desc " : " ";
        List<Vehicle> vehicles = (List<Vehicle>) session.createQuery("From Vehicle " + filters + " order by " + sortField.getFieldName() + orderStr).list();
        session.close();
        return vehicles;
    }

    @Override
    public List<Vehicle> findAll(Integer startIndex, Integer maxResults, VehicleFields sortField, boolean isDistinctOrder, String filters) throws HibernateException {
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
        session.close();
        return vehicles;
    }

    @Override
    public Long totalCount() throws HibernateException {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Query query= session.createQuery("select count(*) from Vehicle");
        Long result = (Long)query.uniqueResult();
        session.close();
        return result;
    }

    @Override
    public double countAvgNumberOfWheels() throws HibernateException {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Query query= session.createQuery("select avg(numberOfWheels) from Vehicle");
        double result = (double)query.uniqueResult();
        session.close();
        return result;
    }

    @Override
    public List<Vehicle> findByName(String nameInput, VehicleFields sortField, boolean isDistinctOrder) throws HibernateException {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        String orderStr = isDistinctOrder ? " desc" : "";
        List<Vehicle> vehicles = (List<Vehicle>) session.createQuery("From Vehicle where name like '" + nameInput + "%'" + " order by " + sortField.getFieldName() + orderStr).list();
        session.close();
        return vehicles;
    }

    @Override
    public List<Vehicle> findByName(String nameInput, Integer startIndex, Integer maxResults, VehicleFields sortField, boolean isDistinctOrder) throws HibernateException {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        String orderStr = isDistinctOrder ? " desc" : "";
        String queryString = "From Vehicle where name like '" + nameInput + "%'" + " order by " + sortField.getFieldName() + orderStr;
        Query query = session.createQuery(queryString);
        if (startIndex != null)
            query.setFirstResult(startIndex);
        if (maxResults != null)
            query.setMaxResults(maxResults);
        List<Vehicle> vehicles = (List<Vehicle>) query.list();
        session.close();
        return vehicles;
    }
}
