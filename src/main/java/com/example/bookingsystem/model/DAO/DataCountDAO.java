package com.example.bookingsystem.model.DAO;

import com.example.bookingsystem.model.DataCount;

import java.time.LocalDate;
import java.util.List;

public interface DataCountDAO {


    public List<DataCount> importData(String s1, LocalDate d1, LocalDate d2);

////

    public List<DataCount> importOrgData(String s1);

    public List<DataCount> importOrgStatData(String s1, LocalDate d1, LocalDate d2);
}