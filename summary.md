# #项目

## 		1.1项目搭建

### 		1.1.1创建父级项目

​					一 .  先创建一个**父项目** **mingrui-shop-parent**  并删除src文件夹  (因为父级项目不需要写代码)

​							添加要用到的pom文件

​					二 .   在父项目上右键一个 NEW---> Module  创建一个项目 **mingrui-shop-basics** (这是一个**服务父工**

​							**程项目**) 删除src文件夹 添加pom相关的文件

​					三 .  在父项目上右键一个 NEW---> Module  创建一个项目 **mingrui-shop-common**(这是一个**公共工程**) 

​							删除src文件夹 添加pom相关的文件

​					四 .  在父项目上右键一个 NEW---> Module  创建一个项目 **mingrui-shop-server**(创建一个**服务实现的项目**)

​							删除src文件夹 添加pom相关的文件

​					五 .  在父项目上右键一个 NEW---> Module  创建一个项目 **mingrui-shop-server**-**api******(创建一个**服务接口**)

​							删除src文件夹 添加pom相关的文件

​		

### 		1.1.2 搭建子项目

​			1 . 在mingrui-shop-basics右键NEW---> Module  **mingrui-shop-basics-eureka-service**(创建eureka服务)

​		添加  pom 文件   

新建 application.yml

```js
spring:
  application:
    name: eureka-server
eureka:
  client:
    # eureka服务url,值为map集合默认key为defaultZone
    service-url:
      defaultZone: http://${eureka.instance.hostname}:${server.port}/eureka
    # 当前服务是否同时注册
    register-with-eureka: false
    # 去注册中心获取其他服务的地址
    fetch-registry: false
  instance:
    hostname: localhost
    # 定义服务续约任务（心跳）的调用间隔，单位：秒 默认30
    lease-renewal-interval-in-seconds: 1
    # 定义服务失效的时间，单位：秒 默认90
    lease-expiration-duration-in-seconds: 2
  server:
    # 测试时关闭自我保护机制，保证不可用服务及时踢出
    enable-self-preservation: false
```

创建 com.baidu 的包 新建启动类  RunEurekaServerApplication

```java
@SpringBootApplication
@EnableEurekaServer
public class RunEurekaServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(RunEurekaServerApplication.class);
    }
}
```

​					

## 1.2 商品管理

### 	1.2.1分类管理的查询

​				在mingrui-shop-common 右键NEW---> Module 新建 mingrui-shop-common-core 项目  

​			写一下pom文件

```html
<dependencies>
    <!-- SpringBoot-整合Web组件 -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <!--处理json与各种数据类型或文档类型的转换-->
    <dependency>
        <groupId>com.google.code.gson</groupId>
        <artifactId>gson</artifactId>
        <version>2.8.5</version>
    </dependency>
    <!--json对象序列化和反序列化的支持-->
    <dependency>
        <groupId>org.codehaus.jackson</groupId>
        <artifactId>jackson-core-lgpl</artifactId>
        <version>1.9.13</version>
    </dependency>
    <dependency>
        <groupId>org.codehaus.jackson</groupId>
        <artifactId>jackson-mapper-lgpl</artifactId>
        <version>1.9.13</version>
    </dependency>
    <!--java对象和json对象之间的转换-->
    <dependency>
        <groupId>org.codehaus.jackson</groupId>
        <artifactId>jackson-core-asl</artifactId>
        <version>1.9.13</version>
    </dependency>
    <dependency>
        <groupId>org.codehaus.jackson</groupId>
        <artifactId>jackson-mapper-asl</artifactId>
        <version>1.9.13</version>
    </dependency>
    <!--alibaba的json处理工具-->
    <dependency>
        <groupId>com.alibaba</groupId>
        <artifactId>fastjson</artifactId>
        <version>1.2.62</version>
    </dependency>
    <dependency>
        <groupId>com.fasterxml.jackson.core</groupId>
        <artifactId>jackson-databind</artifactId>
        <version>2.11.2</version>
    </dependency>
    <!--帮助开发人员快速生成API文档-->
    <dependency>
        <groupId>io.springfox</groupId>
        <artifactId>springfox-swagger2</artifactId>
        <version>2.9.2</version>
    </dependency>
    <dependency>
        <groupId>com.belerweb</groupId>
        <artifactId>pinyin4j</artifactId>
        <version>2.5.1</version>
    </dependency>
</dependencies>
```

新建com.baidu.shop.utils包  新建JSONUTIL类

```java
package com.baidu.shop.utils;

import com.alibaba.fastjson.JSONObject;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class JSONUtil {
    private static Gson gson = null;
    static {
        gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();// todo yyyy-MM-dd HH:mm:ss
    }
    public static synchronized Gson newInstance() {
        if (gson == null) {
            gson = new GsonBuilder().setDateFormat("yyyy-MM-dd  HH:mm:ss").create();
        }
        return gson;
    }
    public static String toJsonString(Object obj) {
        return gson.toJson(obj);
    }
    public static <T> T toBean(String json, Class<T> clz) {
        return gson.fromJson(json, clz);
    }
    public static <T> Map<String, T> toMap(String json, Class<T> clz) {
        Map<String, JsonObject> map = gson.fromJson(json, new
                TypeToken<Map<String, JsonObject>>() {
                }.getType());
        Map<String, T> result = new HashMap<String, T>();
        for (String key : map.keySet()) {
            result.put(key, gson.fromJson(map.get(key), clz));
        }
        return result;
    }
    public static Map<String, Object> toMap(String json) {
        Map<String, Object> map = gson.fromJson(json, new TypeToken<Map<String,
                Object>>() {
        }.getType());
        return map;
    }
    public static <T> List<T> toList(String json, Class<T> clz) {
        JsonArray array = new JsonParser().parse(json).getAsJsonArray();
        List<T> list = new ArrayList<T>();
        for (final JsonElement elem : array) {
            list.add(gson.fromJson(elem, clz));
        }
        return list;
    }
    /**
     * 从json字符串中获取需要的值
     *
     * @param json
     * @param clazz 要转换的类型
     * @return
     */
    public static <T> Object getObjectByKey(String json, Class<T> clazz) {
        if (json != null && !"".equals(json)) {
            return JSONObject.parseObject(json, clazz);
        }
        return null;
    }
    /**
     * 从json字符串中获取需要的值
     *
     * @param json
     * @param clazz 要转换的类型
     * @return
     */
    public static <T> List<T> getListByKey(String json, Class<T> clazz) {
        if (json != null && !"".equals(json)) {
            return JSONObject.parseArray(json, clazz);
        }
        return null;
    }
    /**
     * 从json字符串中获取需要的值
     *
     * @param json
     * @param key
     * 键
     * @return
     */
    public static String getStrByKey(String json, String key) {
        String str = "";
        if (json != null && !"".equals(json)) {
            JSONObject j = JSONObject.parseObject(json);
            if (j.get(key) != null) {
                str = j.get(key).toString();
            }
        }
        return str;
    }
    /**
     * 向文件中写数据
     *
     * @param _sDestFile
     * @param _sContent
     * @throws IOException
     */
    public static void writeByFileOutputStream(String _sDestFile, String
            _sContent) throws IOException {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(_sDestFile);
            fos.write(_sContent.getBytes());
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (fos != null) {
                fos.close();
                fos = null;
            }
        }
    }
    /**
     * 非空
     *
     * @param str
     * @return true:不为空 false：空
     */
    public static boolean noEmpty(String str) {
        boolean flag = true;
        if ("".equals(str)) {
            flag = false;
        }
        return flag;
    }
    /**
     * 将"%"去掉
     *
     * @param str
     * @return
     */
    public static double getDecimalByPercentage(String str) {
        double fuse = 0.0;
        if (!"".equals(str) && str != null) {
            if (str.split("%").length > 0) {
                fuse = Double.parseDouble(str.split("%")[0]);
                return fuse;
            }
        }
        return 0.0;
    }
    /**
     * 保留2位小数
     *
     * @param number
     * @return
     */
    public static double ConversionFraction(double number) {
        return Math.floor(number * 100 + 0.5) / 100;
    }
    public static float ConversionM(double number) {
        return (float) JSONUtil.ConversionFraction(number / 1024 / 1024);
    }
    public static String getErrorText(String s) {
        JSONObject j = JSONObject.parseObject(s);
        return
                j.getJSONObject(j.keySet().iterator().next()).get("errortext").toString();
    }
    public static String getSingleJobId(String s) throws Exception {
        JSONObject j = JSONObject.parseObject(s);
        try {
            return
                    j.getJSONObject(j.keySet().iterator().next()).get("jobid").toString();
        } catch (Exception e) {
            try {
                return
                        j.getJSONObject(j.keySet().iterator().next()).get("errortext").toString();
            } catch (Exception e1) {
                throw new Exception(e1.getMessage());
            }
        }
    }
    public static <T> T readValue(String jsonStr, TypeReference type)
            throws JsonParseException, JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(jsonStr, type);
    }
    public static JSON_TYPE getJSONType(String str) {
        if (null == str || "".equals(str)) {
            return JSON_TYPE.JSON_TYPE_ERROR;
        }
        final char[] strChar = str.substring(0, 1).toCharArray();
        final char firstChar = strChar[0];
        if (firstChar == '{') {
            return JSON_TYPE.JSON_TYPE_OBJECT;
        } else if (firstChar == '[') {
            return JSON_TYPE.JSON_TYPE_ARRAY;
        } else {
            return JSON_TYPE.JSON_TYPE_ERROR;
        }
    }
    public enum JSON_TYPE {
        /** JSONObject */
        JSON_TYPE_OBJECT,
        /** JSONArray */
        JSON_TYPE_ARRAY,
        /** 不是JSON格式的字符串 */
        JSON_TYPE_ERROR
    }

}
```

