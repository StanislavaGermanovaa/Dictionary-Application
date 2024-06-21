package com.dictionaryapp.service;

import com.dictionaryapp.config.UserSession;
import com.dictionaryapp.model.dto.AddWordDTO;
import com.dictionaryapp.model.entity.Language;
import com.dictionaryapp.model.entity.User;
import com.dictionaryapp.model.entity.Word;
import com.dictionaryapp.repo.LanguageRepository;
import com.dictionaryapp.repo.UserRepository;
import com.dictionaryapp.repo.WordRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
}
