package CodeIt.Ytrip.controller;

import CodeIt.Ytrip.common.exception.StatusCode;
import CodeIt.Ytrip.common.exception.UserException;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/exception")
    public void throwException() {
        throw new UserException(StatusCode.DUPLICATE_EMAIL);
    }
}
