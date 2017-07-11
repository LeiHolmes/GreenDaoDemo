### GreenDao简介  
　　GreenDao是一个将对象映射到SQLite数据库中的轻量且快速的ORM数据库框架，本文将讲解如何集成GreenDao3.0环境，如何使用GreenDao自动生成代码及注解的使用。  
　　[GreenDao官网](http://greenrobot.org/greendao/)  
　　[GreenDao GitHub](https://github.com/greenrobot/greenDAO)
### GreenDao3.0优势
　　GreenDao相较于ORMLite等其他数据库框架有以下优势：
　　1. 一个精简的库
　　2. 性能最大化
　　3. 内存开销最小化
　　4. 易于使用的 APIs
　　5. 对 Android 进行高度优化
　　而GreenDao3.0的版本主要使用注解方式定义实体类，通过gradle插件生成相应的代码。相较于3.0之前的版本集成步骤更为便捷，使用起来也更为简单。本文使用的版本为3.2.0。
### 集成
#### 项目下build.gradle
　　GitHub中提示添加maven仓库，但是AndroidStudio项目已经默认包含了jcenter仓库，而jcenter仓库就是maven仓库的一个分支，因此不需要再添加仓库，直接添加classPath即可。
```java
buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:2.3.2'
        classpath 'org.greenrobot:greendao-gradle-plugin:3.2.1'
    }
}
```
#### app下build.gradle
　　在app下build.gradle文件中我们需要声明GreenDao插件，及配置GreenDao生成dao路径等信息，声明GreenDao依赖。
```java
apply plugin: 'org.greenrobot.greendao'

......

greendao {
    schemaVersion 1 //数据库版本号    
    daoPackage 'com.holmeslei.greendaodemo.database' //设置时生成代码的目录    
    targetGenDir 'src/main/java' //设置DaoMaster、DaoSession、Dao目录   
    //targetGenDirTest：设置生成单元测试目录    
    //generateTests：设置自动生成单元测试用例
}

dependencies {
    ......
    compile 'org.greenrobot:greendao:3.2.0'
}
```
### 初始化实体
#### 编写实体类
　　集成好使用环境后需要初始化实体，首先编写实体类。这里演示使用公司Company与雇员Employer实体例子。使用@Entity注解。莫急，注解机制在之后小节有详细讲解。
```java
@Entity
public class Company {
    @Id(autoincrement = true) //自增
    private Long id; //主键
    private String companyName; //公司名称
    private String industry; //行业
    @ToMany(referencedJoinProperty = "companyId") //设置外键companyId
    private List<Employee> employeeList; //公司与雇员建立一对多关系
}
```
```java
@Entity
public class Employee {
    @Id(autoincrement = true)
    private Long id; //主键
    private long companyId; //指向Company主键
    private String employeeName; //雇员姓名
    private int salary; //薪水
}
```
#### 生成代码
　　实体类与注解添加完毕后编译项目，就会为所有带注解的实体生成Dao文件，及DaoMaster与DaoSession。若未在build.gradle中配置过则默认生成目录为build/generated/source。若配置过则生成在配置的目录下。
#### 注解
　　3.0之后最大的不同就是使用的注解来配置实体类属性，便捷且灵活。下面来看一下各类注解：
##### 实体类注解
```java
@Entity(
        schema = "myschema",
        active = true,       
        nameInDb = "AWESOME_USERS",
        indexes = {
                @Index(value = "name DESC", unique = true)
        },
        createInDb = false
)
public class Company {
    ......
}
```
- @Entity：用于标识当前实体需要GreenDao生成代码。
- schema：项目中存在多个Schema时，表明当前实体属于哪个Schema。
- active：标记实体是否处于活动状态，活动状态才支持更新删除刷新等操作。
- nameInDb：存储在数据库中的表名，不写默认与类名一致。
- indexes：定义索引，可跨越多个列。
- createInDb：标记创建数据库表，若有多个实体关联此表可设为false避免重复创建，默认为true。

#####  属性注解
- @Id :主键Long型，可以通过`@Id(autoincrement = true)`设置自增长。
- @Property：设置一个非默认关系映射所对应的列名，默认是的使用字段名例如`@Property (nameInDb="name")`。
- @NotNul：设置数据库表当前列不能为空。
- @OrderBy：指定排序。
- @Transient：添加此标记之后不会生成数据库表的列。
- @Generated：为build之后GreenDao自动生成的注解，为防止重复，每一块代码生成后会加个hash作为标记。

##### 索引注解
```java
@Entity
public class Company {
    @Id(autoincrement = true)
    private Long id;
    @Index(unique = true)
    private String industry;
}
 
@Entity
public class Employee {
    @Id(autoincrement = true)
    private Long id;
    @Unique 
    private String employeeName;
}
```
- @Index：创建索引，通过设置name设置别名，设置unique添加约束。
- @Unique：添加唯一约束与(unique = true)作用相同。

##### 关系注解
- @ToOne：定义与另一实体一对一的关联。

```java
@Entity
public class Company {
    @Id (autoincrement = true)
    private Long id;
    private long customerId;
    @ToOne(joinProperty = "employeeId")
    private Employee employee;
}
  
@Entity
public class Employee {
    @Id(autoincrement = true)
    private Long id;
    private String employeeName;
}
```
- @ToMany：定义与多个实体对象一对多的关联，referencedJoinProperty为外键约束。
- @JoinProperty：标明目标属性的源属性。
- @JoinEntity：建立表连接关系。

```java
//第一种：Employee实体通过外键companyId指向Company实体的主键Id建立一对多关联
@Entity
public class Company {
    @Id(autoincrement = true)
    private Long id;
    private String companyName; 
    private String industry;
    @ToMany(referencedJoinProperty = "companyId")
    private List<Employee> employeeList;
}
  
@Entity
public class Employee {
    @Id(autoincrement = true)
    private Long id;
    private long companyId;
    private String employeeName;
    private int salary;
}

//第二种：Employee以companyId为外键，与Company非主键不为空的键employeeTag建立一对多关联。
@Entity
public class Company {
    @Id(autoincrement = true)
    private Long id;
    private String companyName; 
    private String industry;  
    @ToMany(joinProperties = {
            @JoinProperty(name = "employeeTag", referencedName = "companyId")
    })
    private List<Employee> employeeList;
}
  
@Entity
public class Employee {
    @Id(autoincrement = true)
    private Long id;
    private String employeeName;
    private int salary;
    @NotNull 
    private String companyId;
}
 
//第三种：通过第三者实体类建立关联，用的较少
@Entity
public class Employee {
    @Id(autoincrement = true)
    private Long id;
    private String employeeName;
    private int salary;
  
    @ToMany
    @JoinEntity(
            entity = JoinEmployeeToCompany.class,
            sourceProperty = "employeeId",
            targetProperty = "companyId"
    )
    private List<Company> companyList;
}
  
@Entity
public class JoinEmployeeToCompany {
    @Id(autoincrement = true)
    private Long id;
    private Long employeeId;
    private Long companyId;
}
  
@Entity
public class Company {
    @Id(autoincrement = true)
    private Long id;
    private String companyName; 
    private String industry;  
}
```
### 总结
　　到此，这一篇关于GreenDao3.0的集成与注解就讲解完毕了。技术渣一枚，有写的不对的地方欢迎大神们留言指正，有什么疑惑或者不懂的地方也可以在Issues中提出，我会及时解答。

