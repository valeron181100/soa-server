package model;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@Entity
public class Coordinates {
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    @Column(name = "x_coord")
    private int xCoord;
    @Column(name = "y_coord")
    private int yCoord;

    public Coordinates() { }

    public Coordinates(int xCoord, int yCoord) {
        this.xCoord = xCoord;
        this.yCoord = yCoord;
    }

    public int getId() {
        return id;
    }

    public int getxCoord() {
        return xCoord;
    }

    public void setxCoord(int xCoord) {
        this.xCoord = xCoord;
    }

    public int getyCoord() {
        return yCoord;
    }

    public void setyCoord(int yCoord) {
        this.yCoord = yCoord;
    }
}
