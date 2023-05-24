package com.example.bookingsystem.model;

import com.example.bookingsystem.model.DAO.BookingDAO;
import com.example.bookingsystem.model.DAO.BookingDAOImpl;

import java.sql.SQLException;

public class BookingCode {
    public static String generateBookingCode()
    {

        // String med de characters vi vil bruge i koden
        String characterString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        StringBuilder sb = new StringBuilder(10);

        for (int i = 0; i < 10; i++) {

            // indsætter tilfældig karakter fra characterString
            int index = (int)(characterString.length() * Math.random());

            // tilføj random character til stringBuilder
            sb.append(characterString.charAt(index));
        }

        if (bdi.bookingCodeExists(sb.toString()))
            generateBookingCode(); //recursion, hvis koden allerede findes, genererer den bare igen
        else return sb.toString();

        return null;
    }

    static BookingDAO bdi;

    static {
        try {
            bdi = new BookingDAOImpl();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
