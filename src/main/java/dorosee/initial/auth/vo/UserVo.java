package dorosee.initial.auth.vo;

import lombok.Data;

import java.time.LocalDateTime;


@Data
public class UserVo {

    private String userId;
    private String username;
    private String password;
    private String roles; // USER,ADMIN

    private UserDetailVo userDetail;

    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;

}
