package cn.lulucar.springbootshirovue.dto.session;

import lombok.Data;

import java.util.List;
import java.util.Set;

/**
 * @author wenxiaolan
 * @ClassName SessionInfo
 * @date 2024/5/28 15:36
 * 
 * <p>
 *     保存在session中的用户信息
 * </p>
 *  
 */

@Data
public class SessionUserInfo {
    private int userId;
    private String username;
    private String nickname;
    private List<Integer> roleIds;
    private List<String> menuList;
    private List<String> permissionList;
}