新建com.baidu.shop.status 这个包 包中新建HTTPStatus这个类

```java
public class HTTPStatus {
    public static final int OK = 200;//成功
    public static final int ERROR = 500;//失败
}    
```

新建com.baidu.shop.base 这个包  包下新建Result这个类

```java
package com.baidu.shop.base;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Result<T> {
    private Integer code;//返回码
    private String message;//返回消息
    private T data;//返回数据
    public Result(Integer code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = (T) data;
    }
}
```

在这个包下新建BaseApiService 这个类

```java
package com.baidu.shop.base;

import com.baidu.shop.status.HTTPStatus;
import com.baidu.shop.utils.JSONUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Data
@Component
@Slf4j   //就是产生日志
public class BaseApiService<T> {
    public Result<T> setResultError(Integer code, String msg) {
        return setResult(code, msg, null);
    }
    // 返回错误，可以传msg
    public Result<T> setResultError(String msg) {
        return setResult(HTTPStatus.ERROR, msg, null);
    }
    // 返回成功，可以传data值
    public Result<T> setResultSuccess(T data) {
        return setResult(HTTPStatus.OK, HTTPStatus.OK + "", data);
    }
    // 返回成功，沒有data值
    public Result<T> setResultSuccess() {
        return setResult(HTTPStatus.OK, HTTPStatus.OK + "", null);
    }
    // 返回成功，沒有data值
    public Result<T> setResultSuccess(String msg) {
        return setResult(HTTPStatus.OK, msg, null);
    }
    // 通用封装
    public Result<T> setResult(Integer code, String msg, T data) {
        log.info(String.format("{code : %s , msg : %s , data : %s}",code,msg,
                JSONUtil.toJsonString(data)));
        return new Result<T>(code, msg, data);
    }

}
```

在mingrui-shop-service-api  的pom 文件下增加

```html
<!-- SpringBoot-整合Web组件 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<!--Entity 中的@Table 和@Id需要次注解-->
<dependency>
    <groupId>javax.persistence</groupId>
    <artifactId>persistence-api</artifactId>
    <version>1.0.2</version>
</dependency>
<!--2.3版本之后web删除了验证插件-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
<!--引入common工程代码-->
<dependency>
    <groupId>com.baidu</groupId>
    <artifactId>mingrui-shop-common-core</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
<!--分页工具-->
<dependency>
    <groupId>com.github.pagehelper</groupId>
    <artifactId>pagehelper-spring-boot-starter</artifactId>
    <version>1.2.10</version>
</dependency>
```

在mingrui-shop-service-api项目下新建mingrui-shopservice-api-xxx项目 

​		在pom文件中引入

```html
<!--帮助开发人员快速生成API文档-->
<dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-swagger2</artifactId>
    <version>2.9.2</version>
</dependency>
<!--提供可视化的API文档-->
<dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-swagger-ui</artifactId>
    <version>2.9.2</version>
</dependency>
```

新建包 com.baidu.shop.entity

​		包下新建CategoryEntity

```java
package com.baidu.shop.entity;

import com.baidu.shop.validate.group.MingruiOperation;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@ApiModel(value = "商品类目表")
@Data
@Table(name = "tb_category")

public class CategoryEntity {
    @Id
    @ApiModelProperty(value = "类目id",example = "1")
    @NotNull(message = "Id不为空",groups = {MingruiOperation.Update.class})
    private Integer id;

    @ApiModelProperty(value = "类目名称")
    @NotEmpty(message = "类目名称不能为空",groups = {MingruiOperation.Add.class,MingruiOperation.Update.class})
    private String name;

    @ApiModelProperty(value = "父类目id,顶级类目填0",example = "1")
    @NotNull(message = "父级id顶级父类选项不能为空",groups = {MingruiOperation.Update.class})
    private Integer parentId;

    @ApiModelProperty(value = "是否为父节点，0为否，1为是",example = "1")
    @NotNull(message = "是否为父节点不能为空",groups = {MingruiOperation.Update.class})
    private Integer isParent;

    @ApiModelProperty(value = "排序指数，越小越靠前",example = "1")
    @NotNull(message = "排序指数越小越靠前不能为空",groups = {MingruiOperation.Update.class})
    private Integer sort;
}
```

​		swagger的配置

​		新建config包 新建MrSwagger2Config

```java
package com.baidu.shop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.service.Contact;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class MrSwagger2Config {
    @Bean
    public Docket createRestApi(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(this.apiInfo()).select()
                .apis(RequestHandlerSelectors.basePackage("com.baidu"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                //标题
                .title("明瑞SWAGGER2标题")
                //条款地址
                .termsOfServiceUrl("http://www.baidu.com")
                //联系方式-->有String参数的方法但是已经过时，所以不推荐使用
                .contact(new Contact("shenyaqi","baidu.com","shenyaqiii@163.com"))
                //版本
                .version("v1.0")
                //项目描述
                .description("描述")
                //创建API基本信息
                .build();
    }

}
```



定义商品分类接口

```java
@Api(tags = "商品分类接口")
public interface CategoryService {
    @ApiOperation(value = "通过查询商品分类")
    @GetMapping(value = "category/list")
    Result<List<CategoryEntity>> getCategoryByPid(Integer pid);
}    
```





在mingrui-shop-service工程下新建mingrui-shop-servicexxx工程

​		添加启动类

```java
@SpringBootApplication
@EnableEurekaClient
@MapperScan("com.baidu.shop.mapper")
public class RunXXXApplication {
    public static void main(String[] args) {
        SpringApplication.run(RunXXXApplication.class);
    }
}
```



在com.baidu下新建包shop.mapper

```java
public interface CategoryMapper extends Mapper<CategoryEntity> {
	
}
```

7 在com.baidu下新建包shop.service.impl

```java
@RestController
public class CategoryServiceImpl extends BaseApiService implements CategoryService {
    @Resource
    private CategoryMapper mapper;

    @Override  //查询
    public Result<List<CategoryEntity>> getCategoryByPid(Integer pid) {
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setParentId(pid);
        List<CategoryEntity> list = mapper.select(categoryEntity);
        return this.setResultSuccess(list);
    }
}	
```

### 1.2.2 分类管理的删除

​				**删除所用到的思路**

​					**1 .  通过Id查询数据**

​					**2  . 判断当前节点是否存在**

​					**3  . 判断是否为父节点**

​					**4  . 通过当前父节点的id 查询当前父节点下是否还有其他子节点**

​					**5  .  根据删除的parentId 为节点 查询负极节点的状态**

​		定义商品分类接口

```java
@Api(tags = "商品分类接口")
public interface CategoryService {
    @ApiOperation(value = "通过查询商品分类")
    @GetMapping(value = "category/list")
    Result<List<CategoryEntity>> getCategoryByPid(Integer pid);

    @ApiOperation(value ="通过id删除分类")
    @DeleteMapping(value = "category/del")
    Result<JsonObject> delCategory(Integer id);
}    
```

在mingrui-shop-service-xxx 的com.baidushop.service.impl 包下写删除

