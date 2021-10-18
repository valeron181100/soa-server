package model;

import xml.LocalDateTimeAdapter;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.time.LocalDateTime;

@XmlRootElement
@Table(name = "vehicles")
@Entity
public class Vehicle implements Serializable {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически

    @Column(name = "name", nullable = false)
    private String name; //Поле не может быть null, Строка не может быть пустой

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "coord_id", nullable = false)
    private Coordinates coordinates; //Поле не может быть null

    private java.time.LocalDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически

    public Vehicle() {
        this.creationDate = LocalDateTime.now();
    }

    public Vehicle(int id, String name, Coordinates coordinates, LocalDateTime creationDate, long enginePower, long numberOfWheels, VehicleType type, FuelType fuelType) {
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = LocalDateTime.now();
        this.enginePower = enginePower;
        this.numberOfWheels = numberOfWheels;
        this.type = type;
        this.fuelType = fuelType;
    }

    @Column(name = "engine_power", nullable = false)
    private long enginePower; //Значение поля должно быть больше 0

    @Column(name = "number_of_wheels", nullable = false)
    private long numberOfWheels; //Значение поля должно быть больше 0

    @Column(name = "type")
    private VehicleType type; //Поле может быть null

    @Column(name = "fuel_type")
    private FuelType fuelType; //Поле может быть null

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    @XmlJavaTypeAdapter(value = LocalDateTimeAdapter.class)
    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public long getEnginePower() {
        return enginePower;
    }

    public void setEnginePower(long enginePower) {
        this.enginePower = enginePower;
    }

    public long getNumberOfWheels() {
        return numberOfWheels;
    }

    public void setNumberOfWheels(long numberOfWheels) {
        this.numberOfWheels = numberOfWheels;
    }

    public VehicleType getType() {
        return type;
    }

    public void setType(VehicleType type) {
        this.type = type;
    }

    public FuelType getFuelType() {
        return fuelType;
    }

    public void setFuelType(FuelType fuelType) {
        this.fuelType = fuelType;
    }
}
