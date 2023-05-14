package com.example.bookingsystem.model.DAO;

import com.example.bookingsystem.model.ConnectionSingleton;
import com.example.bookingsystem.model.DAO.DataCountDAO;
import com.example.bookingsystem.model.DataCount;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DataCountDAOImpl implements DataCountDAO {
    private Connection con;

    public DataCountDAOImpl() throws SQLException {
        con = ConnectionSingleton.getInstance().getConnection();
    }

////
    @Override
    public List<DataCount> importData(String s1) {

        List<DataCount> data = new ArrayList<>();
        try{
            PreparedStatement ps = con.prepareStatement("SELECT DISTINCT BookingForløb.boFoID,BookingForløb.bookingID,BookingForløb.forløbID FROM BookingForløb,Forløb,Booking WHERE Forløb.forløbID = BookingForløb.forløbID AND Forløb.forløb = ?;");

            ps.setString(1,s1);
            ResultSet rs = ps.executeQuery();

            DataCount dc;

            while (rs.next()){
                int id1 = rs.getInt(1);
                int id2 = rs.getInt(2);
                int id3 = rs.getInt(3);

                dc = new DataCount(id1, id2, id3);
                data.add(dc);
            }
        } catch (SQLException e) { System.out.println(e); }

        return data;
    }
}