```java
	@Override
    @Transactional //这个注解是为了回滚  增删改都需要这个注解
    public Result<JsonObject> delCategory(Integer id) {                    

        //检验一下Id是否合法  id不能为空并且不能是负数
        if(ObjectUtil.isNull(id) || id <= 0) return this.setResultError("id不合法");

        //通过Id查询数据
        CategoryEntity categoryEntity = mapper.selectByPrimaryKey(id);

        //判断当前节点是否存在
        if(ObjectUtil.isNull(categoryEntity)) return this.setResultError("数据不存在");

        //判断是否为父节点
        if (categoryEntity.getIsParent() == 1) return this.setResultError("当前为父节点 不能删除");

        //通过当前父节点id查询 当前父节点下是否还有其他子节点
        Example example = new Example(CategoryEntity.class);
        example.createCriteria().andEqualTo("parentId",categoryEntity.getParentId());
        List<CategoryEntity> list = mapper.selectByExample(example);
//        Example.Criteria criteria = example.createCriteria();
//        criteria.andEqualTo("parentId",categoryEntity.getParentId());
        
        //判断是否有品牌类的关系分类表
        Example example1 = new Example(BrandCategoryEntity.class);
        example1.createCriteria().andEqualTo("parentId",categoryEntity.getParentId());
        List<BrandCategoryEntity> brandCategoryEntities = categoryBrandMapper.selectByExample(example1);
        if (brandCategoryEntities.size()>0) return this.setResultError("当前节点绑定品牌,请先删除品牌后再删该节点");


        //根据删除的parentId为节点 查询父级节点的状态
        if (list.size() <=1){
            CategoryEntity UpdateCategoryEntity = new CategoryEntity();
            UpdateCategoryEntity.setParentId(0);
            UpdateCategoryEntity.setId(categoryEntity.getParentId());

            mapper.updateByPrimaryKey(UpdateCategoryEntity);
        }
        mapper.deleteByPrimaryKey(id);
        return this.setResultSuccess();
    }
```

**objectUtil** 这个工具类 **是在mingrui-shop-common-core 的包下新建的 是为了让代码代码更加的优雅**

```java
public class ObjectUtil {
    public static Boolean isNull(Object obj){
        return obj ==null;
    }

    public static Boolean isNotNull(Object obj){
        return obj != null;
    }

    public static Boolean isEmpty(Object obj){
        return (obj != null && !"".equals(obj))?true:false;
    }
}
```

​			

**删除的前台 vue** 

```vue
前台:添加一个key的属性
			category 的method 的删除方法中
			handleDelete(id) {
				this.$http.delete('/category/del?id=' + id).then(resp=> {
				  if(resp.data.code != 200){
					this.$message.error('删除失败' + resp.data.message)

					return;
				  }
				  //重新加载属性组件
				  this.key = new Date().getTime();//避免key重复
				  this.$message.success("删除成功");
				}).catch(error => console.log(error))
			  },
```



### 1.2.3 分类管理的修改

**在 mingrui-shop-service-api-xxx 的CategoryService 接口下写修改方法** 

```java
@ApiOperation(value = "修改分类")
@PutMapping(value = "category/edit")
Result<JsonObject> editCategory(@Validated({MingruiOperation.Update.class})@RequestBody CategoryEntity categoryEntity);
```

**在mingrui-shop-service-xxx 的com.baidushop.service.impl 包下写新增**

```java
@Override
@Transactional
public Result<JsonObject> editCategory(CategoryEntity categoryEntity) {
    mapper.updateByPrimaryKeySelective(categoryEntity);
    return this.setResultSuccess();
}
```

### 1.2.4 分类管理的新增

**在 mingrui-shop-service-api-xxx 的CategoryService 接口下写修改方法** 

```java
@ApiOperation(value = "新增分类")
@PostMapping(value = "category/save")
Result<JsonObject> saveCategory(@Validated({MingruiOperation.Add.class}) @RequestBody CategoryEntity categoryEntity);
```

**在mingrui-shop-service-xxx 的com.baidushop.service.impl 包下写新增**

```java
@Override
@Transactional
public Result<JsonObject> saveCategory(CategoryEntity categoryEntity) {

    //新增一个节点的时候 在新增的节点下再新增一个节点的时候把节点改为父节点
    CategoryEntity parentCategoryEntity = new CategoryEntity();
    parentCategoryEntity.setId(categoryEntity.getParentId());
    parentCategoryEntity.setIsParent(1);
    mapper.updateByPrimaryKeySelective(parentCategoryEntity);

    mapper.insertSelective(categoryEntity);
    return this.setResultSuccess();
}
```

### 1.2.5 swagger

**在mingrui-shop-service-api 的pom.xml文件中配置文件**

```html
<!--帮助开发人员快速生成API文档-->
<dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-swagger2</artifactId>
    <version>2.9.2</version>
</dependency>
<dependency>
    <groupId>com.belerweb</groupId>
    <artifactId>pinyin4j</artifactId>
    <version>2.5.1</version>
</dependency>
```

**在  mingrui-shop-service-api-xxx 的 com.baidu.shop 包下新建config包  包下新建 MrSwagger2Config 这个类**

```java
package com.baidu.shop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.service.Contact;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class MrSwagger2Config {
    @Bean
    public Docket createRestApi(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(this.apiInfo()).select()
                .apis(RequestHandlerSelectors.basePackage("com.baidu"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                //标题
                .title("明瑞SWAGGER2标题")
                //条款地址
                .termsOfServiceUrl("http://www.baidu.com")
                //联系方式-->有String参数的方法但是已经过时，所以不推荐使用
                .contact(new Contact("shenyaqi","baidu.com","shenyaqiii@163.com"))
                //版本
                .version("v1.0")
                //项目描述
                .description("描述")
                //创建API基本信息
                .build();
    }

}
```

## 1.3 品牌管理

### 1.3.1 品牌管理的查询

在item包下新建MrBrand.vue







在mingrui-shop-common-core 的pom文件下添加配置文件

```html
<!--帮助开发人员快速生成API文档-->
<dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-swagger2</artifactId>
    <version>2.9.2</version>
</dependency>
```

在base包下新建BaseDTO

```java
package com.baidu.shop.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "BaseDTO用于数据传输 其他Dao需要继承此类")
public class BaseDTO {

    @ApiModelProperty(value = "当前页",example = "1")
    private Integer page;

    @ApiModelProperty(value = "每页显示多少条",example = "5")
    private Integer rows;

    @ApiModelProperty(value = "排序字段")
    private String sort;

    @ApiModelProperty(value = "是否升序")
    private String order;

    //排序
    public String getOrderBy() {
       return sort+ " " + (Boolean.valueOf(order) ? "desc" :"asc");
    }
}
```

在mingrui-shop-service-api 的pom文件下添加配置文件

```html
<!--分页工具-->
<dependency>
    <groupId>com.github.pagehelper</groupId>
    <artifactId>pagehelper-spring-boot-starter</artifactId>
    <version>1.2.10</version>
</dependency>
```

在mingrui-shop-servie-api-xxx下的com.baidu.shop下新建dto包

包下新建BrandDTO 

```java
package com.baidu.shop.dto;

import com.baidu.shop.base.BaseDTO;
import com.baidu.shop.validate.group.MingruiOperation;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@ApiModel(value = "品牌DTO")
@Data
public class BrandDTO extends BaseDTO {
    @ApiModelProperty(value = "品牌主键" ,example = "1")
    @NotNull(message = "品牌主键不能为空" ,groups = {MingruiOperation.Update.class})
    private Integer id;

    @ApiModelProperty(value = "品牌名称" )
    @NotEmpty(message = "品牌名称不能为空" ,groups = {MingruiOperation.Add.class,MingruiOperation.Update.class})
    private String name;

    @ApiModelProperty(value ="图片名称")
    private String image;

    @ApiModelProperty(value = "品牌首字母")
    private Character letter;

    @ApiModelProperty(value = "分类ids")
    @NotEmpty(message = "分类id不能为空",groups = {MingruiOperation.Update.class,MingruiOperation.Add.class})
    private String categories;
}
```

在 entity包下新建BrandEntity 

```java
package com.baidu.shop.entity;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@ApiModel(value = "品牌表")
@Table(name = "tb_brand")
public class BrandEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String image;

    private Character letter;
}
```

在service包下新建BrandService

```java
@Api(tags = "品牌类型接口")
public interface BrandService {

    @ApiOperation(value = "品牌信息查询")
    @GetMapping(value = "brand/list")
    Result<PageInfo<BrandEntity>> getBrandInfo(BrandDTO brandDTO);
}    
```

在mingrui-shop-service-xxx的mapper包下新建BrandMapper

```java
public interface BrandMapper extends Mapper<BrandEntity> {
    
}
```

在impl包下新建 BrandServiceImpl

