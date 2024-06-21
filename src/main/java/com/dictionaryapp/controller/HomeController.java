package com.dictionaryapp.controller;

import com.dictionaryapp.config.UserSession;
import com.dictionaryapp.model.entity.LanguageEnum;
import com.dictionaryapp.model.entity.Word;
import com.dictionaryapp.service.WordService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController {

    private final UserSession userSession;
    private final WordService wordService;


    public HomeController(UserSession userSession, WordService wordService) {
        this.userSession = userSession;
        this.wordService = wordService;
    }

    @GetMapping("/")
    public String nonLoggedIndex(){
        if(userSession.isLoggedIn()){
            return "redirect:/home";
        }
        return "index";
    }

    @GetMapping("home")
    public String loggedInIndex(Model model){
        if(!userSession.isLoggedIn()){
            return "redirect:/";
        }

//        List<Word> spanishWords = wordService.findSpanish();
//        model.addAttribute("spanishWords", spanishWords);

        Map<LanguageEnum, List<Word>> allWords=wordService.findAllWords();

        List<Word> spanishWords = allWords.get(LanguageEnum.SPANISH);
        List<Word> frenchWords = allWords.get(LanguageEnum.FRENCH);
        List<Word> germanWords = allWords.get(LanguageEnum.GERMAN);
        List<Word> italianWords = allWords.get(LanguageEnum.ITALIAN);

        model.addAttribute("spanishWords", spanishWords);
        model.addAttribute("frenchWords", frenchWords);
        model.addAttribute("germanWords", germanWords);
        model.addAttribute("italianWords", italianWords);


        //count words
        Map<String, Integer> wordCounts = new HashMap<>();
        wordCounts.put("Spanish", spanishWords.size());
        wordCounts.put("French", frenchWords.size());
        wordCounts.put("German", germanWords.size());
        wordCounts.put("Italian", italianWords.size());

        // Calculate the total count
        int totalWordCount = wordCounts.values().stream().mapToInt(Integer::intValue).sum();

        // Add the word counts map and the total count to the model
        model.addAttribute("wordCounts", wordCounts);
        model.addAttribute("totalWordCount", totalWordCount);

        return "home";
    }

}
