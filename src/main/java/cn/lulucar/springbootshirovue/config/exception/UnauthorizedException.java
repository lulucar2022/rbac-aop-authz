package cn.lulucar.springbootshirovue.config.exception;

/**
 * @author wenxiaolan
 * @ClassName UnauthorizedException
 * @date 2024/7/14 17:01
 * @description
 */
public class UnauthorizedException extends RuntimeException{
    public UnauthorizedException() {
        super("用户无此接口权限");
    }
}
