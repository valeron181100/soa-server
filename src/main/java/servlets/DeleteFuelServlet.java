package servlets;

import database.VehicleDaoImpl;
import model.FuelType;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/delete_by_fuel_type")
public class DeleteFuelServlet extends HttpServlet {
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String param = req.getParameter("q");
        if (param == null) {
            resp.sendError(400, "Query parameter was expected");
        } else {
            VehicleDaoImpl dao = new VehicleDaoImpl();
            FuelType fuelType = FuelType.valueOf(param);
            dao.deleteAll(fuelType);
        }
    }
}
