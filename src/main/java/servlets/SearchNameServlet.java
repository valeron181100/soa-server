package servlets;

import database.FilterQueryBuilder;
import database.VehicleDaoImpl;
import model.Vehicle;
import model.VehicleFields;
import model.Vehicles;
import org.json.JSONObject;
import xml.XMLConverter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@WebServlet("/api/search_by_name")
public class SearchNameServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        VehicleDaoImpl dao = new VehicleDaoImpl();
        String startIndex = req.getParameter("from_index");
        String maxResults = req.getParameter("max_results");
        String sortField = req.getParameter("sort_by");
        String orderStr = req.getParameter("order_desc");
        String query = req.getParameter("q");

        if (query == null) {
            resp.sendError(400, "Query parameter was expected");
            return;
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
                } catch (NumberFormatException e) {
                    resp.sendError(400, "Invalid query parameter");
                }
            } else {
                List<Vehicle> vehicles = null;
                if ((startIndex != null && !startIndex.isEmpty()) || (maxResults != null && !maxResults.isEmpty())) {
                    if ((startIndex != null && !startIndex.isEmpty()) && (maxResults != null && !maxResults.isEmpty())) {
                        try {
                            vehicles = dao.findByName(query, Integer.parseInt(startIndex), Integer.parseInt(maxResults), VehicleFields.valueOf(sortField), isOrderDesc);
                        } catch (NumberFormatException e) {
                            resp.sendError(400, "Invalid query parameter");
                        }
                    }
                    else if ((startIndex != null && !startIndex.isEmpty()))
                        try {
                            vehicles = dao.findByName(query, Integer.parseInt(startIndex), null, VehicleFields.valueOf(sortField), isOrderDesc);
                        } catch (IllegalArgumentException e) {
                            resp.sendError(400, "Invalid query parameter");
                        }
                    else
                        try {
                            vehicles = dao.findByName(query, null, Integer.parseInt(maxResults), VehicleFields.valueOf(sortField), isOrderDesc);
                        } catch (IllegalArgumentException e) {
                            resp.sendError(400, "Invalid query parameter");
                        }
                }
                else
                    try {
                        vehicles = dao.findByName(query, VehicleFields.valueOf(sortField), isOrderDesc);
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

}