```java
@RestController
public class BrandServiceImpl extends BaseApiService implements BrandService {

    @Resource
    private BrandMapper mapper;

    //查询
    @Override
    public Result<PageInfo<BrandEntity>> getBrandInfo(BrandDTO brandDTO) {

        //mybatis 分页插件 mtbatis执行器
        PageHelper.startPage(brandDTO.getPage(),brandDTO.getRows());
        //排序
        //第四种办法
        if (!StringUtils.isEmpty(brandDTO.getSort())) PageHelper.orderBy(brandDTO.getOrderBy());

//        String order="";
//        if(!StringUtils.isEmpty(brandDTO.getOrder())){
        //第一种 用if和else判断是降序还是升序
//            if (Boolean.valueOf(brandDTO.getOrder())){
//                order = "desc";
//            }else {
//                order = "asc";
//            }
        //第二种 通过三目运算 判断是用降序还是升序
//            PageHelper.orderBy(brandDTO.getSort() + " " + (Boolean.valueOf(brandDTO.getOrder())?"desc":"asc"));
//        }
        //第三种
//        PageHelper.orderBy(brandDTO.getOrderBy());

        //查询与分页
        BrandEntity brandEntity = new BrandEntity();
        BeanUtils.copyProperties(brandDTO,brandEntity);

        Example example = new Example(BrandEntity.class);
        example.createCriteria().andLike("name","%" + brandEntity.getName() +"%");

        List<BrandEntity> brandEntities = mapper.selectByExample(example);
        PageInfo<BrandEntity> pageInfo = new PageInfo<>(brandEntities);

        return this.setResultSuccess(pageInfo);
    }
 }   
```

### 1.3.2 品牌管理的新增

在 mingrui-shop-common-core 下的在 utils包下新建BaiduBeanUtil

​		**克隆bean**

```java
package com.baidu.shop.utils;

import org.springframework.beans.BeanUtils;

public class BaiduBeanUtil<T> {

    public static <T> T copyProperties(Object source,Class<T> clazz){

        try {
            T t = clazz.newInstance();//创建当前类型的实例
            BeanUtils.copyProperties(source,t);
            return t;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }
}
```

在mingrui-shop-service-api-xxx 的包下新建BrandDTO

```java
@ApiModelProperty(value = "分类ids")
@NotEmpty(message = "分类id不能为空",groups = {MingruiOperation.Update.class,MingruiOperation.Add.class})
private String categories;
```

在entity包下新建CategoryBrandEntity

```java
@Data
@Table(name = "tb_category_brand")
@AllArgsConstructor
@NoArgsConstructor
public class BrandCategoryEntity {

    private Integer categoryId;
    private Integer brandId;
}
```



在 service 下新建 BrandService

```java
@ApiOperation(value = "品牌新增")
@PostMapping(value = "brand/save")
Result<JsonObject> saveBrand(@RequestBody BrandDTO brandDTO);
```

在  mingrui-shop-service-xxx 的mapper包下新建CategoryBrandMapper

```java
package com.baidu.shop.mapper;

import com.baidu.shop.entity.BrandCategoryEntity;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.special.InsertListMapper;

public interface CategoryBrandMapper extends Mapper<BrandCategoryEntity>, InsertListMapper<BrandCategoryEntity> {
}
```

新建  BrandServiceImpl

```java
@Override
@Transactional
public Result<JsonObject> saveBrand(BrandDTO brandDTO) {

    //判断是否为空
    if(!ObjectUtil.isEmpty(brandDTO.getCategories())) return this.setResultError("分类id不能为空");
    //新增返回主键
    //两种实现方式 select-key insert 加两个属性
    BrandEntity brandEntity = BaiduBeanUtil.copyProperties(brandDTO, BrandEntity.class);

    //处理品牌首字母
    brandEntity.setLetter(PinyinUtil.getUpperCase(String.valueOf(brandEntity.getName().toCharArray()[0]),false).toCharArray()[0]);
    mapper.insertSelective(brandEntity);

    //维护中间表数据
    //判断分类集合字符串是否包含,
    //判断 如果有 , 就说明新增多条数据 分割开来进行新增
    this.insertCreateBrand(brandDTO.getCategories(),brandEntity.getId());
    return this.setResultSuccess("新增成功");
}
```

#### 1.3.2.1  新增遗留问题

​		**品牌首字母没有自动识别   要将品牌首字母自动识别**

**将  将MrBrandForm.vue组件中所有关于首字母的内容删除掉**

在  common-core下 的pom文件中引入 

```html
<dependency>
    <groupId>com.belerweb</groupId>
    <artifactId>pinyin4j</artifactId>
    <version>2.5.1</version>
</dependency>
```

在  common-core 的utils包下新建PinyinUtil    **这个工具类是用来自动识别首字母的**

```java
package com.baidu.shop.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PinyinUtil {
    public static final Boolean TO_FUUL_PINYIN = true;
    public static final Boolean TO_FIRST_CHAR_PINYIN = false;
    /**
     * 获取汉字首字母或全拼大写字母
     *
     * @param chinese 汉字
     * @param isFull 是否全拼 true:表示全拼 false表示：首字母
     * @return 全拼或者首字母大写字符窜
     */
    public static String getUpperCase(String chinese, boolean isFull) {
        return convertHanzi2Pinyin(chinese, isFull).toUpperCase();
    }
    /**
     * 获取汉字首字母或全拼小写字母
     *
     * @param chinese 汉字
     * @param isFull 是否全拼 true:表示全拼 false表示：首字母
     * @return 全拼或者首字母小写字符窜
     */
    public static String getLowerCase(String chinese, boolean isFull) {
        return convertHanzi2Pinyin(chinese, isFull).toLowerCase();
    }
    /**
     * 将汉字转成拼音
     * <p>
     * 取首字母或全拼
     *
     * @param hanzi 汉字字符串
     * @param isFull 是否全拼 true:表示全拼 false表示：首字母
     * @return 拼音
     */
    private static String convertHanzi2Pinyin(String hanzi, boolean isFull) {
/***
 * ^[\u2E80-\u9FFF]+$ 匹配所有东亚区的语言
 * ^[\u4E00-\u9FFF]+$ 匹配简体和繁体
 * ^[\u4E00-\u9FA5]+$ 匹配简体
 */
        String regExp = "^[\u4E00-\u9FFF]+$";
        StringBuffer sb = new StringBuffer();
        if (hanzi == null || "".equals(hanzi.trim())) {
            return "";
        }
        String pinyin = "";
        for (int i = 0; i < hanzi.length(); i++) {
            char unit = hanzi.charAt(i);
//是汉字，则转拼音
            if (match(String.valueOf(unit), regExp)) {
                pinyin = convertSingleHanzi2Pinyin(unit);
                if (isFull) {
                    sb.append(pinyin);
                } else {
                    sb.append(pinyin.charAt(0));
                }
            } else {
                sb.append(unit);
            }
        }
        return sb.toString();
    }
    /**
     * 将单个汉字转成拼音
     *
     * @param hanzi 汉字字符
     * @return 拼音
     */
    private static String convertSingleHanzi2Pinyin(char hanzi) {
        HanyuPinyinOutputFormat outputFormat = new HanyuPinyinOutputFormat();
        outputFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        String[] res;
        StringBuffer sb = new StringBuffer();
        try {
            res = PinyinHelper.toHanyuPinyinStringArray(hanzi, outputFormat);
            sb.append(res[0]);//对于多音字，只用第一个拼音
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        return sb.toString();
    }
    /***
     * 匹配
     * <P>
     * 根据字符和正则表达式进行匹配
     *
     * @param str 源字符串
    4.1.3 mingrui-shop-service-xxx
    4.1.3.1 BrandServiceImpl
     * @param regex 正则表达式
     *
     * @return true：匹配成功 false：匹配失败
     */
    private static boolean match(String str, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.find();
    }
}
```

#### 1.3.2.2 清空form和表单验证的问题

​			在 **MrBrand.vue**

```html
<!--绑定dialog属性,方便子级组件获取模态框的状态-->
<mr-brand-form @close="dialog = false" :dialog="dialog" @refreshTable="getList"></mr-brand-form>

```

在  MrBrandFrom.vue 更改

