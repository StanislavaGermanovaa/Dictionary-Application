package com.dictionaryapp.service;

import com.dictionaryapp.config.UserSession;
import com.dictionaryapp.model.dto.AddWordDTO;
import com.dictionaryapp.model.entity.Language;
import com.dictionaryapp.model.entity.LanguageEnum;
import com.dictionaryapp.model.entity.User;
import com.dictionaryapp.model.entity.Word;
import com.dictionaryapp.repo.LanguageRepository;
import com.dictionaryapp.repo.UserRepository;
import com.dictionaryapp.repo.WordRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class WordService {
    private final UserRepository userRepository;
    private final UserSession userSession;

    private final LanguageRepository languageRepository;

    private final WordRepository wordRepository;

    public WordService(UserRepository userRepository, UserSession userSession, LanguageRepository languageRepository, WordRepository wordRepository) {
        this.userRepository = userRepository;
        this.userSession = userSession;
        this.languageRepository = languageRepository;
        this.wordRepository = wordRepository;
    }

    public boolean create(AddWordDTO data) {
        if (!userSession.isLoggedIn()) {
            return false;
        }

        Optional<User> byId = userRepository.findById(userSession.getId());

        if (byId.isEmpty()){
            return false;
        }

        Language language = languageRepository.findByLanguageName(data.getLanguage());

        if (language==null){
            return false;
        }

        Word word = new Word();
        word.setTerm(data.getTerm());
        word.setTranslation(data.getTranslation());
        word.setExample(data.getExample());
        word.setDate(data.getDate());
        word.setLanguage(language);
        word.setAddedBy(byId.get());

        wordRepository.save(word);

        return true;

    }

//    public List<Word> findSpanish() {
//        Optional<User> user = userRepository.findById(userSession.getId());
//
//        if (user.isEmpty()) {
//            return new ArrayList<>();
//        }
//
//
//        Language language = languageRepository.findByLanguageName(LanguageEnum.SPANISH);
//
//        return wordRepository.findByLanguageAndAddedBy(language, user.get());
//    }

    public Map<LanguageEnum, List<Word>> findAllWords(){
        Map<LanguageEnum, List<Word>> result=new HashMap<>();

        List<Language> allLanguages=languageRepository.findAll();

        for(Language language: allLanguages){

            List<Word> words=wordRepository.findAllByLanguage(language);

            result.put(language.getLanguageName(),words);
        }
        return result;
    }

    public void delete(String id) {
//        userRepository.findById(userSession.userId())
//                .flatMap(user -> wordRepository.findByIdAndAddedBy(id, user))
//                .ifPresent(wordRepository::delete);

        Optional<User> user = userRepository.findById(userSession.getId());

        if (user.isEmpty()) {
            return;
        }

        Optional<Word> maybeWord = wordRepository.findByIdAndAddedBy(id, user.get());

        if (maybeWord.isEmpty()) {
            return;
        }

        wordRepository.delete(maybeWord.get());
    }
}

