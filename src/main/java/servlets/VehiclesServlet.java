package servlets;

import database.FilterQueryBuilder;
import database.VehicleDao;
import database.VehicleDaoImpl;
import model.*;
import org.json.JSONArray;
import org.json.JSONObject;
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
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@WebServlet(urlPatterns = {"/vehicles", "/vehicles/*"})
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
                JSONObject filtersObj = new JSONObject(filtersParam);
                FilterQueryBuilder builder = new FilterQueryBuilder();
                filtersObj.keySet().forEach(key -> {
                    VehicleFields field = VehicleFields.fromFieldName(key);
                    builder.addFilter(field, filtersObj.getString(field.getFieldName()));
                });
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
                Vehicle vehicle = dao.findById(Integer.parseInt(pathParams[1]));
                out.println(XMLConverter.convert(vehicle));
            } else {
                List<Vehicle> vehicles;
                if ((startIndex != null && !startIndex.isEmpty()) || (maxResults != null && !maxResults.isEmpty())) {
                    if ((startIndex != null && !startIndex.isEmpty()) && (maxResults != null && !maxResults.isEmpty())) {
                        vehicles = dao.findAll(Integer.parseInt(startIndex), Integer.parseInt(maxResults), VehicleFields.valueOf(sortField), isOrderDesc, filters);
                    }
                    else if ((startIndex != null && !startIndex.isEmpty()))
                        vehicles = dao.findAll(Integer.parseInt(startIndex), null, VehicleFields.valueOf(sortField), isOrderDesc, filters);
                    else
                        vehicles = dao.findAll(null, Integer.parseInt(maxResults), VehicleFields.valueOf(sortField), isOrderDesc, filters);
                }
                else
                    vehicles = dao.findAll(VehicleFields.valueOf(sortField), isOrderDesc, filters);

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


    @Override
    public void doPatch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BufferedReader b = new BufferedReader(new InputStreamReader(req.getInputStream()));
        StringBuffer workBuffer = new StringBuffer();
        String workString;
        while((workString = b.readLine()) != null) {
            workBuffer.append(workString);
        }
        workString = workBuffer.toString();

        Vehicle vehicle = XMLConverter.convertToJava(workString);
        VehicleDaoImpl dao = new VehicleDaoImpl();
        dao.update(vehicle);
    }
}