```vue
<template>
  <div>
    <v-card-text>
      <v-form v-model="valid" ref="form">
        <v-text-field
          v-model="brand.name"
          label="品牌名称"
          :rules="nameRules"
          required
        ></v-text-field>

        <v-cascader
          url="/category/list"
          required
          v-model="brand.categories"
          multiple
          label="商品分类"
        />

        <v-layout row>
          <v-flex xs3>
            <span style="font-size: 16px; color: #444;">品牌LOGO：</span>
          </v-flex>
          <v-flex>
            <v-upload
              v-model="brand.image"
              url="/upload"
              :multiple="false"
              :pic-width="250"
              :pic-height="90"
            />
          </v-flex>
        </v-layout>
      </v-form>
    </v-card-text>

    <v-divider></v-divider>

    <v-card-actions>
      <v-spacer></v-spacer>
      <v-btn small @click="cancel()">取消</v-btn>
      <v-btn small color="primary" @click="submitForm()">确认</v-btn>
    </v-card-actions>
  </div>
</template>
<script>
export default {
  name: "MrBrandForm",
  props: {
    dialog: Boolean,
    brandDetail:Object,
    isEdit:Boolean,
  },
  //清空form表单
  watch: {
    dialog(val) {
      if(!this.isEdit){
        if (val) this.$refs.form.reset();
      }
    },
    brandDetail(val){
      //控制是新增还是修改的数据变化
      if(this.isEdit){
        //回显数据
        this.$http.get('/category/brand',{
          params:{
            brandId:this.brandDetail.id
          }
        }).then(resp => {
          let brand = val;
          brand.categories = resp.data.data;
          this.brand = brand;
        }).catch(error => console.log(error));  
      }
    }
  },
  data() {
    return {
      valid: true,
      //正则验证 
      nameRules: [
        (v) => !!v || "品牌名称不能为空",
        (v) => (v && v.length <= 10) || "品牌名称最多10个长度",
      ],
      brand: {
        name: "",
        image:'',
        categories: [],
      },
    };
  },
  methods: {
    cancel() {
      this.$emit("closeDialog");
    },
    submitForm() {
      if (!this.$refs.form.validate()) {
        return;
      }
      let formData = this.brand;
      let categoryIdArr = this.brand.categories.map((category) => category.id);
      formData.categories = categoryIdArr.join();

      this.$http({
        url:'/brand/save',
        method:this.isEdit ? 'put' : 'post',
        data:formData
      }).then((resp) => {
          if (resp.data.code != 200) {
            return;
          }
          //关闭模态框
          this.$message.success("保存成功！")
          this.cancel();
          //刷新表单
        }).catch((error) => console.log(error));
    },
  },
};
</script>
```

#### 1.3.2.3  图片上传

​		**Upload.vue line:88&89** 

```
//文件长传成功后回显后台返回的图片
this.dialogImageUrl = file.response.data;
//给input表单赋值-->提交表单
this.$emit("input", file.response.data)
```

​	 **在  MrBrandForm.vue**

```vue
<template>
  <div>
    <v-card-text>
      <v-form v-model="valid" ref="form">
        <v-text-field
          v-model="brand.name"
          label="品牌名称"
          :rules="nameRules"
          required
        ></v-text-field>

        <v-cascader
          url="/category/list"
          required
          v-model="brand.categories"
          multiple
          label="商品分类"
        />

        <v-layout row>
          <v-flex xs3>
            <span style="font-size: 16px; color: #444;">品牌LOGO：</span>
          </v-flex>
          <v-flex>
            <v-upload
              v-model="brand.image"
              url="/upload"
              :multiple="false"
              :pic-width="250"
              :pic-height="90"
            />
          </v-flex>
        </v-layout>
      </v-form>
    </v-card-text>

    <v-divider></v-divider>

    <v-card-actions>
      <v-spacer></v-spacer>
      <v-btn small @click="cancel()">取消</v-btn>
      <v-btn small color="primary" @click="submitForm()">确认</v-btn>
    </v-card-actions>
  </div>
</template>
<script>
export default {
  name: "MrBrandForm",
  props: {
    dialog: Boolean,
    brandDetail:Object,
    isEdit:Boolean,
  },
  //清空form表单
  watch: {
    dialog(val) {
      if(!this.isEdit){
        if (val) this.$refs.form.reset();
      }
    },
    brandDetail(val){
      //控制是新增还是修改的数据变化
      if(this.isEdit){
        //回显数据
        this.$http.get('/category/brand',{
          params:{
            brandId:this.brandDetail.id
          }
        }).then(resp => {
          let brand = val;
          brand.categories = resp.data.data;
          this.brand = brand;
        }).catch(error => console.log(error));  
      }
    }
  },
  data() {
    return {
      valid: true,
      //正则验证 
      nameRules: [
        (v) => !!v || "品牌名称不能为空",
        (v) => (v && v.length <= 10) || "品牌名称最多10个长度",
      ],
      brand: {
        name: "",
        image:'',
        categories: [],
      },
    };
  },
  methods: {
    cancel() {
      this.$emit("closeDialog");
    },
    submitForm() {
      if (!this.$refs.form.validate()) {
        return;
      }
      let formData = this.brand;
      let categoryIdArr = this.brand.categories.map((category) => category.id);
      formData.categories = categoryIdArr.join();

      this.$http({
        url:'/brand/save',
        method:this.isEdit ? 'put' : 'post',
        data:formData
      }).then((resp) => {
          if (resp.data.code != 200) {
            return;
          }
          //关闭模态框
          this.$message.success("保存成功！")
          this.cancel();
          //刷新表单
        }).catch((error) => console.log(error));
    },
  },
};
</script>
```

 **在mingrui-shop-basics下新建mingrui-shop-basic-upload-server**

​		**在pom 文件中添加** 

```html
<dependencies>
    <!-- SpringBoot-整合Web组件 -->
    <dependency>
    	<groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>com.baidu</groupId>
        <artifactId>mingrui-shop-common-core</artifactId>
        <version>1.0-SNAPSHOT</version>
    </dependency>
</dependencies>
```

​	**写一个  application.yml**

```
server:
  port: 8200
spring:
  application:
    name: upload-server
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka
mingrui:
  upload:
    path:
      windows: E:\\images
      linux: /shenyaqi/images
    img:
      host: http://image.mrshop.com

```

写一个启动类 

```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
/**
* @ClassName RunUploadServerApplication
* @Description: TODO
* @Author shenyaqi
* @Date 2020/8/19
* @Version V1.0
**/
@SpringBootApplication
@EnableEurekaClient
public class RunUploadServerApplication {
    public static void main(String[] args) {
    	SpringApplication.run(RunUploadServerApplication.class);
    }
}

```

**新建包com.baidu.shop.upload.controller  在包下新建UploadController**

```java
package com.baidu.shop.upload.controller;

import com.baidu.shop.base.BaseApiService;
import com.baidu.shop.base.Result;
import com.baidu.shop.status.HTTPStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

//@RestController
//@RequestMapping(value = "upload")
public class UploadController extends BaseApiService {

    //linux系统的上传目录
//    @Value(value = "${mingrui.upload.path.windows}")
    private String windowsPath;
    //window系统的上传目录
//    @Value(value = "${mingrui.upload.path.linux}")
    private String linuxPath;
    //图片服务器的地址
//    @Value(value = "${mingrui.upload.img.host}")
    private String imgHost;

//    @PostMapping
    public Result<String> uploadImg(@RequestParam MultipartFile file) {
        //判断上传的文件是否为空
        if(file.isEmpty()) return this.setResultError("上传的文件为空");

        //获取文件名
        String filename = file.getOriginalFilename();
//        String path = "";
        String os = System.getProperty("os.name").toLowerCase();

        String path = os.indexOf("win") != -1?windowsPath : os.indexOf("lin") !=-1 ?linuxPath :"";
//        if(os.indexOf("win") != -1){
//            path = windowsPath;
//        }else if(os.indexOf("lin") != -1){
//            path = linuxPath;
//        }
        //防止文件名重复
        filename = UUID.randomUUID() + filename;
        //创建文件 路径+分隔符(linux和window的目录分隔符不一样)+文件名
        File dest = new File(path + File.separator + filename);
        //判断文件夹是否存在,不存在的话就创建
        if(!dest.getParentFile().exists()) dest.getParentFile().mkdirs();
        try {
            //上传
            file.transferTo(dest);
        } catch (IllegalStateException e) {
        // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
        // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //将文件名返回页面用于页面回显
        return this.setResult(HTTPStatus.OK,"upload success!!!",imgHost + "/" + filename);

    }
}
```

**新建包:com.baidu.global  在包下新建 GlobalCorsConfig**

```java
package com.baidu.global;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class GlobalCorsConfig {
    @Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true); // 允许cookies跨域
        config.addAllowedOrigin("*");// 允许向该服务器提交请求的URI，*表示全部允许。。这里尽量限制来源域，比如http://xxxx:8080 ,以降低安全风险。。
        config.addAllowedHeader("*");// 允许访问的头信息,*表示全部
        config.setMaxAge(18000L);// 预检请求的缓存时间（秒），即在这个时间段里，对于相同的跨域请求不会再预检了
        config.addAllowedMethod("*");// 允许提交请求的方法，*表示全部允许，也可以单独设置GET、PUT等
        config.addAllowedMethod("HEAD");
        config.addAllowedMethod("GET");// 允许Get的请求方法
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("DELETE");
        config.addAllowedMethod("PATCH");
        source.registerCorsConfiguration("/**", config);
        //3.返回新的CorsFilter.
        return new CorsFilter(source);
    }
}
```

 **使用postman测试上传**

**我们将利用nginx来做代理服务,代理本地目录**

**hosts文件中新增**

```
127.0.0.1 image.mrshop.com
```

 **nginx-home/conf/nginx.conf新增**

