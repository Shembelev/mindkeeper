package com.mshembelev.mindskeeper.services;


import com.mshembelev.mindskeeper.dto.auth.UpdateResponse;
import com.mshembelev.mindskeeper.models.UserModel;
import com.mshembelev.mindskeeper.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository repository;

    /**
     * Сохранение пользователя
     *
     * @return сохраненный пользователь
     */
    public UserModel save(UserModel user) {
        return repository.save(user);
    }


    /**
     * Создание пользователя
     *
     * @return созданный пользователь
     */
    public UserModel create(UserModel user) {
        if (repository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Пользователь с таким именем уже существует");
        }
        if (repository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Пользователь с таким email уже существует");
        }
        logger.info("Пользователь " + user.getUsername() + " успешно зарегистрирован");
        return save(user);
    }

    /**
     * Получение пользователя по имени пользователя
     *
     * @return пользователь
     */
    public UserModel getByUsername(String username) {
        return repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));

    }

    /**
     * Получение пользователя по имени пользователя
     * <p>
     * Нужен для Spring Security
     *
     * @return пользователь
     */
    public UserDetailsService userDetailsService() {
        return this::getByUsername;
    }

    /**
     * Получение текущего пользователя
     *
     * @return текущий пользователь
     */
    public UserModel getCurrentUser() {
        // Получение имени пользователя из контекста Spring Security
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        return getByUsername(username);
    }

    /**
     * Получение текущего пользователя по токену
     *
     * @return текущий пользователь
     */
    public UpdateResponse update() {
        UserModel user = getCurrentUser();
        UpdateResponse updateResponse = UpdateResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .enabled(user.isEnabled())
                .build();
        return updateResponse;
    }
}