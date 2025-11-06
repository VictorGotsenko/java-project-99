package hexlet.code.model;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@Getter
@Setter

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 1)
    private String name; //Обязательное. Минимум 1 символ. Названия задач могут быть любыми

    private Integer index; //Необязательное, целое число. Нужно для прав-го отображ. задач во фронте

    private String description; //Необязательное. Описание задачи, может быть любым

    @ManyToOne(fetch = FetchType.EAGER)
    private TaskStatus taskStatus; //Обязательное. Связано с сущностью статуса

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private User assignee; //Необязательное. Исполнитель задачи, связан с сущностью пользователя

    @CreatedDate
    private LocalDate createdAt;
}
