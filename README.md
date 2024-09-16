# RBAC 权限系统
> 技术栈：
>
> - SpringBoot
>
> - MyBatisPlus
>
> - Caffeine
> 
> - Vue

# 项目介绍

采用 RBAC 权限模型，自定义注解配合 AOP 拦截接口验证用户权限，做到接口级别的权限鉴定。

用户的角色和权限信息以及token，在登录成功后缓存，每次接口的权限鉴定所需要的信息都从缓存中获取。

# 项目进度

- [x] ArticleController层的接口方法
    - [x] 封装响应结果集
    - [x] 文章更新功能
- [x] LoginController层的接口方法
    - [x] 登录
    - [x] 查询用户信息
    - [x] 登出
- [x] UserController层
    - [x] 查询用户
        - [x] 用户总数
        - [x] 用户列表

    - [x] 修改用户
        - [x] 不能修改管理员
        - [x] 修改后，需要删除用户的所有旧角色
        - [x] 再把新角色批量绑定给用户
- [x] 角色
    - [x] 角色列表
    - [x] 查询角色的全部权限
    - [x] 统计一个角色拥有多少用户
    - [x] 添加角色
        - [x] 同时绑定对应的权限
    - [x] 修改角色
        - [x] 修改角色名称
        - [x] 批量添加新权限
        - [x] 删除旧权限
    - [x] 删除角色
    - [x] 删除角色现有的全部权限
- [x] 权限
    - [x] 查询所有权限
    - [x] 查询角色的权限
    - [x] 删除角色的权限
    - [x] 插入角色的权限
- [x] 把字段`creat_time`和 `update_time`改为自动生成
- [x] 加密解密工具类
- [x] 校验参数工具类
- [x] 添加校验参数异常类，并设置在全局异常处理类中
- [x] 请求拦截器
- [x] token 处理类
    - [x] 生成 token （缓存30分钟）
    - [x] 验证 token
    - [x] 消除 token
- [x] 权限鉴定注解


# 构建项目

因为使用到 `Mybatis-Plus`，所以可以用代码生成器创建项目。创建出项目的基础框架。

在 IDEA 的插件市场下载这个 `MyBatisPlus` 插件。

# 项目配置功能

## 权限鉴定注解

**自定义注解**

```java
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiresPermissions {
    String[] value();
    
    Logical logical() default Logical.AND;
}
```

**AOP 切面**

```java
@Aspect
@Slf4j
@Component
@Order(3)
public class PermissionAspect {
    private final TokenService tokenService;

    public PermissionAspect(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Before("@annotation(cn.lulucar.springbootshirovue.config.annotation.RequiresPermissions)")
    public void before(JoinPoint joinPoint) {
        log.debug("开始校验角色权限");
        SessionUserInfo userInfo = tokenService.getUserInfo();
        log.info("用户信息：{}",userInfo);
        List<String> userCodes = userInfo.getPermissionList();
        log.info("用户权限：{}",userCodes.toString());
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        RequiresPermissions annotation = methodSignature.getMethod().getAnnotation(RequiresPermissions.class);
        String[] value = annotation.value();
        log.debug("校验权限code：{}", Arrays.toString(value));
        log.debug("用户拥有的权限code：{}", userCodes);
        // 校验用户拥有的权限与操作的权限
        if (annotation.logical() == Logical.AND) {
            // 交集关系，需要包含每一个权限
            for (String perm : value) {
                if (!userCodes.contains(perm)) {
                    log.warn("用户权限不足，缺少权限：{}",perm);
                    throw new UnauthorizedException();
                }
            }
        } else {
            // 并集关系，只要包含一个权限即可
            boolean hasPermission = false;
            for (String perm : value) {
                if (userCodes.contains(perm)) {
                    hasPermission = true;
                    break;
                }
            }
            if (!hasPermission) {
                log.warn("用户权限不足，缺少任意一种权限：{}",Arrays.toString(value));
                throw new UnauthorizedException();
            }
        }

    }
        
}

```



## Caffeine 缓存用户信息和 token

**Caffeine 配置**

