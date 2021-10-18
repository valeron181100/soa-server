package database;

import model.Vehicle;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class VehicleDaoImpl {

    public Vehicle findById(int id) {
        Vehicle vehicle = null;
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            vehicle = session.get(Vehicle.class, id);
        }
        return vehicle;
    }

    public void save(Vehicle vehicle) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.save(vehicle);
        tx1.commit();
        session.close();
    }

    public void update(Vehicle vehicle) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.update(vehicle);
        tx1.commit();
        session.close();
    }

    public void delete(int id) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        Vehicle vehicle = session.get(Vehicle.class, id);
        session.delete(vehicle);
        tx1.commit();
        session.close();
    }

    public List<Vehicle> findAll() {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        List<Vehicle> vehicles = (List<Vehicle>)  HibernateSessionFactoryUtil.getSessionFactory().openSession().createQuery("From Vehicle").list();
        return vehicles;
    }

}
