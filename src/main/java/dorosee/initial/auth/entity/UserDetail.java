package dorosee.initial.auth.entity;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@DynamicUpdate
@Table(name="TB_USER_DETAILS_INFO")
@EntityListeners(AuditingEntityListener.class)
public class UserDetail implements Persistable<String> {

    @Id
    @NotNull
    @Column(name = "USER_ID")
    private String userId;
    @NotNull
    @Column(name = "NAME")
    private String name;

    @Column(name = "MOBILE")
    private String mobile;
    @Column(name = "MEMO")
    private String memo;
    @CreatedDate
    @Column(name = "CREATED_DATE", columnDefinition = "timestamp default CURRENT_TIMESTAMP not null")
    private LocalDateTime createdDate;

    @Override
    public String getId() {
        return userId;
    }

    @Override
    public boolean isNew() {
        return createdDate == null;
    }

    @Builder
    public UserDetail(String userId, String name, String mobile, String memo, LocalDateTime createdDate) {
        this.userId = userId;
        this.name = name;
        this.mobile = mobile;
        this.memo = memo;
        this.createdDate = createdDate;
    }
}
