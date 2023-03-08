package dorosee.initial.auth.dto;

import dorosee.initial.auth.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class UserDto {
    private String userId;
    private String username;
    private String roles;
    private String createdDate;

    public UserDto(User user){
        this.userId = user.getUserId();
        this.username = user.getUsername();
        this.roles = user.getRoles();
        this.createdDate = String.valueOf(user.getCreatedDate());
    }
}
