package nl.hu.s4.project.security.application;

public class JwtException extends RuntimeException {
    public JwtException(String message) {
        super(message);
    }
}
