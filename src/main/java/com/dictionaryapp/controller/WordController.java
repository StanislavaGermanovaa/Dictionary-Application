package com.dictionaryapp.controller;


import com.dictionaryapp.config.UserSession;
import com.dictionaryapp.model.dto.AddWordDTO;
import com.dictionaryapp.service.WordService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class WordController {

    private final WordService wordService;
    private final UserSession userSession;
    private final WordService wordsService;

    public WordController(WordService wordService, UserSession userSession, WordService wordsService) {
        this.wordService = wordService;
        this.userSession = userSession;
        this.wordsService = wordsService;
    }

    @ModelAttribute("addWordData")
    public AddWordDTO wordData(){
        return new AddWordDTO();
    }

    @GetMapping("/words")
    public String viewAddWord() {
        if(!userSession.isLoggedIn()){
            return "redirect:/";
        }
        return "word-add";
    }

    @PostMapping("/words")
    public String doAddWord(
            @Valid AddWordDTO data,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {

        if(!userSession.isLoggedIn()){
            return "redirect:/";
        }

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("addWordData", data);
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.addWordData", bindingResult);

            return "redirect:/words";
        }

        boolean success = wordService.create(data);

        if (!success){
            redirectAttributes.addFlashAttribute("addWordData",data);
            return "redirect:/words";
        }

        return "redirect:/home";
    }

    @DeleteMapping("/words/{id}")
    public String deleteWord(@PathVariable String id){

        if(!userSession.isLoggedIn()){
            return "redirect:/";
        }

        wordsService.delete(id);
        return "redirect:/home";

    }
}
