package DAJ2EE.Config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AdminAuthenticationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        // Currently relies on frontend-only authorization checks
        // Admin tab is only shown to ADMIN users on frontend
        // All admin API endpoints can be accessed if frontend sends requests
        // For production, implement token-based or session-based auth properly
        chain.doFilter(request, response);
    }
}
