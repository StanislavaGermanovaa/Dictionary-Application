package com.dictionaryapp.controller;


import com.dictionaryapp.model.dto.AddWordDTO;
import com.dictionaryapp.service.WordService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class WordController {

    private final WordService wordService;

    public WordController(WordService wordService) {
        this.wordService = wordService;
    }

    @ModelAttribute("addWordData")
    public AddWordDTO wordData(){
        return new AddWordDTO();
    }

    @GetMapping("/words")
    public String viewAddWord() {
        return "word-add";
    }

    @PostMapping("/words")
    public String doAddWord(
            @Valid AddWordDTO data,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {
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
}
