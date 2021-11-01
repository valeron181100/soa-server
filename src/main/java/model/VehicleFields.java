package model;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum VehicleFields {

    ID("id"),
    NAME("name"),
    COORDINATES("coordinates"),
    CREATION_DATE("creationDate"),
    ENGINE_POWER("enginePower"),
    NUMBER_OF_WHEELS("numberOfWheels"),
    VEHICLE_TYPE("type"),
    FUEL_TYPE("fuelType");

    private final String fieldName;

    VehicleFields(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public static VehicleFields fromFieldName(String fieldName) {
        return Arrays.stream(VehicleFields.values()).filter(p -> p.getFieldName().equals(fieldName)).collect(Collectors.toList()).get(0);
    }
}
