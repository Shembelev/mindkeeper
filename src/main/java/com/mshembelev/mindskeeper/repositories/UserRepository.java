package com.mshembelev.mindskeeper.repositories;


import com.mshembelev.mindskeeper.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<UserModel, Long> {

}
