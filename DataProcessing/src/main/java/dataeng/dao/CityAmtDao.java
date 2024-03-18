package dataeng.dao;

import dataeng.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

public class CityAmtDao {
    private Map<String, Double> cityAmtMap;

    public void setAndInsertCityAmtMap(Map<String, Double> cityAmtMap) {
        this.cityAmtMap = cityAmtMap;
        try (Connection conn = DataSource.getConnection();
             PreparedStatement insertCityAmt = conn.prepareStatement("INSERT INTO fact_city_amt (city_cd, rptg_amt) VALUES (?, ?)")) {

            for (Map.Entry<String, Double> cityAmt : cityAmtMap.entrySet()) {
                insertCityAmt.setString(1, cityAmt.getKey());
                insertCityAmt.setDouble(2, cityAmt.getValue());
                insertCityAmt.addBatch();
            }
            int row = insertCityAmt.executeBatch().length;
            System.out.println("Inserted " + row + " fact_city_amt");
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        }
    }
}
