package dorosee.initial.auth.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.jetbrains.annotations.NotNull;


@Entity
@Data
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@Table(name ="TB_USER_REFRESH_TOKEN")
public class Token {

    @Id
    @NotNull
    @Column(name = "USER_ID")
    private String userId;

    @NotNull
    @Column(name = "USERNAME")
    private String username;
    @NotNull
    @Column(name = "REFRESH_TOKEN")
    private String refreshToken;

    @Builder
    public Token(String userId, String username, String refreshToken) {
        this.userId = userId;
        this.username = username;
        this.refreshToken = refreshToken;
    }
}
