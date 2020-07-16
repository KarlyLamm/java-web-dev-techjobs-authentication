package org.launchcode.javawebdevtechjobsauthentication;


import org.launchcode.javawebdevtechjobsauthentication.controllers.AuthenticationController;
import org.launchcode.javawebdevtechjobsauthentication.models.User;
import org.launchcode.javawebdevtechjobsauthentication.models.data.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class AuthenticationFilter extends HandlerInterceptorAdapter {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthenticationController authenticationController;
//whitelist
    private static final List<String> allowList = Arrays.asList("/login", "/register", "/css", "/logout");
//method checks a given path against values in whitelist
    private static boolean isAllowed(String path) {
        for (String pathRoot: allowList) {
            if (path.startsWith(pathRoot)) {
                return true;
            }
        }
        return false;
    }
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws IOException {
        //Don't require sign-in for whitelisted pages
        //checks path against the whitelist, returning true
        if (isAllowed(request.getRequestURI())) {
            //returning true indicates that the request may proceed
            return true;
        }
// retrieves the user's session object, contained in the request
        HttpSession session = request.getSession();
        //retrieves users object corresponding to the given user
        //null if user is not logged in
        User user = authenticationController.getUserFromSession(session);
//the user is logged in
        //user object is not null, allow request to be handled as normal
        if( user != null) {
            return true;
        }
// user object is null, redirects to login oage
        response.sendRedirect("/login");
        return false;
    }


}