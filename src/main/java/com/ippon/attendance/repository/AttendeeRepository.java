package com.ippon.attendance.repository;

import com.ippon.attendance.domain.Attendee;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Attendee entity.
 */
@SuppressWarnings("unused")
public interface AttendeeRepository extends JpaRepository<Attendee,Long> {

}
