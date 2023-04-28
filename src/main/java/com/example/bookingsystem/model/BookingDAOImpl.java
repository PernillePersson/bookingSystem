package com.example.bookingsystem.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BookingDAOImpl implements BookingDAO {
    @Override
    public List<Booking> getAllBooking() {
        List<Booking> allBookings = new ArrayList<>();
        try {
            Connection con = ConnectionSingleton.getInstance().getConnection();

            PreparedStatement ps = con.prepareStatement("SELECT * FROM Booking;");
            ResultSet rs = ps.executeQuery();

            Booking b;
            while (rs.next()){
                int id = rs.getInt(1);
                String fName = rs.getString(2);
                String lName = rs.getString(3);
                String org = rs.getString(4);
                String mail = rs.getString(5);
                int phone = Integer.parseInt(rs.getString(6));
                char bType = rs.getString(7).charAt(0);
                char catering = rs.getString(8).charAt(0);
                LocalDate bDate = LocalDate.parse(rs.getString(9));
                LocalDate created = LocalDate.parse(rs.getString(10));
                String bCode = rs.getString(11);
                int startTime = rs.getInt(12);
                int endTime = rs.getInt(13);

                b = new Booking(id, fName, lName, org, mail, phone,
                        bType, catering, bDate, created, bCode, startTime, endTime);
                allBookings.add(b);

            }
        } catch (SQLException e){
            System.err.println("Kan ikke finde booking " + e.getMessage());
        }
        return allBookings;
    }

    @Override
    public void addBooking() throws SQLException {
        Connection con = ConnectionSingleton.getInstance().getConnection();
    }

    @Override
    public void cancelBooking() throws SQLException {
        Connection con = ConnectionSingleton.getInstance().getConnection();
    }
}
