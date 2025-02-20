package com.projeto.sistema.servico.autenticacao;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.projeto.sistema.servico.CookieServico;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class LoginInterceptor implements HandlerInterceptor {

	@Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {
        // Verifica se o cookie de login existe
        String usuarioIdCookie = CookieServico.getCookie(request, "id_usuario");

        if (usuarioIdCookie != null) {
            // Cookie existe, o usuário está autenticado
            return true;
        } else {
            // Se o cookie não existir ou tiver expirado, redireciona para o login
            response.sendRedirect("/login");
            return false;
        }
    }

}