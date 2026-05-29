package com.deliveryapp.backend.common.filters;


import com.deliveryapp.backend.common.services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    @Qualifier("handlerExceptionResolver")
    private final HandlerExceptionResolver handlerExceptionResolver;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {

        //Obtenemos el header "Authorization" de la request
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {

            System.out.println("Error No token found!");

            //le decimos que siga con el siguiente filtro pero no va a poder porque el siguiente filtro es el de validar el nombre
            // de usuario y contraseña pero como no lo obtuvimos aca y no hay otro metodo de validacion al final fallara la autenticacion
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorizationHeader.substring(7); //substring a partir del numero 7 (por lo de "Bearer ")

        try {
            boolean isTokenExpired = jwtService.isTokenExpired(token);
            boolean canBeTokenRenewed = jwtService.canBeTokenRenewed(token);

            if (isTokenExpired && !canBeTokenRenewed) {
                System.out.println("Token expired!");
                filterChain.doFilter(request, response);
                return;
            }

            String username = jwtService.getUsername(token);

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            //esta comprobacion me parece redundante porque ya lanzaria exception antes en loadUserByUsername
            boolean isValidToken = jwtService.isValidToken(token, userDetails);


            //si el token no es valido o ya existe alguien autenticado entonces falla
            if (!isValidToken || SecurityContextHolder.getContext().getAuthentication() != null) {
                System.out.println("Invalid token or user already authenticated");
                filterChain.doFilter(request, response);
                return;
            }


            if (isTokenExpired && canBeTokenRenewed) {
                String renewedToken = jwtService.renewToken(token, userDetails);
                response.setHeader("Authorization", "Bearer " + renewedToken);
            }

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
            );

            //le agregamos los detalles de donde se hace la autenticacion para que tenga el contexto al respecto
            authentication.setDetails(
                    new WebAuthenticationDetailsSource()
                            .buildDetails(request)
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception e) {
            handlerExceptionResolver.resolveException(request, response, null, e);
            //  response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // Set appropriate status code
            //  response.getWriter().write("An error occurred while processing your request.");
            //sin el return este no funciona, pero no se que temas de seguridad traera
            return;
        }

        filterChain.doFilter(request, response);

    }
}
