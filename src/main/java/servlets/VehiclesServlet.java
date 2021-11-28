package servlets;

import database.FilterQueryBuilder;
import database.VehicleDao;
import database.VehicleDaoImpl;
import model.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import xml.XMLConverter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import javax.xml.bind.JAXBException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@WebServlet(urlPatterns = {"/api/vehicles", "/api/vehicles/*"})
public class VehiclesServlet extends MyServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        VehicleDaoImpl dao = new VehicleDaoImpl();
        String startIndex = req.getParameter("from_index");
        String maxResults = req.getParameter("max_results");
        String sortField = req.getParameter("sort_by");
        String orderStr = req.getParameter("order_desc");
        String filtersParam = req.getParameter("filters");
        String filters = null;
        if (filtersParam != null) {
            filtersParam = URLDecoder.decode(filtersParam, StandardCharsets.UTF_8.name());
            if (!filtersParam.equals("{}")) {

                JSONObject filtersObj = null;

                try {
                    filtersObj = new JSONObject(filtersParam);
                } catch (JSONException e) {
                    resp.sendError(400, "Invalid JSON filters");
                    return;
                }

                FilterQueryBuilder builder = new FilterQueryBuilder();
                for (int i = 0; i < filtersObj.keySet().toArray().length; i++) {
                    String key = (String) filtersObj.keySet().toArray()[i];
                    VehicleFields field = VehicleFields.fromFieldName(key);
                    if (field == null) {
                        resp.sendError(400, "Filters url has no field with name " + key);
                        return;
                    }
                    builder.addFilter(field, filtersObj.getString(field.getFieldName()));
                }
                filters = builder.buildQuery();
            }
        }

        boolean isOrderDesc = false;
        if (orderStr != null)
            isOrderDesc = true;
        if (sortField == null)
            sortField = "CREATION_DATE";
        String[] pathParams = null;
        if (req.getPathInfo() != null) {
            pathParams = req.getPathInfo().split("/");
        }

        resp.setContentType("application/xml");
        try (PrintWriter out = resp.getWriter()) {
            if (pathParams != null && pathParams.length > 1 && !pathParams[1].equals("")) {
                try {
                    Vehicle vehicle = dao.findById(Integer.parseInt(pathParams[1]));
                    out.println(XMLConverter.convert(vehicle));
                } catch (IllegalArgumentException e) {
                    resp.sendError(400, "Invalid query parameter");
                }
            } else {
                List<Vehicle> vehicles = null;
                if ((startIndex != null && !startIndex.isEmpty()) || (maxResults != null && !maxResults.isEmpty())) {
                    try {
                        if ((startIndex != null && !startIndex.isEmpty()) && (maxResults != null && !maxResults.isEmpty())) {
                            vehicles = dao.findAll(Integer.parseInt(startIndex), Integer.parseInt(maxResults), VehicleFields.valueOf(sortField), isOrderDesc, filters);
                        } else if ((startIndex != null && !startIndex.isEmpty()))
                            vehicles = dao.findAll(Integer.parseInt(startIndex), null, VehicleFields.valueOf(sortField), isOrderDesc, filters);
                        else
                            vehicles = dao.findAll(null, Integer.parseInt(maxResults), VehicleFields.valueOf(sortField), isOrderDesc, filters);
                    } catch (IllegalArgumentException e) {
                        resp.sendError(400, "Invalid query parameter");
                    }
                } else
                    try {
                        vehicles = dao.findAll(VehicleFields.valueOf(sortField), isOrderDesc, filters);
                    } catch (IllegalArgumentException e) {
                        resp.sendError(400, "Invalid query parameter");
                    }

                Vehicles vehiclesXmlList = new Vehicles();
                vehiclesXmlList.setVehicle(vehicles);
                Long total = dao.totalCount();
                vehiclesXmlList.setTotalCount(total.intValue());

                out.println(XMLConverter.convert(vehiclesXmlList));
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BufferedReader b = new BufferedReader(new InputStreamReader(req.getInputStream()));
        StringBuffer workBuffer = new StringBuffer();
        String workString;
        while ((workString = b.readLine()) != null) {
            workBuffer.append(workString);
        }
        workString = workBuffer.toString();

        Vehicle vehicle = null;
        try {
            vehicle = XMLConverter.convertToJava(workString);
        } catch (JAXBException e) {
            resp.sendError(400, e.getCause().getMessage());
            return;
        }
        VehicleDaoImpl dao = new VehicleDaoImpl();
        try {
            dao.save(vehicle);
        } catch (ConstraintViolationException e) {
            resp.sendError(400, "The data does not meet the database constraints");
        } catch (NullPointerException e) {
            ArrayList<Field> fields = new ArrayList<Field>(Arrays.asList(vehicle.getClass().getFields()));
            StringBuilder response = new StringBuilder("Following fields cannot be empty: ");
            Vehicle finalVehicle = vehicle;
            fields.forEach(field -> {
                boolean access = field.isAccessible();
                field.setAccessible(true);
                try {
                    if (!field.getName().equals("fuelType") && !field.getName().equals("type") && field.get(finalVehicle) == null)
                        response.append(field.getName()).append(", ");
                    field.setAccessible(access);
                } catch (IllegalAccessException illegalAccessException) {
                    illegalAccessException.printStackTrace();
                }
            });

            resp.sendError(400, response.toString());
        }
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


    @Override
    public void doPatch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BufferedReader b = new BufferedReader(new InputStreamReader(req.getInputStream()));
        StringBuffer workBuffer = new StringBuffer();
        String workString;
        while ((workString = b.readLine()) != null) {
            workBuffer.append(workString);
        }
        workString = workBuffer.toString();

        Vehicle vehicle = null;
        try {
            vehicle = XMLConverter.convertToJava(workString);
        } catch (JAXBException e) {
            resp.sendError(400, e.getCause().getMessage());
            return;
        }
        VehicleDaoImpl dao = new VehicleDaoImpl();
        try {
            dao.update(vehicle);
        } catch (ConstraintViolationException e) {
            resp.sendError(400, "The data does not meet the database constraints");
        }
    }
}
