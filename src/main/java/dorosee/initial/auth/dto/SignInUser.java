package dorosee.initial.auth.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import dorosee.initial.auth.vo.UserDetailVo;
import dorosee.initial.auth.vo.UserVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SignInUser {

    UserVo user;
    UserDetailVo userDetail;
}
