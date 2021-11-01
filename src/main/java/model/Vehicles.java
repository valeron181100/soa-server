package model;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement
public class Vehicles {

    private List<Vehicle> vehicle;

    private int totalCount;

    public List<Vehicle> getVehicle() {
        return vehicle;
    }

    public void setVehicle(List<Vehicle> vehicles) {
        this.vehicle = vehicles;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }
}


