package dataeng.dao;

import dataeng.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class OrderDao {
    public void insertFactOrder(String orderId, int orderTime, int cityDistrictId, String currencyCd, double rptgAmt, int orderQty) {
        try (Connection conn = DataSource.getConnection();
             PreparedStatement insertFactOrder = conn.prepareStatement("INSERT INTO fact_orders (order_id, order_time_id, city_district_id, currency_cd, rptg_amt, order_qty) VALUES (?, ?, ?, ?, ?, ?)")) {

            insertFactOrder.setString(1, orderId);
            insertFactOrder.setInt(2, orderTime);
            insertFactOrder.setInt(3, cityDistrictId);
            insertFactOrder.setString(4, currencyCd);
            insertFactOrder.setDouble(5, rptgAmt);
            insertFactOrder.setInt(6, orderQty);
            int row = insertFactOrder.executeUpdate();
//        System.out.println("Inserted " + row + " fact_orders [" + orderId + ", " + orderTime + ", " + cityDistrictId + ", " + currencyCd + ", " + rptgAmt + ", " + orderQty + "]");
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        }

    }
}
