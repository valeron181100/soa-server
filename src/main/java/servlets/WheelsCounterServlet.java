package servlets;

import database.VehicleDaoImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/wheels_avg_count")
public class WheelsCounterServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        VehicleDaoImpl dao = new VehicleDaoImpl();
        resp.getWriter().print("<avg>");
        resp.getWriter().print(String.format("%.3f", dao.countAvgNumberOfWheels()));
        resp.getWriter().print("</avg>");
    }
}
