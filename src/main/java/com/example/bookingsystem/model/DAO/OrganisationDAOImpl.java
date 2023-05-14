package com.example.bookingsystem.model.DAO;

import com.example.bookingsystem.model.ConnectionSingleton;
import com.example.bookingsystem.model.objects.Company;
import com.example.bookingsystem.model.objects.Organisation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrganisationDAOImpl implements OrganisationDAO{
    private Connection con;

    public OrganisationDAOImpl() throws SQLException {
        con = ConnectionSingleton.getInstance().getConnection();
    }

    @Override
    public void addOrg(String bk, int o) {
        try {
            int id = 0;
            PreparedStatement ps1 = con.prepareStatement("SELECT bookingID FROM Booking WHERE bookingCode = ?");
            ps1.setString(1, bk);
            ResultSet rs = ps1.executeQuery();

            while (rs.next()){
                id = rs.getInt(1);
            }

            PreparedStatement ps = con.prepareStatement("INSERT INTO BookingOrg VALUES(?,?)");
            ps.setInt(1, id);
            ps.setInt(2, o);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Kunne ikke tilføje organisation til booking " + e.getMessage());
        }
    }

    @Override
    public List<Organisation> getAllOrg() {
        List<Organisation> allOrg = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM Organisation;");
            ResultSet rs = ps.executeQuery();

            Organisation o;
            while (rs.next()){
                int id = rs.getInt(1);
                String forløb = rs.getString(2);


                o = new Organisation(id, forløb);
                allOrg.add(o);

            }
        } catch (SQLException e){
            System.err.println("Kan ikke finde forløb " + e.getMessage());
        }
        return allOrg;
    }

    @Override
    public Organisation getOrg(int id) {
        Organisation o = null;
        try {
            PreparedStatement ps = con.prepareStatement("SELECT Organisation.organisationID, organisation FROM Organisation \n" +
                    "JOIN BookingOrg ON BookingOrg.organisationID = Organisation.organisationID\n" +
                    "WHERE bookingID = ?;");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                int i = rs.getInt(1);
                String s = rs.getString(2);

                o = new Organisation(i, s);
            }

        }catch(SQLException e){
            System.out.println("Kunne ikke finde organisation " + e.getMessage());
        }
        return o;
    }

    @Override
    public Company getCompany(int id) {
        Company c = null;
        try {
            PreparedStatement ps = con.prepareStatement("SELECT companyID, company FROM Company WHERE bookingID = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                int i = rs.getInt(1);
                String s = rs.getString(2);

                c = new Company(i, s);
            }

        }catch(SQLException e){
            System.out.println("Kunne ikke finde note " + e.getMessage());
        }
        return c;
    }

    @Override
    public void addCompany(String bk, String c) {
        try {
            int id = 0;
            PreparedStatement ps1 = con.prepareStatement("SELECT bookingID FROM Booking WHERE bookingCode = ?");
            ps1.setString(1, bk);
            ResultSet rs = ps1.executeQuery();

            while (rs.next()){
                id = rs.getInt(1);
            }

            PreparedStatement ps = con.prepareStatement("INSERT INTO Company VALUES(?,?)");
            ps.setInt(1, id);
            ps.setString(2, c);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Kunne ikke tilføje virksomhed til booking " + e.getMessage());
        }
    }
}
