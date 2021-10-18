package servlets;

import database.VehicleDao;
import database.VehicleDaoImpl;
import model.*;
import xml.XMLConverter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(urlPatterns = {"/vehicles", "/vehicles/*"})
public class VehiclesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        VehicleDaoImpl dao = new VehicleDaoImpl();
        String[] pathParams = null;
        if (req.getPathInfo() != null) {
            pathParams = req.getPathInfo().split("/");
        }

        resp.setContentType("application/xml");
        try (PrintWriter out = resp.getWriter()) {
            if (pathParams != null && pathParams.length > 1 && !pathParams[1].equals("")) {
                Vehicle vehicle = dao.findById(Integer.parseInt(pathParams[1]));
                out.println(XMLConverter.convert(vehicle));
            } else {
                List<Vehicle> vehicles = dao.findAll();

                Vehicles vehiclesXmlList = new Vehicles();
                vehiclesXmlList.setVehicle(vehicles);

                out.println(XMLConverter.convert(vehiclesXmlList));
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BufferedReader b = new BufferedReader(new InputStreamReader(req.getInputStream()));
        StringBuffer workBuffer = new StringBuffer();
        String workString;
        while((workString = b.readLine()) != null) {
            workBuffer.append(workString);
        }
        workString = workBuffer.toString();

        Vehicle vehicle = XMLConverter.convertToJava(workString);
        VehicleDaoImpl dao = new VehicleDaoImpl();
        dao.save(vehicle);
        int k = 0;
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        VehicleDaoImpl dao = new VehicleDaoImpl();
        String[] pathParams = null;
        if (req.getPathInfo() != null) {
            pathParams = req.getPathInfo().split("/");
        }

        if (pathParams != null && pathParams.length > 1 && !pathParams[1].equals("")) {
            dao.delete(Integer.parseInt(pathParams[1]));
        }
    }
}
