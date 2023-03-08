package dorosee.initial.auth.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserDetailVo {

    private String userId;
    private String name;
    private String mobile;
    private LocalDateTime createdDate;

}
