package hexlet.code.service;

import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsManager {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public CustomUserDetailsService(UserRepository userRepository,
                                    PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * @param email
     * @return UserDetails
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Нужно добавить в репозиторий findByEmail
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User with email " + email + " not found"));
    }

    /**
     * void createUser.
     *
     * @param userData
     */
    @Override
    public void createUser(UserDetails userData) {
        var user = new User();
        user.setEmail(userData.getUsername());
        var hashedPassword = passwordEncoder.encode(userData.getPassword());
        user.setPasswordDigest(hashedPassword);
        userRepository.save(user);
    }

    /**
     * void updateUser.
     *
     * @param user
     */
    @Override
    public void updateUser(UserDetails user) {
        throw new UnsupportedOperationException("Unimplemented method 'updateUser'");
    }

    /**
     * void deleteUser.
     *
     * @param username
     */
    @Override
    public void deleteUser(String username) {
        throw new UnsupportedOperationException("Unimplemented method 'deleteUser'");
    }

    /**
     * void changePassword.
     *
     * @param oldPassword
     * @param newPassword
     */
    @Override
    public void changePassword(String oldPassword, String newPassword) {
        throw new UnsupportedOperationException("Unimplemented method 'changePassword'");
    }

    /**
     * boolean userExists.
     *
     * @param username
     * @return boolean
     */
    @Override
    public boolean userExists(String username) {
        throw new UnsupportedOperationException("Unimplemented method 'userExists'");
    }
}
