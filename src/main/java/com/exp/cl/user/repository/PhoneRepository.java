package com.exp.cl.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.exp.cl.user.models.entity.Phone;

public interface PhoneRepository extends JpaRepository<Phone, Long> {

}