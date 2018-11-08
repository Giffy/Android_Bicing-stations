package com.mideann.giffy.estacionsbicis;

import android.location.Location;

import java.util.ArrayList;

public class Estacio {

    private int id;                         // ID de la estació
    private String type;                    // Tipus de vehicle Bici o electrica
    private Double latitude, longitude;     // Coordenades GPS
    private Location location;
    private String streetName, streetNumber;// Adreça: Carrer i numero
    private int slots, bikes;               // Slots lliures i bicis disponibles
    private String[] nearbyStations; // Estacions properes
    private String status;                  // Estat de funcionament de la estacio

    public Estacio(int id, String type, Double latitude, Double longitude, String streetName, String streetNumber, int slots, int bikes, String[] nearbyStations, String status) {
        this.id = id;
        this.type = type;
        this.location = new Location("service Provider");
        this.location.setLatitude(latitude);
        this.location.setLongitude(longitude);

        this.latitude = latitude;
        this.longitude = longitude;
        this.streetName = streetName;
        this.streetNumber = streetNumber;
        this.slots = slots;
        this.bikes = bikes;
        this.nearbyStations = nearbyStations;
        this.status = status;
    }


    // Setters i getters

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public String getAddress() {
        return streetName + " " + streetNumber;
    }

    public int getSlots() {
        return slots;
    }

    public void setSlots(int slots) {
        this.slots = slots;
    }

    public int getBikes() {
        return bikes;
    }

    public void setBikes(int bikes) {
        this.bikes = bikes;
    }

    public String[] getNearbyStations() {
        return nearbyStations;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Location getLocation() {
        Location location = new Location("service Provider");
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        return location;
    }

    public float getDistanceTo(Location location) {
        float distanceInMeters = this.location.distanceTo(location);
        return distanceInMeters;
    }

}