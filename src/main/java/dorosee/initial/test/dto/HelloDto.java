package dorosee.initial.test.dto;

import dorosee.initial.test.entity.Hello;
import lombok.Getter;

@Getter
public class HelloDto {
    private int id;
    private String data;
    private String nullData;

    public HelloDto(Hello hello) {
        this.id = hello.getId();
        this.data = hello.getData();
        this.nullData = hello.getNullData();
    }
}
