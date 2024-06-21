package com.dictionaryapp.service;

import com.dictionaryapp.config.UserSession;
import com.dictionaryapp.model.dto.UserLoginDTO;
import com.dictionaryapp.model.dto.UserRegisterDTO;
import com.dictionaryapp.model.entity.User;
import com.dictionaryapp.model.entity.Word;
import com.dictionaryapp.repo.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    public UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    private final UserSession userSession;
    public UserService(UserRepository userRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder, UserSession userSession) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.userSession = userSession;
    }

    public boolean register(UserRegisterDTO data){
        if(!data.getPassword().equals(data.getConfirmPassword())){
            return false;
        }

        boolean isUsernameOrEmailTaken = userRepository.existsByUsernameOrEmail(data.getUsername(),data.getEmail());

        if(isUsernameOrEmailTaken){
            return false;
        }

        User mapped=modelMapper.map(data,User.class);
        mapped.setPassword(passwordEncoder.encode(mapped.getPassword()));

        userRepository.save(mapped);

        return true;
    }

    public boolean login(UserLoginDTO userLoginDTO){
        Optional<User> byUsername = userRepository.findByUsername(userLoginDTO.getUsername());

        if(byUsername.isEmpty()){
            return false;
        }

        User user=byUsername.get();

        if(!passwordEncoder.matches(userLoginDTO.getPassword(), user.getPassword())){
            return false;
        }

        userSession.login(user);

        return true;
    }

}
