package dorosee.initial.auth.controller;

import dorosee.initial.auth.dto.UserDto;
import dorosee.initial.auth.entity.User;
import dorosee.initial.auth.service.UserService;
import dorosee.initial.config.basestatus.BaseException;
import dorosee.initial.config.basestatus.BaseResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/v1/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /*
     username에 해당하는 유저 정보 가져오기
      */
    @GetMapping
    public BaseResponse<UserDto> getUserInfo(HttpServletRequest req) {

        String userId = ((User) req.getAttribute("user")).getUserId();

        try {
            UserDto userDto = userService.getUser(userId);

            return new BaseResponse<>(userDto, null);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    // 모든 유저 정보 가져오기
    @GetMapping("/list")
    public BaseResponse<List<UserDto>> getUsersInfo() {
        try {
            List<UserDto> getUsersInfo = userService.getUsers();
            return new BaseResponse<>(null, getUsersInfo);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /*
    유저 삭제
     */
    @DeleteMapping
    public BaseResponse<String> deleteUser(HttpServletRequest req) throws BaseException {

        String userId = ((User) req.getAttribute("user")).getUserId();

        try {
            userService.deleteUserByUserId(userId);
            return new BaseResponse<>("success", null);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @PostMapping
    public BaseResponse<Map<String, String>> updateUserInfo(HttpServletRequest req,
                                                            @RequestBody User user) throws BaseException {
        String userId = ((User) req.getAttribute("user")).getUserId();
        String originUsername = ((User) req.getAttribute("user")).getUsername();
        log.info("method::updateUserInfo, loginUserId: {}, loginUsername: {}", userId, originUsername);
        log.info("method::updateUserInfo, userId: {}", user.getUserId());

        //todo update 시 userId가 담겨오는지 여부 확인
        /**
         * TODO 아래 로직은 로그인한 사용자 토큰 정보와 수정하려는 사용자 정보(from body)의 값을 비교하여 권한을 체크
         * 하지만, 사용자가 본인의 id 값을 확인하기 어렵기 때문에(확인할 수는 있으나 다른 api 호출이 필요함) 추후 논의 필요
         */
        /*
        if (userId != null && !userId.equals(user.getId())) {
            throw new BaseException(NOT_HAVE_PERMISSION_TO_UPDATE_USER);
        }
         */
        try {
            Map<String, String> userInfo = userService.updateUser(user, userId);
            return new BaseResponse<>(userInfo, null);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
