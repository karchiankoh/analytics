package dataeng.dao;

import dataeng.DataSource;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class TimeDao {
    private Set<Integer> timeSet = new HashSet<>();

    public boolean containsOrderTime(int orderTime) {
        return timeSet.contains(orderTime);
    }

    public void insertTime(int orderTime) {
        int hour = orderTime / 10000;
        int minute = orderTime % 10000 / 100;
        int second = orderTime % 100;
        Time timePst = new Time(hour, minute, second);

        try (Connection conn = DataSource.getConnection();
             PreparedStatement insertDimTime = conn.prepareStatement("INSERT INTO dim_time (time_id, time_pst, hour, minute, second) VALUES (?, ?, ?, ?, ?)")) {
            insertDimTime.setInt(1, orderTime);
            insertDimTime.setTime(2, timePst);
            insertDimTime.setInt(3, hour);
            insertDimTime.setInt(4, minute);
            insertDimTime.setInt(5, second);
            int row = insertDimTime.executeUpdate();
            timeSet.add(orderTime);
            System.out.println("Inserted " + row + " dim_time [" + orderTime + ", " + timePst + ", " + hour + ", " + minute + ", " + second + "]");
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        }
    }
}
