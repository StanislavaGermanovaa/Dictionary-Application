package com.dictionaryapp.controller;

import com.dictionaryapp.config.UserSession;
import com.dictionaryapp.model.dto.UserLoginDTO;
import com.dictionaryapp.model.dto.UserRegisterDTO;
import com.dictionaryapp.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserController {
    private final UserService userService;
    private final UserSession userSession;

    public UserController(UserService userService, UserSession userSession) {
        this.userService = userService;
        this.userSession = userSession;
    }

    @ModelAttribute("registerData")
    public UserRegisterDTO createEmptyDTo(){
        return new UserRegisterDTO();
    }

    @ModelAttribute("loginData")
    public UserLoginDTO loginData(){
        return new UserLoginDTO();
    }


    @GetMapping("/register")
    public String viewRegister(){
        if(userSession.isLoggedIn()){
            return "redirect:/home";
        }
        return "register";
    }

    @PostMapping("/register")
    public String doRegister(@Valid UserRegisterDTO data,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes){

        if(userSession.isLoggedIn()){
            return "redirect:/";
        }

        if(bindingResult.hasErrors() || !userService.register(data)){
            redirectAttributes.addFlashAttribute("registerData",data);
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.registerData",bindingResult);

            return "redirect:/register";
        }


        return "redirect:/login";
    }

    @GetMapping("/login")
    public String viewLogin(){
        if(userSession.isLoggedIn()){
            return "redirect:/home";
        }
        return "login";
    }

    @PostMapping("/login")
    public String doLogin( @Valid UserLoginDTO userLoginDTO,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes){

        if (userSession.isLoggedIn()) {
            return "redirect:/home";
        }

        if(bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("loginData", userLoginDTO);
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.loginData", bindingResult);

            return "redirect:/login";

        }

        boolean success = userService.login(userLoginDTO);

        if(!success){
            redirectAttributes.addFlashAttribute("loginData",userLoginDTO);
            redirectAttributes.addFlashAttribute("userPassMismatch",true);

            return "redirect:login";
        }
        return "redirect:/home";
    }

    @GetMapping("/logout")
    public String logout() {
        if (!userSession.isLoggedIn()) {
            return "redirect:/";
        }

        userSession.logout();

        return "redirect:/";
    }
}
