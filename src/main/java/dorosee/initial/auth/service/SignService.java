package dorosee.initial.auth.service;

import dorosee.initial.auth.dto.UserDto;
import dorosee.initial.auth.entity.User;
import dorosee.initial.auth.entity.UserDetail;
import dorosee.initial.auth.repository.UserDetailRepository;
import dorosee.initial.auth.repository.UserRepository;
import dorosee.initial.auth.vo.UserDetailVo;
import dorosee.initial.auth.vo.UserVo;
import dorosee.initial.config.basestatus.BaseException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

import static dorosee.initial.config.basestatus.BaseResponseStatus.DATABASE_ERROR;
import static dorosee.initial.config.basestatus.BaseResponseStatus.EXIST_BY_USER_ID;

@Transactional
@Slf4j
@Service
@AllArgsConstructor
public class SignService {

    private final UserRepository userRepository;
    private final UserDetailRepository userDetailRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    public UserDto signInUser(UserVo userVO, UserDetailVo userDetailVO) throws BaseException {

        UUID uuid = UUID.randomUUID();
        String uuidStr = uuid.toString();
        uuidStr = uuidStr.replaceAll("-", "");

        User user =User.builder()
                .userId(uuidStr)
                .username(userVO.getUsername())
                .password(userVO.getPassword())
                .roles(userVO.getRoles())
                .createdDate(LocalDateTime.now()).build();

        UserDetail userDetail = UserDetail.builder()
                .userId(uuidStr)
                .name(userDetailVO.getName())
                .mobile(userDetailVO.getMobile())
                .memo(userDetailVO.getMobile())
                .createdDate(LocalDateTime.now()).build();

        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));


        //check : 중복 ID 검사
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new BaseException(EXIST_BY_USER_ID);
        }

        try {
            User saveUser = userRepository.save(user);

            userDetailRepository.save(userDetail);

            return new UserDto(saveUser);

        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
