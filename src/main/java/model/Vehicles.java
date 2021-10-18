package model;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement
public class Vehicles {

    private List<Vehicle> vehicle;

    public List<Vehicle> getVehicle() {
        return vehicle;
    }

    public void setVehicle(List<Vehicle> vehicles) {
        this.vehicle = vehicles;
    }
}