```java
@Configuration
@EnableCaching
public class CacheConfig {

    /**
     * 配置默认的缓存管理器
     */
    @Primary
    @Bean("defaultCacheManager")
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(Caffeine.newBuilder()
                // 设置最后一次写入后经过固定时间过期.
                .expireAfterWrite(10, TimeUnit.SECONDS)
                // 初始的缓存空间大小
                .initialCapacity(100)
                // 缓存的最大条数
                .maximumSize(1000));
        return cacheManager;
    }

    /**
     * token 放在本地缓存
     * @return Cache 组件
     */
    @Bean("tokenCacheManager")
    public Cache<String, SessionUserInfo> caffeineCache() {
        return Caffeine.newBuilder()
                // 设置最近一次访问后刷新过期时间
                .expireAfterAccess(30L, TimeUnit.MINUTES)
                // 初始的缓存空间大小
                .initialCapacity(100)
                // 缓存的最大条数
                .maximumSize(10000)
                .build();
    }
}
```

**缓存用户信息、token**

```java
@Service
@Slf4j
public class TokenServiceImpl implements TokenService {
    private final LoginMapper loginMapper;
    private final Cache<String, SessionUserInfo> cacheMap;
    public TokenServiceImpl(LoginMapper loginMapper, Cache<String, SessionUserInfo> cacheMap) {
        this.loginMapper = loginMapper;
        this.cacheMap = cacheMap;
    }

    /**
     * 用户登录验证通过后，生成 token，记录用户已登录的状态。
     * @param username 用户名
     * @return token
     */
    @Override
    public String generateToken(String username) {
        MDC.put("username",username);
        String token = UUID.randomUUID().toString().replace("-", "").substring(0,20);
        // 设置用户信息缓存
        setCache(token,username);
        return token;
    }

    /**
     * 从 MDC中获取token，然后从缓存中获取用户信息
     * @return 用户信息
     */
    @Override
    public SessionUserInfo getUserInfo() {
        String token = MDC.get("token");
        return getUserInfoFromCache(token);
    }

    /**
     * 登出时，将 token 设置为无效
     */
    @Override
    public void invalidateToken() {
        String token = MDC.get("token");
        if (!StringUtil.isNullOrEmpty(token)) {
            cacheMap.invalidate(token);   
        } else {
            throw new CommonJsonException(ErrorEnum.E_10011);
        }
        log.debug("用户登出，清除缓存，token={}", token);
    }

    /**
     * 根据 token 查询用户信息
     * 从缓存中获取用户信息
     * @param token 令牌
     * @return 用户信息
     */
    private SessionUserInfo getUserInfoFromCache(String token) {
        // 验证token的有效性
        if (StringUtil.isNullOrEmpty(token)) {
            throw new CommonJsonException(ErrorEnum.E_20011);
        }
        log.debug("从缓存中获取用户token:token={}", token);
        SessionUserInfo info = cacheMap.getIfPresent(token);
        if (info == null) {
            log.info("缓存中不存在token={}", token);
            throw new CommonJsonException(ErrorEnum.E_20011);
        }
        return info;
    }

    /**
     * 设置用户缓存信息
     * @param token 用户令牌
     * @param username 用户名
     */
    private void setCache(String token, String username) {
        SessionUserInfo info = getUserInfoByUsername(username);
        log.info("设置用户信息缓存:token={} , username={}, info={}", token, username, info);
        cacheMap.put(token,info);
    }

    /**
     * 查询用户的会话信息
     * 如果是管理员，就获取所有菜单和权限
     * @param username 用户名
     * @return 用户会话
     */
    private SessionUserInfo getUserInfoByUsername(String username) {
        SessionUserInfo userInfo = loginMapper.getUserInfo(username);
        // 判断用户的角色
        if (userInfo.getRoleIds().contains(1)) {
            // 管理员
            userInfo.setMenuList(loginMapper.getAllMenu());
            userInfo.setPermissionList(loginMapper.getAllPermissionCode());
        }
        return userInfo;
    }
}
```

## 接口请求日志

> 使用 AOP 切面，把 controller 层的接口的**请求信息**和**异常信息**打印出来。

