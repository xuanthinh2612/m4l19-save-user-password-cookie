package saveUserPassword.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import saveUserPassword.model.User;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@SessionAttributes("user")
@RequestMapping("/")
public class LoginController {

    @ModelAttribute("user")
    public User setUserForm() {
        return new User();
    }

    @GetMapping("/login")
    public ModelAndView showLoginForm(@CookieValue(value = "setUser", defaultValue = "") String setUser) {
        Cookie cookie = new Cookie("setUser",setUser);

        return new ModelAndView("login","cookieValue", cookie );

    }

    @PostMapping("/login")
    public String doLogin(@ModelAttribute("user") User user, Model model, @CookieValue(value = "setUser", defaultValue = "") String setUser,
                          HttpServletResponse response, HttpServletRequest request) {
        //implement business logic
        if (user.getEmail().equals("admin@gmail.com") && user.getPassword().equals("12345")) {
            if (user.getEmail() != null)
                setUser = user.getEmail();

            // create cookie and set it in response
            Cookie cookie = new Cookie("setUser", setUser);
            cookie.setMaxAge(24 * 60 * 60);
            response.addCookie(cookie);

            //get all cookies
            Cookie[] cookies = request.getCookies();
            //iterate each cookie
            for (Cookie ck : cookies) {
                //display only the cookie with the name 'setUser'
                if (ck.getName().equals("setUser")) {
                    model.addAttribute("cookieValue", ck);
                    break;
                } else {
//                    ck.setValue("");
//                    model.addAttribute("cookieValue", ck);
//                    break;
                }
            }
            model.addAttribute("message", "Login success. Welcome ");
        } else {
            user.setEmail("");
            Cookie cookie = new Cookie("setUser", setUser);
            model.addAttribute("cookieValue", cookie);
            model.addAttribute("message", "Login failed. Try again.");
        }
        return "login";
    }

}
