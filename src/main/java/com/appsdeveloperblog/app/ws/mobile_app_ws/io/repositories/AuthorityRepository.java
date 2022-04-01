package com.appsdeveloperblog.app.ws.mobile_app_ws.io.repositories;

import com.appsdeveloperblog.app.ws.mobile_app_ws.io.entity.AuthorityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityRepository extends JpaRepository<AuthorityEntity, Long> {

    AuthorityEntity findByName(String name);
}