```java
@Aspect
@Component
@Slf4j
@Order(1)
public class WebLogAspect {
    @Pointcut("execution(public * cn.lulucar.springbootshirovue.controller..*.*(..))")
    public void webLog() {
        
    }
    
    @Pointcut("execution(public * cn.lulucar.springbootshirovue.config.exception.handler.GlobalExceptionHandler.*(..))")
    public void exceptionLog() {
        
    }

    /**
     * 在进入 controller层时记录请求信息
     *
     */
    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        log.debug("请求路径：{}，请求方法：{}", request.getRequestURI(),joinPoint.getSignature().getDeclaringTypeName()+":"+joinPoint.getSignature().getName());
        MDC.put("req",getRequestInfo(request).toJSONString());
        MDC.put("startTime",String.valueOf(System.currentTimeMillis()));
    }


    /**
     * 打印请求日志
     */
    @AfterReturning(pointcut = "webLog() || exceptionLog()", returning = "result")
    public void doAfterReturning(Object result) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        Map<String, String> map = MDC.getCopyOfContextMap();
        if (map != null){
            JSONObject jsonObject = new JSONObject(true);
            jsonObject.put("uri", request.getRequestURI());
            jsonObject.put("method", request.getMethod());
            jsonObject.put("took", System.currentTimeMillis() - Long.parseLong(map.getOrDefault("startTime", String.valueOf(System.currentTimeMillis()))));
            jsonObject.put("userId", map.getOrDefault("userId", ""));
            jsonObject.put("request", JSON.parseObject(map.getOrDefault("request", "")));
            if (result != null) {
                jsonObject.put("response",JSON.parseObject(result.toString()));
            }
            log.info(jsonObject.toJSONString());
        }
    }



    /**
     * 读取请求信息，转换为json
     */
    private JSONObject getRequestInfo(HttpServletRequest request) {
        JSONObject requestInfo = new JSONObject();
        try {
            StringBuffer requestURL = request.getRequestURL();
            requestInfo.put("requestURL",requestURL);
            String method = request.getMethod();
            requestInfo.put("method",method);
            if (request.getQueryString() != null) {
                requestInfo.put("queryString", URLDecoder.decode(request.getQueryString(), StandardCharsets.UTF_8));
            }
            String remoteAddr = request.getRemoteAddr();
            requestInfo.put("remoteAddr",remoteAddr);
            if (request instanceof ContentCachingRequestWrapper) {
                ContentCachingRequestWrapper wrapper = (ContentCachingRequestWrapper) request;
                String bodyString = new String(wrapper.getContentAsByteArray(),StandardCharsets.UTF_8);
                if (bodyString.startsWith("{")) {
                    JSONObject jsonObject = JSON.parseObject(bodyString);
                    requestInfo.put("requestBody",jsonObject);
                }
            }
        } catch (Exception e) {
            log.error("解析请求失败",e);
            requestInfo.put("parseError", e.getMessage());
        }
        return requestInfo;
    }
}
```



## MyBatisPlus-自动填充字段

> MyBatis-Plus 提供了一个便捷的自动填充功能，用于在插入或更新数据时自动填充某些字段，如创建时间、更新时间等。

**原理：**自动填充功能通过实现 `com.baomidou.mybatisplus.core.handlers.MetaObjectHandler` 接口来实现。你需要创建一个类来实现这个接口，并在其中定义插入和更新时的填充逻辑。

### 使用步骤

#### 1. 使用@TableField注解（在实体类中）

`FieldFill`枚举类有四个成员

- DEFAULT,       // 默认不处理
- INSERT,        // 插入填充字段
- UPDATE,        // 更新填充字段
- INSERT_UPDATE  // 插入和更新填充字段



```java
public class User {
    @TableField(fill = FieldFill.INSERT)
    private String createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateTime;

    // 其他字段...
}
```

#### 2. 实现 MetaObjectHandler 接口

创建一个类来实现 `MetaObjectHandler` 接口，并重写 `insertFill` 和 `updateFill` 方法。

记得用`@Component`注解，确保被 Spring 管理。

**某个字段插入和更新都要填充**：`insertFill`和`updateFill`都需要对该字段实现方法。

```java
@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("开始插入填充...");
        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("开始更新填充...");
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
    }
}
```