```
server {
    listen 80;
    server_name image.mrshop.com;
    proxy_set_header X-Forwarded-Host $host;
    proxy_set_header X-Forwarded-Server $host;
    proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
		location ~ .*\.(gif|jpg|pdf|jpeg|png)$
    {
        #root D:/nginx-1.15.5/temp/images/;#指定图片存放路径(可以放在nginx文件夹路径里也可以放其他p盘)
  		root E:\images;
    }
    location / {
        root html;
        index index.html index.htm;
    }
}
```

 **重启nginx**

```
nginx.exe -s reload
```

 **浏览器输入http://image.mrshop.com/imageName.jpg测试**

**zuul的application.yml**

```
zuul:
  # 声明路由
  routes:
    # 路由名称
    api-xxx:
      # 声明将所有以/api-ribbon/的请求都转发到eureka-ribbon的服务中
      path: /api-xxx/**
      serviceId: xxx-service
    # 启用重试
    retryable: true
    # 包含此路径的不进行路由
    ignored-patterns: /upload/**
    # 忽略上传服务
    ignored-services:
      -upload-server
```

重启zuul服务测试,还是不行,为什么?因为这个配置没有从根本上去解决问题,虽然忽略了/upload的请求, 但是/上传的请求中依然包含/api-xxx

所以我们需要借助nginx来帮助我们做一些事:

只要包含/api-xxx/upload的请求都将/api-xxx去掉,

为什么是在nginx里面处理?

 因为浏览器发起请求的时候第一个经过nginx服务



 **在nginx-home/conf/nginx.conf 添加api.mrshop.com的代理配置**

```
# 上传路径的映射
# 只要包含/api-xxx/upload 都会把请求映射到8200服务
# rewrite "^/api-xxx/(.*)$" /$1 break;
# 将/api-xxx 替换成/
location /api-xxx/upload {
    proxy_pass http://127.0.0.1:8200;
    proxy_connect_timeout 600;
    proxy_read_timeout 600;
    rewrite "^/api-xxx/(.*)$" /$1 break;
}

```

### 1.3.3 品牌管理修改

​		**回显  vue 项目** 

```vue
<template>
  <v-card>
    <v-card-title>
      <v-btn color="warning" @click="addData()">新增</v-btn>

      <div class="text-xs-center">
    <v-dialog v-model="dialog" width="500">
      <v-card>
        <v-card-title class="headline grey lighten-2" primary-title>品牌{{isEdit?'修改':'新增'}}</v-card-title>

        <mr-brand-form @closeDialog="closeDialog" :dialog="dialog" :isEdit="isEdit" :brandDetail="brandDetail"/>
      </v-card>
    </v-dialog>
  </div>
      <!-- 调按钮和输入框之间的间距 -->
      <v-spacer />

      <!--
            append-icon : 图标
            label : input默认值
        -->
      <v-text-field
        append-icon="search"
        label="品牌名称"
        @keyup.enter="getTableData()"
        v-model="search"
      ></v-text-field>
    </v-card-title>
    <!-- 表格组件 -->
    <v-data-table
      :headers="headers"
      :items="desserts"
      :pagination.sync="pagination"
      :total-items="total"
      class="elevation-1"
    >
      <template slot="items" slot-scope="props">
        <td class="text-xs-center">{{ props.item.id }}</td>
        <td class="text-xs-center">{{ props.item.name }}</td>
        <td class="text-xs-center">
          <!-- src 是html标签的属性 :src="vue的属性" -->
          <img :src="props.item.image" :width="100"/>
        </td>
        <td class="text-xs-center">{{ props.item.letter }}</td>
        <td class="text-xs-center">
          <!--添加修改和删除按钮-->
          <v-btn icon color="red" @click="deleteData(props.item.id)">
              <v-icon>delete</v-icon>
          </v-btn>
          <v-btn @click="editData(props.item)" icon  color="green">
              <v-icon>edit</v-icon>
          </v-btn>
        </td>
      </template>
    </v-data-table>
  </v-card>
</template>
<script>
import MrBrandForm from './MrBrandForm';
export default {
  name: "MrBrand",
  components:{
    MrBrandForm
  },
  data() {
    return {
      brandDetail:{},
      isEdit:false,
      pagination: {},
      dialog: false,
      total: 0,
      search: "",
      headers: [
        { text: "id", align: "center", value: "id"  },
        { text: "品牌名称", align: "center", value: "name"},
        {text: "品牌logo",align: "center",value: "image"},
        {text: "首字母", align: "center", value: "letter"},
        {text: "操作", align: "center", value: "letter",value:"id"}
      ],
      desserts: [],
    };
  },
  mounted() {
    this.getTableData();
  },
  methods: {
    deleteData(id){
      this.$message.confirm('是否将此品牌删除掉').then(() =>{
        this.$http.delete('/brand/del?id=' + id).then(resp=>{
          if(resp.data.code == 200){
            this.$message.success("删除成功");
            this.getTableData();
          }else{
            this.$message.error(resp.data.message);
          }
        }).catch(error => this.$message.error(error));
      }).catch(() =>{
        this.$message.info("取消删除")
      })
    },
    closeDialog(){
      this.dialog =false;
    },
    openModel(){
      this.isEdit = false;
      this.dialog = true;
    },
    addData(){
      this.isEdit=false;
      this.dialog=true;
    },
    editData(obj){
      this.brandDetail = obj;
      this.isEdit = true;
      this.dialog = true;
    },
    getTableData() {
      this.$http
        .get("/brand/list", {
          params: {
            page: this.pagination.page,
            rows: this.pagination.rowsPerPage,
            sort: this.pagination.sortBy,
            order: this.pagination.descending,
            name: this.search
          },
        })
        .then((resp) => {
          this.desserts = resp.data.data.list;
          this.total = resp.data.data.total;
        })
        .catch((error) => console.log(error));
    }
  },
  watch: {
    pagination() {
      this.getTableData();
    }
  }
};
</script>
```



**修改一下 MrBrandForm.vue**

```vue
<template>
  <div>
    <v-card-text>
      <v-form v-model="valid" ref="form">
        <v-text-field
          v-model="brand.name"
          label="品牌名称"
          :rules="nameRules"
          required
        ></v-text-field>

        <v-cascader
          url="/category/list"
          required
          v-model="brand.categories"
          multiple
          label="商品分类"
        />

        <v-layout row>
          <v-flex xs3>
            <span style="font-size: 16px; color: #444;">品牌LOGO：</span>
          </v-flex>
          <v-flex>
            <v-upload
              v-model="brand.image"
              url="/upload"
              :multiple="false"
              :pic-width="250"
              :pic-height="90"
            />
          </v-flex>
        </v-layout>
      </v-form>
    </v-card-text>

    <v-divider></v-divider>

    <v-card-actions>
      <v-spacer></v-spacer>
      <v-btn small @click="cancel()">取消</v-btn>
      <v-btn small color="primary" @click="submitForm()">确认</v-btn>
    </v-card-actions>
  </div>
</template>
<script>
export default {
  name: "MrBrandForm",
  props: {
    dialog: Boolean,
    brandDetail:Object,
    isEdit:Boolean,
  },
  //清空form表单
  watch: {
    dialog(val) {
      if(!this.isEdit){
        if (val) this.$refs.form.reset();
      }
    },
    brandDetail(val){
      //控制是新增还是修改的数据变化
      if(this.isEdit){
        //回显数据
        this.$http.get('/category/brand',{
          params:{
            brandId:this.brandDetail.id
          }
        }).then(resp => {
          let brand = val;
          brand.categories = resp.data.data;
          this.brand = brand;
        }).catch(error => console.log(error));  
      }
    }
  },
  data() {
    return {
      valid: true,
      //正则验证 
      nameRules: [
        (v) => !!v || "品牌名称不能为空",
        (v) => (v && v.length <= 10) || "品牌名称最多10个长度",
      ],
      brand: {
        name: "",
        image:'',
        categories: [],
      },
    };
  },
  methods: {
    cancel() {
      this.$emit("closeDialog");
    },
    submitForm() {
      if (!this.$refs.form.validate()) {
        return;
      }
      let formData = this.brand;
      let categoryIdArr = this.brand.categories.map((category) => category.id);
      formData.categories = categoryIdArr.join();

      this.$http({
        url:'/brand/save',
        method:this.isEdit ? 'put' : 'post',
        data:formData
      }).then((resp) => {
          if (resp.data.code != 200) {
            return;
          }
          //关闭模态框
          this.$message.success("保存成功！")
          this.cancel();
          //刷新表单
        }).catch((error) => console.log(error));
    },
  },
};
</script>
```

**在 mingrui-shop-service-api-xxx 的 CategoryService 写** 

