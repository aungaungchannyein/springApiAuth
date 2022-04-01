package com.appsdeveloperblog.app.ws.mobile_app_ws.io.repositories;

import com.appsdeveloperblog.app.ws.mobile_app_ws.io.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity,Long> {

    RoleEntity findByName(String name);
}
