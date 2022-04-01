package com.appsdeveloperblog.app.ws.mobile_app_ws.io.repositories;

import com.appsdeveloperblog.app.ws.mobile_app_ws.io.entity.PasswordResetTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetTokenEntity,Long> {

    PasswordResetTokenEntity findByToken(String token);
}
