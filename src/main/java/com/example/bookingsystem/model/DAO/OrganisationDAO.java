package com.example.bookingsystem.model.DAO;

import com.example.bookingsystem.model.objects.Company;
import com.example.bookingsystem.model.objects.Organisation;

import java.util.List;

public interface OrganisationDAO {
    public void addOrg(String bk, int o);

    public List<Organisation> getAllOrg();

    public Organisation getOrg(int id);

    public Company getCompany(int id);

    public void addCompany(String bk, String c);
}
