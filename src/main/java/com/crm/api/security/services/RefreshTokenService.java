package com.crm.api.security.services;

import com.crm.api.exception.TokenRefreshException;
import com.crm.api.model.RefreshToken;
import com.crm.api.repository.RefreshTokenRepository;
import com.crm.api.repository.AuthRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {
  @Value("${crm.app.jwtRefreshExpirationMs}")
  private Long refreshTokenDurationMs;

  RefreshTokenRepository refreshTokenRepository;
  AuthRepository authRepository;

  public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, AuthRepository authRepository) {
    this.refreshTokenRepository = refreshTokenRepository;
    this.authRepository = authRepository;
  }

  public Optional<RefreshToken> findByToken(String token) {
    return refreshTokenRepository.findByToken(token);
  }

  public RefreshToken createRefreshToken(UUID userId) {
    RefreshToken refreshToken = new RefreshToken();

    refreshToken.setUser(authRepository.findById(userId).get());
    refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
    refreshToken.setToken(UUID.randomUUID().toString());

    refreshToken = refreshTokenRepository.save(refreshToken);
    return refreshToken;
  }

  public RefreshToken verifyExpiration(RefreshToken token) {
    if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
      refreshTokenRepository.delete(token);
      throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new signin request");
    }

    return token;
  }

  @Transactional
  public int deleteByUserId(UUID userId) {
    return refreshTokenRepository.deleteByUser(authRepository.findById(userId).get());
  }
}