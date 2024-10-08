package cn.lulucar.springbootshirovue.util.model;

import com.alibaba.fastjson.JSONObject;

import java.util.List;
import java.util.Set;

/**
 * @author wenxiaolan
 * @ClassName One2Many
 * @date 2024/6/6 23:09
 * @description
 */
public class One2Many extends JSONObject {
    private Set<String> roleList;
    private Set<String> menuList;
    private Set<String> permissionList;
    private Set<Integer> permissionIds;
    private List<JSONObject> picList;
    private List<JSONObject> menus;
    private List<JSONObject> users;
    private List<JSONObject> permissions;
    private List<JSONObject> roles;
}