```java
@ApiOperation(value = "根据品牌id查询分类的相关数据")
@GetMapping(value = "category/brand")
public Result<List<CategoryEntity>> getBrandById(Integer brandId);}
```

**在 mingrui-shop-service-xxx 的 CategoryMapper**

```java
public interface CategoryMapper extends Mapper<CategoryEntity>{
    @Select(value = "select c.id,c.name from tb_category c where c.id in (select cb.category_id from tb_category_brand cb where cb.brand_id=#{brandId})")
    List<CategoryEntity> getByBrandId(Integer brandId);
}
```

**在  CategoryServiceImpl 写** 

```java
@Override
public Result<List<CategoryEntity>> getBrandById(Integer brandId) {
    List<CategoryEntity> byBrandId = mapper.getByBrandId(brandId);
    return this.setResultSuccess(byBrandId);
}
```



**修改的vue 项目**

**在  MrBrandForm.vue**

```vue
    submitForm() {
      if (!this.$refs.form.validate()) {
        return;
      }
      let formData = this.brand;
      let categoryIdArr = this.brand.categories.map((category) => category.id);
      formData.categories = categoryIdArr.join();

      this.$http({
        url:'/brand/save',
        method:this.isEdit ? 'put' : 'post',
        data:formData
      }).then((resp) => {
          if (resp.data.code != 200) {
            return;
          }
          //关闭模态框
          this.$message.success("保存成功！")
          this.cancel();
          //刷新表单
        }).catch((error) => console.log(error));
    },
```

**在 mingrui-shop-service-api-xxx 的 BrandService 写品牌信息的修改**

```java
@ApiOperation(value = "品牌修改")
@PutMapping(value = "brand/save")
Result<JsonObject> editBrand(@RequestBody BrandDTO brandDTO);
```

**在 mingrui-shop-service-xxx 的BrandServiceImpl 写**

```java
//修改
@Override
@Transactional
public Result<JsonObject> editBrand(BrandDTO brandDTO) {
    //把brandDTO复制到BrandEntity中  克隆
    BrandEntity brandEntity = BaiduBeanUtil.copyProperties(brandDTO, BrandEntity.class);
    //brandEntity.getName().toCharArray()[0] 转换成一个char类型的数组 获取下标的第一位
    //String.valueOf(brandEntity.getName().toCharArray()[0]  再转换成一个String类型的  因为PinyinUtil.getUpperCase要String类型
    // false 表示首字母
    //又因为 set 只要char类型的 不要String类型的
    brandEntity.setLetter(PinyinUtil.getUpperCase(String.valueOf(brandEntity.getName().toCharArray()[0]),false).toCharArray()[0]);
    mapper.updateByPrimaryKeySelective(brandEntity);

    //通过brandId删除中间表的数据
    this.deleteCreateBrandById(brandEntity.getId());
    return this.setResultSuccess();
}
```

### 1.3.4  品牌管理的删除

**在 vue项目  Mrbrand.vue**

```vue
 <v-btn icon color="red" @click="deleteData(props.item.id)">
      <v-icon>delete</v-icon>
 </v-btn>
```

```vue
 deleteData(id){
      this.$message.confirm('是否将此品牌删除掉').then(() =>{
        this.$http.delete('/brand/del?id=' + id).then(resp=>{
          if(resp.data.code == 200){
            this.$message.success("删除成功");
            this.getTableData();
          }else{
            this.$message.error(resp.data.message);
          }
        }).catch(error => this.$message.error(error));
      }).catch(() =>{
        this.$message.info("取消删除")
      })
    }
```

**在 mingrui-shop-api-xxx 的 BrandService 写删除方法**

```java
@ApiOperation(value = "品牌删除")
@DeleteMapping(value = "brand/del")
Result<JsonObject> delBrand(Integer id);
```

在 mingrui-shop-service-xxx  的 BrandServiceImpl 中

```java
//删除
@Override
public Result<JsonObject> delBrand(Integer id) {
    //删除品牌
    mapper.deleteByPrimaryKey(id);
    //通过brandId删除中间表的数据
    this.deleteCreateBrandById(id);
    return this.setResultSuccess("");
}
```

####  1.3.5 公共部分使代码更整洁 

```java
//提出来公共的部分 使代码更简洁
private void insertCreateBrand(String categories,Integer brandId){
    //批量新增 \ 新增
    if (StringUtils.isEmpty(categories)) throw new RuntimeException();
    //判断 如果有 , 就说明新增多条数据 分割开来进行新增
    if (categories.contains(",")){
        categoryBrandMapper.insertList(
            Arrays.asList(categories.split(","))
                .parallelStream()
                .map(str -> new BrandCategoryEntity(Integer.valueOf(str),brandId))
                .collect(Collectors.toList())
        );
    //否则就是新增一条数据 普通的新增
    }else {
        BrandCategoryEntity entity = new BrandCategoryEntity(Integer.valueOf(categories),brandId);
        categoryBrandMapper.insertSelective(entity);
    }
}
```

```java
//提出来公共部分 删除中间表的数据
private void deleteCreateBrandById(Integer brandId){
    //通过brandId删除中间表的数据
    Example example = new Example(BrandCategoryEntity.class);
    example.createCriteria().andEqualTo("brandId",brandId);
    categoryBrandMapper.deleteByExample(example);
}
```

## 1.4 商品规格

###   	1.4.1 加载分类信息

​		**vue项目  的 index.js line:29**

```vue
route("/item/specification",'/item/specification/Specification',"Specification"),

```

**specification/Specification.vue**

```vue
<!--修改成我们自己的url-->
<v-tree url="/category/list" :isEdit="false" @handleClick="handleClick" />
```

### 		1.4.2 规格组(查询)

​				**vue项目 的 SpecGroup.vue**  72 行

```vue
 loadData(){
          this.$http.get("/specgroup/getSpecGroupInfo?cid=" + this.cid)
          .then(({data}) => {
              this.groups = data.data;
          })
          .catch(() => {
              this.groups = [];
          })
      },
```

**在 mingrui-shop-service-api-xxx 的entity包下新建SpecGroupEntity**

```java
package com.baidu.shop.entity;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "tb_spec_group")
@Data
public class SpecGroupEntity {
    @Id
    private Integer id;

    private Integer cid;

    private String name;
}
```

**在 dto包下新建SpecGroupDTO** 

```java
package com.baidu.shop.dto;

import com.baidu.shop.validate.group.MingruiOperation;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


import javax.validation.constraints.NotNull;

@ApiModel
@Data
public class SpecGroupDTO extends BrandDTO{

    @ApiModelProperty(value = "主键",example = "1")
    @NotNull(message = "主键不为空",groups = {MingruiOperation.Update.class})
    private Integer id;

    @ApiModelProperty(value = "类型id",example = "1")
    @NotNull(message = "类型id不能为空",groups = {MingruiOperation.Add.class})
    private Integer cid;

    @ApiModelProperty(value = "规格组名称",example = "1")
    @NotNull(message = "规格组名不能为空",groups = {MingruiOperation.Add.class})
    private String name;
}
```

**在 service包下新建SpecificationService** 

```java
@Api(tags = "规格接口")
public interface SpecificationService {
    @ApiOperation(value = "通过条件查询规格组")
    @GetMapping(value = "specgroup/getSpecGroupInfo")
    Result<List<SpecGroupEntity>> getSpecGroupInfo(SpecGroupDTO specGroupDTO);
 }   
```

**在 mingrui-shop-service-xxx 的 mapper包下新建SpecGroupMapper**

```java
public interface SpecGroupMapper extends Mapper<SpecGroupEntity> {
}

```

**在 service.impl包下新建SpecificationServiceImpl** 

```java
@RestController
public class SpecificationServiceImpl extends BaseApiService implements SpecificationService {

    @Resource
    private SpecGroupMapper specGroupMapper;
    
        @Override
    public Result<List<SpecGroupEntity>> getSpecGroupInfo(SpecGroupDTO specGroupDTO) {

        Example example = new Example(SpecGroupEntity.class);

        example.createCriteria().andEqualTo("cid",BaiduBeanUtil.copyProperties(specGroupDTO,SpecGroupEntity.class).getCid());

        List<SpecGroupEntity> specGroupEntities = specGroupMapper.selectByExample(example);
        return this.setResultSuccess(specGroupEntities);
    }
```

### 1.4.3 规格组(新增,删除,修改)

​			**vue 项目 SpecGroup.vue**