## MyBatisPlus-批量操作(插入，更新，删除)

> 批量操作是一种高效处理大量数据的技术，它允许开发者一次性执行多个数据库操作，从而减少与数据库的交互次数，提高数据处理的效率和性能。在MyBatis-Plus中，批量操作主要用于以下几个方面：
>
> - 数据插入（Insert）：批量插入是批量操作中最常见的应用场景之一。通过一次性插入多条记录，可以显著减少SQL语句的执行次数，加快数据写入速度。这在数据迁移、初始化数据等场景中尤为有用。
> - 数据更新（Update）：批量更新允许同时修改多条记录的特定字段，适用于需要对大量数据进行统一变更的情况，如批量修改用户状态、更新产品价格等。
> - 数据删除（Delete）：批量删除操作可以快速移除数据库中的多条记录，常用于数据清理、用户注销等场景。

### 功能概述

- 支持版本：`3.5.4 +`
- 事务控制：需手动管理（默认关闭）
- 执行结果：返回批量处理结果，便于业务判断成功与否
- 数据写入：取决于代码是否正确执行到`flushStatements`
- 兼容性：支持Spring与非Spring项目
- 异常类型：执行抛出`PersistenceException`
- 建议：对于`saveOrUpdate`方法，建议保持简单的新增或更新操作

### 类使用步骤

#### **MybatisBatch<?>**

创建MybatisBatch实例（绑定数据与sqlSessionFactory）

- 泛型：实际数据类型（实体类/非实体类）
- sqlSessionFactory：可通过容器获取，非Spring容器下需自行初始化Mybatis并记录上下文
- dataList：实际批量数据处理列表（不可为空）

#### MybatisBatch.Method<?>

创建MybatisBatch.Method实例（确定执行的Mapper类方法）

- 泛型：实际Mapper方法参数类型
- mapperClass：具体的Mapper类

#### BatchMethod<?>

执行操作（将批量参数转换为Mapper方法所需参数）

- 泛型：实际Mapper方法参数类型
- statementId：执行的MappedStatement ID
- parameterConvert：参数类型转换处理器，用于数据类型与Mapper方法参数不一致时的转换

### 示例说明

#### 一：实体类型

> 项目中有 SysRolePermission 实体类，对 rolePermissionList 进行批量插入

```java
MybatisBatch<SysRolePermission> mybatisBatch = new MybatisBatch<>(getSqlSessionFactory(), rolePermissionList);
MybatisBatch.Method<SysRolePermission> method = new MybatisBatch.Method<>(SysRolePermissionMapper.class);
mybatisBatch.execute(method.insert());
```

#### 二：非实体类型（单个字段）

> 批量插入某个类的单个属性。

```java
List<Long> ids = Arrays.asList(120000L, 120001L);
MybatisBatch<Long> mybatisBatch = new MybatisBatch<>(sqlSessionFactory, ids);
MybatisBatch.Method<User> method = new MybatisBatch.Method<>(UserMapper.class);
mybatisBatch.execute(method.insert(id -> {
    User user = new User();
    user.setTestId(id);
    return User;
}));
```

## MyBatisPlus-LambdaWrapper

### LambdaQueryWrapper

用于查询、删除语句的条件构造器

### LambdaUpdateWrapper

用于更新语句的条件构造器

# 全局异常处理

## 构建全局异常处理类

**默认异常处理器**：`defaultErrorHandler`



