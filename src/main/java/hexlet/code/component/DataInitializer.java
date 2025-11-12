package hexlet.code.component;

import hexlet.code.model.Label;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@AllArgsConstructor
public class DataInitializer implements ApplicationRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    /**
     * @param args
     */
    @Override
    public void run(ApplicationArguments args) {
        initApp();
    }

    private void initApp() {
        initAdmin();
        initTasksStatus();
        initLabels();
    }

    private void initAdmin() {
        if (userRepository.findByEmail("hexlet@example.com").isEmpty()) {
            User userData = new User();
            userData.setEmail("hexlet@example.com");
            String passDigist = passwordEncoder.encode("qwerty");
            userData.setPasswordDigest(passDigist);
            userData.setFirstName("admin");
            userData.setLastName("root");
            userRepository.save(userData);
        }
    }

    private void initTasksStatus() {
        Map<String, String> taskStatuses = new HashMap<>();
        taskStatuses.put("Draft", "draft");
        taskStatuses.put("ToReview", "to_review");
        taskStatuses.put("ToBeFixed", "to_be_fixed");
        taskStatuses.put("ToPublish", "to_publish");
        taskStatuses.put("Published", "published");
        for (Map.Entry<String, String> entry : taskStatuses.entrySet()) {
            TaskStatus taskStatus = new TaskStatus();
            taskStatus.setName(entry.getKey());
            taskStatus.setSlug(entry.getValue());
            taskStatusRepository.save(taskStatus);
        }
    }

    private void initLabels() {
        List<String> labels = List.of("feature", "bug");
        for (String st : labels) {
            Label label = new Label();
            label.setName(st);
            labelRepository.save(label);
        }
    }
}
