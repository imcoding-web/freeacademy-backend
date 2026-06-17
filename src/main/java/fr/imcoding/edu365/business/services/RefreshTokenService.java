package fr.imcoding.edu365.business.services;

import fr.imcoding.edu365.client.dtos.response.RefreshJwResponse;
import fr.imcoding.edu365.exceptions.UserForbiddenException;
import fr.imcoding.edu365.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @author Rokaya
 * @Date 26/05/2022
 */

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

  private final JwtUtils jwtUtils;

  public RefreshJwResponse refreshUserToken(String refreshToken) {
    if (!StringUtils.hasText(refreshToken) || jwtUtils.isTokenExpired(refreshToken)) {
      throw new UserForbiddenException("Token is not valide");
    }
    return new RefreshJwResponse(jwtUtils.generateJwtTokenFromExpiredToken(refreshToken));
  }

}