```vue
	save(){
           this.$http({
            method: this.isEdit ? 'put' : 'post',
            url: '/specgroup/save',
            data: this.group
          }).then(() => {
            // 关闭窗口
            this.show = false;
            this.$message.success("保存成功！");
            this.loadData();
          }).catch(() => {
              this.$message.error("保存失败！");
            });
      },
      deleteGroup(id){
          this.$message.confirm("确认要删除分组吗？")
          .then(() => {
            this.$http.delete("/specgroup/delete/" + id)
                .then((req) => {
                    if(req.data.code==200){
                        this.$message.success("删除成功");
                        this.loadData();
                    }else{
                        this.$message.error(req.data.message);
                    }
                   
                })
          })
      },
```

**在 mingrui-shop-service-api-xxx  的SpecificationService** 

```java
@ApiOperation(value = "新增规格组")
@PostMapping(value = "specgroup/save")
Result<JsonObject> saveGroupInfo(@RequestBody SpecGroupDTO specGroupDTO);

@ApiOperation(value = "修改规格组")
@PutMapping(value = "specgroup/save")
Result<JsonObject> editGroupInfo(@RequestBody SpecGroupDTO specGroupDTO);

@ApiOperation(value = "删除规格组")
@DeleteMapping(value = "specgroup/delete/{id}")
Result<JsonObject> deleteGroupId(@PathVariable Integer id);
```

**在  mingrui-shop-service-xxx 的  SpecificationServiceImpl**

```java
@Override
@Transactional
public Result<JsonObject> editGroupInfo(SpecGroupDTO specGroupDTO) {
    specGroupMapper.updateByPrimaryKeySelective(BaiduBeanUtil.copyProperties(specGroupDTO,SpecGroupEntity.class));
    return this.setResultSuccess();
}

@Override
@Transactional
public Result<JsonObject> deleteGroupId(Integer id) {

    //删除规格组之前判断一下当前规格组下是否有规格参数.
    Example example = new Example(SpecParamEntity.class);
    example.createCriteria().andEqualTo("groupId",id);
    List<SpecParamEntity> specParamEntities = specParamMapper.selectByExample(example);
    if (specParamEntities.size()>0) return this.setResultError("当前规格下有其他规格 不能删除");
    //如果有就是true 不能被删除
    //没有就是false 可以被删除

    specGroupMapper.deleteByPrimaryKey(id);
    return this.setResultSuccess();
}

@Override
public Result<List<SpecParamEntity>> getSpecParamInfo(SpecParamDTO specParamDTO) {
    SpecParamEntity specParamEntity = BaiduBeanUtil.copyProperties(specParamDTO, SpecParamEntity.class);
    Example example = new Example(SpecParamEntity.class);
    example.createCriteria().andEqualTo("groupId",specParamEntity.getGroupId());
    List<SpecParamEntity> specParamMappers = specParamMapper.selectByExample(example);
    return this.setResultSuccess(specParamMappers);
}
```

### 1.4.4 规格参数(查询)

**vue项目的 SpecParam.vue**

```vue
 loadData() {
      this.$http
        .get("/specParam/getSpecParamInfo?groupId=" + this.group.id)
        .then(({data}) => {
          data.data.forEach(p => {
              p.segments = p.segments ? p.segments.split(",").map(s => s.split("-")) : [];
          })
          this.params = data.data;
        })
        .catch(() => {
          this.params = [];
        });
    },
```

**在 mingrui-shop-service-api-xxx 的 entity包下新建SpecParamEntity**

```java
package com.baidu.shop.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "tb_spec_param")
@Data
public class SpecParamEntity {

    @Id
    private Integer id;

    private Integer cid;

    private Integer groupId;

    private String name;

    //numeric 是一个关键字 转换需要这样转换
    @Column(name = "`numeric`")
    private Boolean numeric;

    private String unit;

    private Boolean generic;

    private Boolean searching;

    private String segments;
}
```

**在  dto包下新建SpecParamDTO**

```java
package com.baidu.shop.dto;

import com.baidu.shop.base.BaseDTO;
import com.baidu.shop.validate.group.MingruiOperation;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;
import sun.plugin2.message.Message;

import javax.validation.constraints.NotNull;

@ApiModel(value = "规格参数数据传输")
@Data
public class SpecParamDTO extends BaseDTO {

    @ApiModelProperty(value = "主键",example = "1")
    @NotNull(message = "主键不能为空",groups = {MingruiOperation.Update.class})
    private Integer id;

    @ApiModelProperty(value = "分类id",example = "1")
    private Integer cid;

    @ApiModelProperty(value = "规格组id",example = "1")
    private Integer groupId;

    @ApiModelProperty(value = "规格参数名称")
    private String name;

    @ApiModelProperty(value = "是否是数字类型参数",example = "0")
    @NotNull(message = "是否是数字类型参数不能为空",groups = {MingruiOperation.Add.class,MingruiOperation.Update.class})
    private Boolean numeric;

    @ApiModelProperty(value = "数字类型参数的单位,非数字类型可以为空")
    private String unit;

    @ApiModelProperty(value = "是否为sku通用属性  1是true 0是false",example = "0")
    @NotNull(message = "是否为sku通用属性不能为空",groups = {MingruiOperation.Add.class,MingruiOperation.Update.class})
    private Boolean generic;

    @ApiModelProperty(value = "是否用于搜索过滤 1是true 0是false",example = "0")
    @NotNull(message = "是否用于搜索过滤不能为空",groups = {MingruiOperation.Add.class,MingruiOperation.Update.class})
    private Boolean searching;

    @ApiModelProperty(value = "数值类型参数,如果需要搜索,则添加分段间隔值")
    private String segments;
}
```

**在 SpecificationService** 

```java
@ApiOperation(value = "查询规格参数")
@GetMapping(value = "specParam/getSpecParamInfo")
Result<List<SpecParamEntity>> getSpecParamInfo(SpecParamDTO specParamDTO);
```

**在 mingrui-shop-service-xxx 的  mapper包下新建SpecParamMapper**

```java
public interface SpecParamMapper extends Mapper<SpecParamEntity> {
}

```

**在 SpecificationServiceImpl**

```java
  @Resource
    private SpecParamMapper specParamMapper;

@Override
public Result<List<SpecParamEntity>> getSpecParamInfo(SpecParamDTO specParamDTO) {
    SpecParamEntity specParamEntity = BaiduBeanUtil.copyProperties(specParamDTO, SpecParamEntity.class);
    Example example = new Example(SpecParamEntity.class);
    example.createCriteria().andEqualTo("groupId",specParamEntity.getGroupId());
    List<SpecParamEntity> specParamMappers = specParamMapper.selectByExample(example);
    return this.setResultSuccess(specParamMappers);
}
```

**1.4.5 5 规格参数(增,删,改)**

**vue项目 的 SpecParam.vue**

```vue
 deleteParam(id) {
        this.$message.confirm("确认要删除该参数吗？")
        .then(() => {
            this.$http.delete("/specParam/delete?id=" + id)
            .then(() => {
                this.$message.success("删除成功");
                this.loadData();
            })
            .catch(() => {
                this.$message.error("删除失败");
            })
        })
    },
    formatBoolean(boo) {
      return boo ? "是" : "否";
    },
    save(){
        const p = {};
        Object.assign(p, this.param);
        p.segments = p.segments.map(s => s.join("-")).join(",")
        this.$http({
            method: this.isEdit ? 'put' : 'post',
            url: '/specParam/save',
            data: p,
        }).then(() => {
            // 关闭窗口
            this.show = false;
            this.$message.success("保存成功！");
            this.loadData();
          }).catch(() => {
              this.$message.error("保存失败！");
            });
    }
```

**在 mingrui-shop-service-api-xxx 的 SpecificationService**

```java
@ApiOperation(value = "新增规格参数")
@PostMapping(value = "specParam/save")
Result<JsonObject> saveParamInfo(@RequestBody SpecParamDTO specParamDTO);

@ApiOperation(value = "修改规格参数")
@PutMapping(value = "specParam/save")
Result<JsonObject> editParamInfo(@RequestBody SpecParamDTO specParamDTO);

@ApiOperation(value="删除规格参数")
@DeleteMapping(value = "specParam/delete")
Result<JsonObject> deleteParamId(Integer id);
```

**在 mingrui-shop-service-xxx的 SpecificationServiceImpl**

```java
@Override
public Result<JsonObject> saveParamInfo(SpecParamDTO specParamDTO) {
    specParamMapper.insertSelective(BaiduBeanUtil.copyProperties(specParamDTO,SpecParamEntity.class));
    return this.setResultSuccess();
}

@Override
public Result<JsonObject> editParamInfo(SpecParamDTO specParamDTO) {
    specParamMapper.updateByPrimaryKeySelective(BaiduBeanUtil.copyProperties(specParamDTO,SpecParamEntity.class));
    return this.setResultSuccess();
}

@Override
public Result<JsonObject> deleteParamId(Integer id) {
    specParamMapper.deleteByPrimaryKey(id);
    return this.setResultSuccess();
}
```

