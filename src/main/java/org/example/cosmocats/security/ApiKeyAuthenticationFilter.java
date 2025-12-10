package org.example.cosmocats.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.cosmocats.config.SecurityProperties;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.access.prepost.PreAuthorize;


import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class ApiKeyAuthenticationFilter extends OncePerRequestFilter {

    private final SecurityProperties securityProperties;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String expectedHeader = securityProperties.getApiKeyHeader();
        String providedApiKey = request.getHeader(expectedHeader);

        
        if (providedApiKey == null) {
            writeError(response, 401, "API key header is missing");
            return;
        }

        
        if (!providedApiKey.equals(securityProperties.getApiKey())) {
            writeError(response, 401, "Invalid API key provided");
            return;
        }

        
        var authentication = new ApiKeyAuthenticationToken(
                providedApiKey,
                AuthorityUtils.createAuthorityList("ROLE_API")
        );

        authentication.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request)
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }

    private void writeError(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getOutputStream().write(
                ("{\"status\":" + status + ",\"message\":\"" + message + "\"}")
                        .getBytes(StandardCharsets.UTF_8)
        );
    }
}
