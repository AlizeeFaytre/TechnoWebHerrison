package com.grille.dao;

import com.grille.entities.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by jiawei on 10/06/2017.
 */
public interface AttendanceRepository extends JpaRepository<Attendance, Integer> {
}