```java
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    
    @ExceptionHandler(Exception.class)
    public JSONObject defaultErrorHandler(HttpServletRequest request, Exception e) {
        String errorPosition = "";
        // 错误栈信息中找到发生错误的位置（文件名：行数）
        if (e.getStackTrace().length>0) {
            StackTraceElement element = e.getStackTrace()[0];
            String fileName = element.getFileName() == null ? "未找到错误文件" : element.getFileName();
            int lineNumber = element.getLineNumber();
            errorPosition = fileName + ":" + lineNumber;
        }
        JSONObject jsonObject = new JSONObject();
        // 错误代码
        jsonObject.put("code",ErrorEnum.E_400.getErrorCode());
        // 错误信息
        jsonObject.put("msg",ErrorEnum.E_400.getErrorMsg());
        // 错误发生位置
        JSONObject errorJson = new JSONObject();
        errorJson.put("errorLocation",e + "，错误位置：" + errorPosition);
        // 存入 返回结果中
        jsonObject.put("info",errorJson);
        log.error("异常",e);
        return jsonObject;
    }

    /**
     * GET/POST请求方法错误的拦截器
     * 因为开发时可能比较常见,而且发生在进入controller之前,上面的拦截器拦截不到这个错误
     * 所以定义了这个拦截器
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public JSONObject httpRequestMethodHandler() {
        return CommonUtil.errorJSON(ErrorEnum.E_500);
    }

    /**
     * 本系统自定义错误的拦截器
     * 拦截到此错误之后,就返回这个类里面的json给前端
     * 常见使用场景是参数校验失败,抛出此错,返回错误信息给前端
     */
    @ExceptionHandler(CommonJsonException.class)
    public JSONObject commonJsonExceptionHandler(CommonJsonException commonJsonException) {
        return commonJsonException.getResultJson();
    }
    
    /**
     * 权限不足报错拦截
     */
    @ExceptionHandler(UnauthorizedException.class)
    public JSONObject unauthorizedExceptionHandler() {
        return CommonUtil.errorJSON(ErrorEnum.E_502);
    }

    /**
     * 未登录报错拦截
     * 在请求需要权限的接口,而连登录都还没登录的时候,会报此错
     */
    @ExceptionHandler(UnauthenticatedException.class)
    public JSONObject unauthenticatedException() {
        return CommonUtil.errorJSON(ErrorEnum.E_20011);
    }
}
```



# 项目工具类

### JSONObject

> [阿里巴巴开源项目] FastJson 是一个高性能、轻量级的 Java 语言实现的 JSON 库，其核心类之一 JSONObject 提供了便捷的操作 JSON 对象的能力。在处理 JSON 数据序列化与反序列化过程中，JSONObject 类扮演着至关重要的角色。本文将详细总结 FastJson 中 JSONObject 的主要用法及一些常用的 API 方法。

在处理接口返回响应结果时，即可用`JSONObject`把 **code**，**msg**，**data** 封装起来，转为JSON格式返回给调用者。



### Constants

> 定义项目中的常量，例如：给响应结果定义状态码和消息。

```java
public class Constants {
    public static final String SUCCESS_CODE = "200";    
    
    public static final String SUCCESS_MSG = "OK";

    // 时间格式
    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
}
```

### ErrorEnum

> 异常枚举类，定义了各种异常的状态码和信息

```java
@Getter
public enum ErrorEnum {
    E_400("400", "请求处理异常，请稍后再试"),
    E_500("500", "请求方式有误,请检查 GET/POST"),
    E_501("501", "请求路径不存在"),
    E_502("502", "权限不足"),
    E_10008("10008", "角色删除失败,尚有用户属于此角色"),
    E_10009("10009", "账户已存在"),
    E_10010("10010", "账号/密码错误"),
    E_10011("10011","账户不存在"),
    E_20011("20011", "登陆已过期,请重新登陆"),
    E_90003("90003", "缺少必填参数");
    
    private final String errorCode;

    private final String errorMsg;

    ErrorEnum(String errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

}
```



# 遇到的问题

> 遇到的问题太零散了，没有集中记录。

## Put方法无法解析JSON

**错误结果：404 Bad Request**

JSON parse error: Invalid UTF-8 start byte 0x8e]

**原因：**Apifox软件的mock动态值的语法出错，导致无法解析。

## JSONObject 类型作为 Mapper注解查询结果类型，JSONObject 的key 为数据库中的字段名

**错误结果**：`jsonObject.get("roleName")` 为 `null`

**原因**：**JSONObject ** 是一个 `map` 结构，没有现成的属性去映射字段，所以属性名会变成字段名。需要创建一个 **DTO**类，创建对应的驼峰命名格式的属性。**MyBatis** 会自动处理简单的映射关系（ role_name —> roleName）。如果是（id -> roleId）这种情况，就需要使用`@Results`和`@Result`注解去映射字段和属性。