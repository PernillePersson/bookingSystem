package com.example.bookingsystem.model;

import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.util.List;

public interface BookingDAO {
    public List<Booking> getAllBooking();
    public void addBooking(String fn, String ln, String org,
                           String mail, int phone, char bt,
                           char catering, LocalDate bd,
                           Time st, Time et);

    public void cancelBooking(Booking b) throws SQLException;

    public boolean bookingCodeExists(String bc);

    public void addNote(Booking b, String s);

    public List<Booking> recentlyCreated();

    public List<Booking> upcoming();

}
