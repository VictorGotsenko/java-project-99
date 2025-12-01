package hexlet.code.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "users")
@Schema(description = "User data model")
public class User implements BaseEntity, UserDetails {

    @Id
    @Schema(description = "User ID",
            example = "123")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "id", nullable = false)
    private Long id;

    @Schema(description = "User first name",
            example = "Tom",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    @EqualsAndHashCode.Include
    private String firstName;
    @NotBlank
    private String lastName;


    @Email
    @Column(unique = true)
    private String email;

    @Schema(description = "Password hash")
    @NotBlank
    private String passwordDigest;

    @Schema(description = "User creation date")
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDate createdAt;

    @LastModifiedDate
    private LocalDate updatedAt;

    /**
     * @return String
     */
    @Override
    public String getPassword() {
        return passwordDigest;
    }

    /**
     * @return String
     */
    @Override
    public String getUsername() {
        return email;
    }

    /**
     * @return GrantedAuthority
     */
    @Override
    @SuppressWarnings("java:S2293")
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<GrantedAuthority>();
    }
}
