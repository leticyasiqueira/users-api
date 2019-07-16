package com.lsiqueira.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lsiqueira.user.entity.Authorities;

public interface AuthoritiesRepository extends JpaRepository<Authorities, Long>{

}
