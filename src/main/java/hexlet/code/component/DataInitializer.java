package hexlet.code.component;

import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DataInitializer implements ApplicationRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     *
     * @param args
     */
    @Override
    public void run(ApplicationArguments args) {
        createAdmin();
    }

    private void createAdmin() {
        User userData = new User();
        userData.setEmail("hexlet@example.com");
        String passDigist = passwordEncoder.encode("qwerty");
        userData.setPasswordDigest(passDigist);
        userData.setFirstName("admin");
        userData.setLastName("root");
        userRepository.save(userData);
    }
}
