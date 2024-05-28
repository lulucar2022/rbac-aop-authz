package cn.lulucar.springbootshirovue;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("cn.lulucar.springbootshirovue.mapper")
public class SpringbootShiroVueApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootShiroVueApplication.class, args);
    }

}
