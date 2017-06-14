package com.grille.dao;

import com.grille.entities.Attendance;
import com.grille.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by jiawei on 10/06/2017.
 */
public interface AttendanceRepository extends JpaRepository<Attendance, Integer> {
    public Attendance findById(int id);
    public ArrayList<Attendance> findByDate(Date date);
    public Attendance findByDateAndUser(Date date, User user);
}
