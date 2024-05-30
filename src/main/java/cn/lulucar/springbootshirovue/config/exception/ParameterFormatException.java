package cn.lulucar.springbootshirovue.config.exception;

/**
 * @author wenxiaolan
 * @ClassName ParameterFormatException
 * @date 2024/5/30 22:14
 * @description
 */
public class ParameterFormatException extends RuntimeException{
    public ParameterFormatException(String msg) {
        super(msg);
    }
}
