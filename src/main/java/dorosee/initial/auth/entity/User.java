package dorosee.initial.auth.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Entity
@Data
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
@EntityListeners(AuditingEntityListener.class)
@Table(name = "TB_USER_INFO")
public class User implements Persistable<String> {

    @Id
    @NotNull
    @Column(name = "USER_ID")
    private String userId;
    @NotNull
    @Column(name = "USERNAME")
    private String username;
    @NotNull
    @Column(name = "PASSWORD")
    private String password;
    @NotNull
    @Column(name = "ROLES")
    private String roles;
    //하나의 유저가 두 개의 롤을 가지는 것에 대해 대비하기위한 변수

    @OneToOne
    @JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID")
    private UserDetail userDetail;

    @CreatedDate
    @Column(name = "CREATED_DATE", columnDefinition = "timestamp default CURRENT_TIMESTAMP not null")
    private LocalDateTime createdDate;

    @UpdateTimestamp
    @Column(name = "LAST_MODIFIED_DATE")
    private LocalDateTime lastModifiedDate;

    @Override
    public String getId() {
        return userId;
    }

    @Override
    public boolean isNew() {
        return createdDate == null;
    }

    public List<String> getRoleList() {
        if (this.roles.length() > 0) {
            return Arrays.asList(this.roles.split(","));
        }
        return new ArrayList<>();
    }

    @Builder
    public User(String userId, String username, String password, String roles,
                LocalDateTime createdDate, LocalDateTime lastModifiedDate) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.roles = roles;
        this.createdDate = createdDate;
        this.lastModifiedDate = lastModifiedDate;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
