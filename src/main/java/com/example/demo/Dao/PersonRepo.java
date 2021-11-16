package com.example.demo.Dao;

import com.example.demo.Dao.Person;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

public interface PersonRepo extends CrudRepository<Person, Long> {
    /*@Query("SELECT id FROM Person p WHERE p.email = ?1")
    int findByEmail(String email);*/

    @Query("SELECT p FROM Person p WHERE p.email = ?1")
    Person findByEmail(String email);

    @Modifying
    @Transactional
    @Query("update Person p set p.permissionTypeMask = ?1 where p.id = ?2")
    void setPersonPermsById(int permissionTypeMask, int id);
}

