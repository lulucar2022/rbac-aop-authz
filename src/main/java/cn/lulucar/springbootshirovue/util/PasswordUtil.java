package cn.lulucar.springbootshirovue.util;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.DefaultHashService;
import org.apache.shiro.crypto.hash.Hash;
import org.apache.shiro.crypto.hash.HashRequest;
import org.apache.shiro.util.ByteSource;

import java.nio.charset.StandardCharsets;

/**
 * @author wenxiaolan
 * @ClassName PasswordUtil
 * @date 2024/6/3 19:32
 * @description
 */
public class PasswordUtil {
    // 加密算法
    private static final String ALGORITHM_NAME = "SHA-256";
    // 迭代次数
    private static final int HASH_ITERATIONS = 1024;
   

    // 生成随机 salt  
    public static byte[] generateSalt() {
        SecureRandomNumberGenerator secureRandomNumberGenerator = new SecureRandomNumberGenerator();
        return secureRandomNumberGenerator.nextBytes(16).getBytes(); // 16 bytes is often a good size for a salt  
    }
    // 使用 salt 和 SHA-256 进行密码哈希  
    public static String encryptPassword(String password) {
        byte[] salt = generateSalt(); // 生成随机 salt  
        HashRequest request = new HashRequest.Builder()
                .setAlgorithmName(ALGORITHM_NAME)
                .setSalt(ByteSource.Util.bytes(salt))
                .setIterations(HASH_ITERATIONS)
                .setSource(ByteSource.Util.bytes(password))
                .build();

        Hash hash = new DefaultHashService().computeHash(request);

        // 这里我们假设将 salt 转换为 Base64 编码的字符串，以便与哈希值一起存储  
        String encodedSalt = org.apache.shiro.codec.Base64.encodeToString(salt);

        // 返回哈希值和 salt 的组合（在实际应用中，您可能需要一种更复杂的格式或存储方式）  
        return hash.toHex() + ":" + encodedSalt;
    }

    // 验证密码是否与存储的哈希值匹配  
    public static boolean verifyPassword(String password, String storedHashAndSalt) {
        // 假设 storedHashAndSalt 是由 hash.toHex() 和 Base64 编码的 salt 组成，中间用冒号分隔  
        String[] hashAndSaltParts = storedHashAndSalt.split(":");
        if (hashAndSaltParts.length != 2) {
            throw new IllegalArgumentException("Invalid hash and salt format");
        }

        String storedHash = hashAndSaltParts[0];
        byte[] salt = org.apache.shiro.codec.Base64.decode(hashAndSaltParts[1]);

        HashRequest request = new HashRequest.Builder()
                .setAlgorithmName(ALGORITHM_NAME)
                .setSalt(ByteSource.Util.bytes(salt))
                .setIterations(HASH_ITERATIONS)
                .setSource(ByteSource.Util.bytes(password))
                .build();

        Hash computedHash = new DefaultHashService().computeHash(request);

        return computedHash.toHex().equals(storedHash);
    }

}
