package be.vinci.chattycar.authentication;

import be.vinci.chattycar.authentication.models.Credentials;
import be.vinci.chattycar.authentication.models.InsecureCredentials;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
  private final AuthenticationRepository repository;
  private final Algorithm jwtAlgorithm;
  private final JWTVerifier jwtVerifier;

  public AuthenticationService(AuthenticationRepository repository, AuthenticationProperties properties) {
    this.repository = repository;
    this.jwtAlgorithm = Algorithm.HMAC512(properties.getSecret());
    this.jwtVerifier = JWT.require(this.jwtAlgorithm).withIssuer("auth0").build();
  }

  public String connect(InsecureCredentials insecureCredentials) {
    Credentials credentials = (Credentials)this.repository.findById(insecureCredentials.getEmail()).orElse((Credentials)null);
    if (credentials == null) {
      return null;
    } else {
      return !BCrypt.checkpw(insecureCredentials.getPassword(), credentials.getHashedPassword()) ? null : JWT.create().withIssuer("auth0").withClaim("email", credentials.getEmail()).sign(this.jwtAlgorithm);
    }
  }

  public String verify(String token) {
    try {
      String email = this.jwtVerifier.verify(token).getClaim("email").asString();
      return !this.repository.existsById(email) ? null : email;
    } catch (JWTVerificationException var3) {
      return null;
    }
  }

  public boolean createOne(InsecureCredentials insecureCredentials) {
    if (this.repository.existsById(insecureCredentials.getEmail())) {
      return false;
    } else {
      String hashedPassword = BCrypt.hashpw(insecureCredentials.getPassword(), BCrypt.gensalt());
      this.repository.save(insecureCredentials.toCredentials(hashedPassword));
      return true;
    }
  }

  public boolean updateOne(InsecureCredentials insecureCredentials) {
    if (!this.repository.existsById(insecureCredentials.getEmail())) {
      return false;
    } else {
      String hashedPassword = BCrypt.hashpw(insecureCredentials.getPassword(), BCrypt.gensalt());
      this.repository.save(insecureCredentials.toCredentials(hashedPassword));
      return true;
    }
  }

  public boolean deleteOne(String email) {
    if (!this.repository.existsById(email)) {
      return false;
    } else {
      this.repository.deleteById(email);
      return true;
    }
  }
}

