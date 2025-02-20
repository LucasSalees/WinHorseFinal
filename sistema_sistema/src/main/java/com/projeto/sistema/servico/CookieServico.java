package com.projeto.sistema.servico;

import java.util.Arrays;
import java.util.Optional;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CookieServico {
	public static void setCookie(HttpServletResponse response, String key, String valor, int segundos) {
		Cookie cookie = new Cookie (key, valor);
		cookie.setMaxAge(segundos);
        response.addCookie(cookie);
	}
	
	public static String getCookie(HttpServletRequest request, String key) {
		return Optional.ofNullable(request.getCookies())
		   .flatMap(cookies -> Arrays.stream(cookies)
			.filter(cookie -> key.equals(cookie.getName()))
			.findAny()
			).map(e -> e.getValue())
			.orElse(null);
	}
	
	public static void removeCookie(HttpServletResponse response, String key) {
	    Cookie cookie = new Cookie(key, null);
	    cookie.setMaxAge(0); // Remove o cookie
	    cookie.setPath("/"); // Garante que remove em todas as rotas
	    response.addCookie(cookie);
	    
	}
}