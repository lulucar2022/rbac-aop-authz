package cn.lulucar.springbootshirovue.config.exception;

/**
 * @author wenxiaolan
 * @ClassName UnauthenticatedException
 * @date 2024/7/14 17:01
 * @description
 */
public class UnauthenticatedException extends RuntimeException{
    public UnauthenticatedException() {
        super("未登录");
    }
}
