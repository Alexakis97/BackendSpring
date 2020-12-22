package com.aed.demo.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;
    
    // @Autowired
	// private UserRepository userRepo;
    
	// @Autowired
	// private CacheMemory myCache;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException
           {

        final String authorizationHeader = request.getHeader("LTT");

        String username = null;
        String jwt = null;
        
//        System.out.println("Printing Request");
//        
//        Enumeration<String> headerNames = request.getHeaderNames();
//        while(headerNames.hasMoreElements()) {
//          String headerName = headerNames.nextElement();
//          System.out.println("Header Name - " + headerName + ", Value - " + request.getHeader(headerName));
//        }
//        
//        Enumeration<String> params = request.getParameterNames(); 
//        while(params.hasMoreElements()){
//         String paramName = params.nextElement();
//         System.out.println("Parameter Name - "+paramName+", Value - "+request.getParameter(paramName));
//        }
        
        
       

      
        
        if (authorizationHeader != null && authorizationHeader.startsWith("LifeTimeToken ")) {
            jwt = authorizationHeader.substring(14);
            try {
            username = jwtUtil.extractUsername(jwt);
            } catch (ExpiredJwtException e) {
                System.out.println(" Token expired ");
            } catch (SignatureException e) {
              //  Logger.getLogger(JWTController.class.getName()).log(Level.ERROR, e);
            } catch(Exception e){
                System.out.println(" Some other exception in JWT parsing ");
            }
        }

      
        if (username !=  null && SecurityContextHolder.getContext().getAuthentication() == null) {
        	
        	
        	
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            if (jwtUtil.validateToken(jwt, userDetails)) {

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
               
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                
                
                
                Authentication auth=  SecurityContextHolder.getContext().getAuthentication();
        		System.out.println("Principal: "+auth.getPrincipal());
        		System.out.println("Authorities: "+auth.getAuthorities()); //get it from here ORsy
            }
        }
        
        
			chain.doFilter(request, response);
       
    }

}
