// this controller will deal w/ User objects
package org.launchcode.javawebdevtechjobsauthentication.controllers;

import org.launchcode.javawebdevtechjobsauthentication.models.data.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class AuthenticationController {

    @Autowired
    private UserRepository userRepository;

    // this code allows us to store and retrieve the login status of a user in a session
    //users ID will be stored in their session
    private static final String userSessionKey = "user";
    //userSessionKey used to store user IDs

    //looks for data w/ the key user in the user's session
    public User getUserFromSession(HttpSession session) {
        Integer userId = (Integer) session.getAttribute(userSessionKey);
        if (userId == null) {
            return null;
        }

        Optional<User> user = userRepository.findById(userId);

        if (user.isEmpty()) {
            return null;
        }

        return user.get();
    }

    private static void setUserInSession(HttpSession session, User user) {
        session.setAttribute(userSessionKey, user.getId());
    }
    //registration form
    @GetMapping("/register")
    public String displayRegistrationForm(Model model) {
        model.addAttribute(new RegisterFormDTO());
        model.addAttribute("title", "Register");
        return "register";
    }
    //defines handler method at the route /register
    //takes a valid registerFormDTO object, associated errors, and a model
    @PostMapping("/register")
    public String processRegistrationForm(@ModelAttribute @Valid RegisterFormDTO registerFormDTO,
                                          Errors errors, HttpServletRequest request,
                                          Model model) {
    //return the user to the form if a validation error
        if (errors.hasErrors()) {
            model.addAttribute("title", "Register");
            return "register";
        }
    //retrieves the user with the given username from the database
        User existingUser = userRepository.findByUsername(registerFormDTO.getUsername());
    //if a user w the given user already exists, register a custom error w the errors object and return user to form
        if (existingUser != null) {
            errors.rejectValue("username", "username.alreadyexists", "A user with that username already exists");
            model.addAttribute("title", "Register");
            return "register";
        }

        String password = registerFormDTO.getPassword();
        String verifyPassword = registerFormDTO.getVerifyPassword();
       //compares passwords submitted
        if (!password.equals(verifyPassword)) {
            errors.rejectValue("password", "passwords.mismatch", "Passwords do not match");
            model.addAttribute("title", "Register");
            return "register";
        }
    //creates new user object, store it in database, then create a new session for the user
        User newUser = new User(registerFormDTO.getUsername(), registerFormDTO.getPassword());
        userRepository.save(newUser);
        setUserInSession(request.getSession(), newUser);
    //takes user to homepage.... finally!
        return "redirect:";
    }
    User newUser = new User(registerFormDTO.getUsername(), registerFormDTO.getPassword());
        userRepository.save(newUser);
    setUserInSession(request.getSession(), newUser);

        return "redirect:";
}
// same steps repeated for login

    @GetMapping("/login")
    public String displayLoginForm(Model model) {
        model.addAttribute(new LoginFormDTO());
        model.addAttribute("title", "Log In");
        return "login";
    }

    @PostMapping("/login")
    public String processRegistrationFom(@ModelAttribute @Valid LoginFormDTO loginFormDTO,
                                         Errors errors, HttpServletRequest request, Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("title", "Log In");
            return "login";
        }
// retrieves the user object with the given password from the database
        User theUser = (User) userRepository.findByUsername(loginFormDTO.getUsername());
// custom error if user doesnt exist.. returns to form
        if (theUser ==null) {
            errors.rejectValue("username", "username.invalid", "The given username does not exists" );
            model.addAttribute("title", "Log In");
            return "login";
        }
//retrieves the submitted password from the form DTO
        String password = loginFormDTO.getPassword();
//error if password is incorrect
        if (!theUser.isMatchingPassword(password)) {
            //User.isMatchingPassword() method handles the details associated w checking hashed passwords
            errors.rejectValue("password", "password.invalid", "Invalid password");
            model.addAttribute("title", "Log In");
            return "login";

        }
// creates new session for user
        setUserInSession(request.getSession(), theUser);
        //redirects user to the homepage
        return "redirect:";
    }


    //logout
    //invalidates the session data from the request object
    //redirects the user to the login form
    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return"redirect:/login";
    }


}

