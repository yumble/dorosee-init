package dorosee.initial.test.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/v1/api/hello")
@RequiredArgsConstructor
public class HelloController {

    @GetMapping
    public Map<String, Object> hello() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("message", "hello");
        map.put("timestamp", System.currentTimeMillis());
        map.put("date", new Date());
        return map;
    }
}
