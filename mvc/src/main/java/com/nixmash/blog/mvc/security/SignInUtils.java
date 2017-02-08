package com.nixmash.blog.mvc.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.connect.ConnectionData;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;

import com.nixmash.blog.jpa.model.CurrentUser;
import com.nixmash.blog.jpa.model.User;
import com.nixmash.blog.mvc.controller.GlobalController;

public class SignInUtils {

	public static void authorizeUser(User user) {

		CurrentUser currentUser = new CurrentUser(user);
		SecurityContextHolder.getContext().setAuthentication(
				new UsernamePasswordAuthenticationToken(currentUser, user.getPassword(), user.getAuthorities()));

	}

	public static void setUserConnection(WebRequest request, ConnectionData connectionData) {
		String attribute = GlobalController.SESSION_USER_CONNECTION;
		request.setAttribute(attribute, connectionData, RequestAttributes.SCOPE_SESSION);
	}

}
