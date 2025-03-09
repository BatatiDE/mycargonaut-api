package com.mycargonaut.backend.config

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.stereotype.Component

@Component
class JwtAuthenticationFilter(
    private val jwtService: JwtService
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val path = request.servletPath

        // Exclude /graphql during development mode
        if (isGraphQLPathExcluded(path)) {
            filterChain.doFilter(request, response)
            return
        }

        val authHeader = request.getHeader("Authorization")
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response)
            return
        }

        val token = authHeader.substring(7) // Remove "Bearer "
        if (jwtService.validateToken(token)) {
            val username = jwtService.extractUsername(token)
            val auth = UsernamePasswordAuthenticationToken(username, null, emptyList()) // No roles for simplicity
            auth.details = WebAuthenticationDetailsSource().buildDetails(request)
            SecurityContextHolder.getContext().authentication = auth
        }

        filterChain.doFilter(request, response)
    }

    // Helper method to check if the request should bypass JWT validation
    private fun isGraphQLPathExcluded(path: String): Boolean {
        return path == "/graphql" && isDevelopmentMode()
    }

    private fun isDevelopmentMode(): Boolean {
        val activeProfile = System.getProperty("spring.profiles.active")
            ?: System.getenv("SPRING_PROFILES_ACTIVE")
            ?: "prod"
        return activeProfile == "dev"
    }
}
