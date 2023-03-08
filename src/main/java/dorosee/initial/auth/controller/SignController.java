package dorosee.initial.auth.controller;

import dorosee.initial.auth.dto.SignInUser;
import dorosee.initial.auth.dto.UserDto;
import dorosee.initial.auth.service.SignService;
import dorosee.initial.auth.vo.UserDetailVo;
import dorosee.initial.auth.vo.UserVo;
import dorosee.initial.config.basestatus.BaseException;
import dorosee.initial.config.basestatus.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/api/auth")
@RequiredArgsConstructor
public class SignController {
    private final SignService signService;

    @PostMapping
    public BaseResponse<UserDto> join(@RequestBody SignInUser signInUser) throws BaseException {

        try {
            UserVo userVO = signInUser.getUser();
            UserDetailVo userDetailVO = signInUser.getUserDetail();

            UserDto result = signService.signInUser(userVO, userDetailVO);

            return new BaseResponse<>(result, null);

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

}
