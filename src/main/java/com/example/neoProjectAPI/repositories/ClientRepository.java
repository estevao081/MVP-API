package com.example.neoProjectAPI.repositories;

import com.example.neoProjectAPI.models.ClientModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClientRepository extends JpaRepository<ClientModel, UUID> {

    Page<ClientModel> findAll(Pageable pageable);

    List<ClientModel> findByNameContainingIgnoreCase(String name);

    @Query("SELECT c FROM ClientModel c WHERE c.age = :age")
    Optional<ClientModel> filterByAge(@Param("age") Integer age);

    @Query("SELECT c FROM ClientModel c WHERE c.mail = :mail")
    Optional<ClientModel> findByMail(@Param("mail") String mail);

}
