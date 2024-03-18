package dataeng.dao;

import dataeng.DataSource;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class CityDistrictDao {
    private Map<Integer, String> idCityMap = new HashMap<>();
    private Map<String, Integer> cityDistrictMap = new HashMap<>();

    public int getCityDistrictId(String shipToCityCd, String shipToDistrictName) {
        return cityDistrictMap.get(cityDistrictKey(shipToCityCd, shipToDistrictName));
    }

    public boolean containsCityDistrictId(int cityDistrictId) {
        return idCityMap.containsKey(cityDistrictId);
    }

    public boolean containsCityDistrict(String shipToCityCd, String shipToDistrictName) {
        return cityDistrictMap.containsKey(cityDistrictKey(shipToCityCd, shipToDistrictName));
    }

    public String getCityCd(int cityDistrictId) {
        return idCityMap.get(cityDistrictId);
    }

    public void insertCityDistrictMap(int cityDistrictId, String shipToCityCd, String shipToDistrictName) {
        int row = executeInsertCityDistrictMap(cityDistrictId, shipToCityCd, shipToDistrictName);
        idCityMap.put(cityDistrictId, shipToCityCd);
        cityDistrictMap.put(cityDistrictKey(shipToCityCd, shipToDistrictName), cityDistrictId);
        System.out.println("Inserted " + row + " dim_city_district_map [" + cityDistrictId + ", " + shipToCityCd + ", " + shipToDistrictName + "]");
    }

    public void insertCityDistrictMap(String shipToCityCd, String shipToDistrictName) {
        int row = executeInsertCityDistrictMap(0, shipToCityCd, shipToDistrictName);
        int cityDistrictId = executeSelectCityDistrictId(shipToCityCd, shipToDistrictName);
        idCityMap.put(cityDistrictId, shipToCityCd);
        cityDistrictMap.put(cityDistrictKey(shipToCityCd, shipToDistrictName), cityDistrictId);
        System.out.println("Inserted " + row + " dim_city_district_map [" + cityDistrictId + ", " + shipToCityCd + ", " + shipToDistrictName + "]");
    }

    private int executeSelectCityDistrictId(String shipToCityCd, String shipToDistrictName) {
        try (Connection conn = DataSource.getConnection();
             PreparedStatement selectCityDistrictId = conn.prepareStatement("SELECT city_district_id FROM dim_city_district_map WHERE ship_to_city_cd = ? AND ship_to_district_name = ?")) {

            selectCityDistrictId.setString(1, shipToCityCd);
            selectCityDistrictId.setString(2, shipToDistrictName);
            ResultSet rs = selectCityDistrictId.executeQuery();
            if (rs.next()) {
                return rs.getInt("city_district_id");
            }
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        }
        throw new IllegalStateException("This shouldn't happen");
    }

    private int executeInsertCityDistrictMap(int cityDistrictId, String shipToCityCd, String shipToDistrictName) {
        try (Connection conn = DataSource.getConnection();
             PreparedStatement insertDimCityDistrict = conn.prepareStatement("INSERT INTO dim_city_district_map (city_district_id, ship_to_city_cd, ship_to_district_name) VALUES (?, ?, ?)")) {

            insertDimCityDistrict.setInt(1, cityDistrictId);
            insertDimCityDistrict.setString(2, shipToCityCd);
            insertDimCityDistrict.setString(3, shipToDistrictName);
            int row = insertDimCityDistrict.executeUpdate();
            return row;
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        }
        throw new IllegalStateException("This shouldn't happen");
    }

    private String cityDistrictKey(String shipToCityCd, String shipToDistrictName) {
        return shipToCityCd + "," + shipToDistrictName;
    }
}
