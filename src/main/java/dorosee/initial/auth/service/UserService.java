package dorosee.initial.auth.service;

import dorosee.initial.auth.dto.UserDto;
import dorosee.initial.auth.entity.User;
import dorosee.initial.auth.entity.UserDetail;
import dorosee.initial.auth.repository.TokenRepository;
import dorosee.initial.auth.repository.UserDetailRepository;
import dorosee.initial.auth.repository.UserRepository;
import dorosee.initial.config.basestatus.BaseException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static dorosee.initial.config.basestatus.BaseResponseStatus.*;

@Transactional
@Slf4j
@Service
@AllArgsConstructor
public class UserService {
    private final TokenRepository tokenRepository;
    private final TokenService tokenService;

    private final UserRepository userRepository;
    private final UserDetailRepository userDetailRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserDto getUser(String id) throws BaseException {
        try {
            //todo password 정보 가리기
            Optional<User> user = userRepository.findById(id);
            if (user.isEmpty()) {
                throw new BaseException(NOT_FOUND_BY_USER_ID);
            }

            return new UserDto(user.get());

        } catch (BaseException exception) {
            throw new BaseException(NOT_FOUND_BY_USER_ID);
        }
    }

    public List<UserDto> getUsers() throws BaseException {
        try {

            List<User> userList = userRepository.findAll();
            if (userList.isEmpty()) {
                throw new BaseException(NOT_FOUND_ANY_USER);
            }

            return userList
                    .stream()
                    .map(UserDto::new)
                    .collect(Collectors.toList());
        } catch (BaseException exception) {
            throw new BaseException(NOT_FOUND_ANY_USER);
        }
    }

    public void deleteUserByUserId(String userId) throws BaseException {

        try {
            userRepository.deleteById(userId);
            userDetailRepository.deleteById(userId);
            tokenRepository.deleteById(userId);
        } catch (Exception exception) {
            throw new BaseException(FAIL_TO_DELETE_USER);
        }
    }

    public Map<String, String> updateUser(User modifyUser, String userId) throws BaseException {
        Map<String, String> tokenMap = null;
        Optional<User> optionalUser;
        LocalDateTime localDateTime = LocalDateTime.now();

        if ((optionalUser = userRepository.findById(userId)).isEmpty()) {
            throw new BaseException(NOT_FOUND_BY_USER_ID);
        }
        User user = optionalUser.get();
        UserDetail userDetail = userDetailRepository.findById(userId).get();
        try {
            user.setPassword(bCryptPasswordEncoder.encode(modifyUser.getPassword()));
            user.setRoles(modifyUser.getRoles());
            user.setLastModifiedDate(localDateTime);

            userDetail.setMemo(modifyUser.getUserDetail().getMemo());
            userDetail.setMobile(modifyUser.getUserDetail().getMobile());

            //username(로그인아이디)가 수정되면 Token Entity도 수정 필요
            if (!(user.getUsername().equals(modifyUser.getUsername()))) {
                //check : id 중복여부
                if (userRepository.findByUsername(modifyUser.getUsername()) != null) {
                    throw new BaseException(EXIST_BY_USER_ID);
                }
                user.setUsername(modifyUser.getUsername());
                userDetail.setName(modifyUser.getUsername());

                tokenMap = tokenService.issueAccessTokenByModify(user);
            }
            return tokenMap;
        } catch (Exception exception) {
            throw new BaseException(FAIL_TO_UPDATE_USER);
        }
    }
}
