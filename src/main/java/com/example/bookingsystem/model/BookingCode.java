package com.example.bookingsystem.model;

public class BookingCode {
    public static String generateBookingCode()
    {

        // String med de characters vi vil bruge i koden
        String characterString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        StringBuilder sb = new StringBuilder(10);

        for (int i = 0; i < 10; i++) {

            // generate tilfældig nr fra 0 til characterStrings lændge
            int index = (int)(characterString.length() * Math.random());

            // tilføj random character til stringBuilder
            sb.append(characterString.charAt(index));
        }

        return sb.toString();
    }
}
