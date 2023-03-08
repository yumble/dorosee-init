package dorosee.initial.test.controller;

import dorosee.initial.config.basestatus.BaseResponse;
import dorosee.initial.test.entity.Hello;
import dorosee.initial.test.repository.HelloRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("/v1/api/hello")
@RequiredArgsConstructor
public class HelloController {

    private final HelloRepository helloRepository;
    @GetMapping
    public Map<String, Object> hello() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("message", "hello");
        map.put("timestamp", System.currentTimeMillis());
        map.put("date", new Date());
        map.put("null", null);
        return map;
    }
    @GetMapping("/test")
    public BaseResponse<Map<String, Object>> test() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("message", "hello");
        map.put("timestamp", System.currentTimeMillis());
        map.put("date", new Date());
        map.put("null", null);
        return new BaseResponse<>(map, null);
    }
    @GetMapping("/test2")
    public BaseResponse<List<Hello>> test2() {
        Map<String, Object> map = new LinkedHashMap<>();

        List<Hello> byId = helloRepository.findAll();
        return new BaseResponse<>(null, byId);
    }
}
