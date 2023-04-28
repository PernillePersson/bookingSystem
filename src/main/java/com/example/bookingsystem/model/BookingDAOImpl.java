package com.example.bookingsystem.model;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BookingDAOImpl implements BookingDAO {
    private Connection con;

    public BookingDAOImpl() throws SQLException {
        con = ConnectionSingleton.getInstance().getConnection();
    }

    @Override
    public List<Booking> getAllBooking() {
        List<Booking> allBookings = new ArrayList<>();
        try {
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
    public void addBooking(String fn, String ln, String org, String mail, int phone, char bt, char catering,
                           LocalDate bd, Time st, Time et) throws SQLException {
        try {
            PreparedStatement ps = con.prepareStatement("INSERT INTO Booking VALUES(?,?,?,?,?,?,?," +
                    "?, GETDATE(), ?, ?, ?)");
            ps.setString(1, fn);
            ps.setString(2, ln);
            ps.setString(3, org);
            ps.setString(4, mail);
            ps.setInt(5, phone);
            ps.setString(6, String.valueOf(bt));
            ps.setString(7, String.valueOf(catering));
            ps.setDate(8, Date.valueOf(bd));
            ps.setString(9, BookingCode.generateBookingCode());
            ps.setTime(10, st);
            ps.setTime(11, et);

            ps.executeUpdate();
        }catch(SQLException e){
            System.err.println("Kunne ikke oprette booking");
        }
    }

    @Override
    public void cancelBooking(Booking b) throws SQLException {
        try {
            PreparedStatement ps = con.prepareStatement("DELETE FROM Booking WHERE bookingID = ?");
            ps.setInt(1, b.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Kunne ikke slette booking" + e.getMessage());
        }
    }
}