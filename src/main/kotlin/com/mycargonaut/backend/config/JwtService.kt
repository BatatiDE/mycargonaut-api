package com.mycargonaut.backend.config

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Service
// import java.util.Base64
import java.util.Date

@Service
class JwtService {
    // Generiere einen zufälligen Schlüssel, der mit dem JWT-Signaturalgorithmus verwendet wird
    private val SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256) // Wenn du einen sicheren Schlüssel verwenden möchtest, behalte dies
    // Alternativ, wenn du Base64 verwenden möchtest, kannst du es so machen
    // private val SECRET_KEY = Base64.getEncoder().encodeToString("my-super-secret-key".toByteArray())

    fun generateToken(username: String): String {
        return Jwts.builder()
            .setSubject(username)
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 Stunden
            .signWith(SECRET_KEY) // Das ist die Verwendung des Schlüssels beim Signieren
            .compact()
    }

    fun validateToken(token: String): Boolean {
        return try {
            Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY) // Schlüssel beim Parsen verwenden
                .build()
                .parseClaimsJws(token)
            true
        } catch (e: Exception) {
            println("JWT Validation Error: ${e.message}") // Fehler beim Validieren
            false
        }
    }

    fun extractUsername(token: String): String {
        return Jwts.parserBuilder()
            .setSigningKey(SECRET_KEY)
            .build()
            .parseClaimsJws(token).body.subject
    }
}
