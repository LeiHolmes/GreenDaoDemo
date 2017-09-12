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
### 数据库初始化
　　首先初始化数据库与表，可封装一个工具类，这里献上我的：
```java
public class GreenDaoUtil {
    private static DaoSession daoSession;
    private static SQLiteDatabase database;
    /**
     * 初始化数据库
     * 建议放在Application中执行
     */
    public static void initDataBase(Context context) {
        //通过DaoMaster的内部类DevOpenHelper，可得到一个SQLiteOpenHelper对象。
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper
        (context, "greendaoutil.db", null); //数据库名称
        database = devOpenHelper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(database);
        daoSession = daoMaster.newSession();
    }
    
    public static DaoSession getDaoSession() {
        return daoSession;
    }

    public static SQLiteDatabase getDatabase() {
        return database;
    }
}
```
　　然后在Application中调用。
```java
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        GreenDaoUtil.initDataBase(getApplicationContext());
    }
}
```
### 插入
　　插入公司与雇员的假数据：
```java
//获取实体Dao
CompanyDao companyDao = GreenDaoUtil.getDaoSession().getCompanyDao();
EmployeeDao employeeDao = GreenDaoUtil.getDaoSession().getEmployeeDao();
//插入公司
Company company1 = new Company();
company1.setId(null);
company1.setCompanyName("Netease");
company1.setIndustry("news");
Company company2 = new Company();
company2.setId(null);
company2.setCompanyName("Tencent");
company2.setIndustry("chat");
companyDao.insert(company1);
companyDao.insert(company2);

//插入不同公司的雇员
for (int i = 0; i < 5; i++) {
    Employee employee = new Employee(null, company1.getId(), "Sherlock" + i, 11000 + i * 1000);
    employeeDao.insert(employee);
}
for (int i = 0; i < 5; i++) {
    Employee employee = new Employee(null, company2.getId(), "Richard" + i, 8000 + i * 1000);
    employeeDao.insert(employee);
}
```
　　注意：设置setId(null)，GreenDao会自动分配自增Id。
### 查询
　　由于删除与更新基本都需要先进行查询，所以咱们来看看如何进行查询：
#### QueryBuilder
　　举例：查询Tencent公司中薪水大于等于10000的职员。
```java
//查询Company表中名为Tencent的公司
Company company = companyDao.queryBuilder()
.where(CompanyDao.Properties.CompanyName.eq("Tencent"))
.unique();
//查询Employee表中属于Tencent公司且薪水水大于等于10000的Employee
List<Employee> employeeList = employeeDao.queryBuilder()
.where(EmployeeDao.Properties.CompanyId.eq(company.getId()), 
EmployeeDao.Properties.Salary.ge(10000))
.list();
```
　　注意：如果查询调用.unique()的话，需注意本次查询的结果必须唯一，否则会报错。where中为查询条件，支持多条件查询以" , "隔开。
#### Query
　　使用Query可进行重复查询，更改查询条件参数即可，还是上面的例子。
```java
//查询Company表中名为Tencent的公司
Company company = companyDao.queryBuilder()
.where(CompanyDao.Properties.CompanyName.eq("Tencent"))
.unique();
//查询Employee表中属于Tencent公司且薪水水大于等于10000的Employee
Query query = employeeDao.queryBuilder()
.where(EmployeeDao.Properties.CompanyId.eq(company.getId()), 
EmployeeDao.Properties.Salary.ge(10000))
.build();
//修改查询条件参数
query.setParameter(0, company.getId());
query.setParameter(1, 11000);
List<Employee> employeeList = query.list();
```
#### load(Long key)
　　根据主键查询一条记录
```java
Company company =  companyDao.load(1l);
```
#### loadAll()
　　查询表中所有记录
```java
List<Company> companyList = companyDao.loadAll();
List<Employee> employeeList = employeeDao.loadAll();
```
#### 原声sql查询
　　推荐通过QueryBuilder和WhereCondition.StringCondition来实现原声sql查询。
```java
Query query = companyDao.queryBuilder()
.where( new StringCondition("_ID IN " + "(SELECT USER_ID FROM USER_MESSAGE WHERE READ_FLAG = 0)"))
.build();
```
　　也可使用queryRaw()或queryRawCreate()方法来实现。
#### 多线程查询
　　如果数据量过大，对于数据库查询的操作是很耗时的，所以需要开启新的线程进行查询。
```java
private void queryThread() {
    final Query query = employeeDao.queryBuilder().build();
    new Thread(){
        @Override
        public void run() {
            List list = query.forCurrentThread().list();
        }
    }.start();
}
```
#### 查询条件判断
##### eq,noteq与like查询
　　eq判断值是否相等，通常用来具体查找，返回一条指定类型数据。
　　noteq与eq相反，判断值是否不等，通常用来模糊查找，返回指定类型的集合。
　　like相当于通配符查询，包含查询值的实体都会返回，同样模糊查找，返回指定类型的集合。
##### >、<、>=、<=查询
　　分别对应方法：
　　>: gt()
　　<: lt()
　　>=: ge()
　　<=: le()
##### isNull与isNotNull
　　为空与不为空，判断数据库中有无数据。
##### 排序
　　对查询结果进行排序，有升序与降序。
```java
List<Employee> employeeList = employeeDao.queryBuilder()
.where(EmployeeDao.Properties.CompanyId.eq(company.getId()))
.orderAsc(EmployeeDao.Properties.Salary)
.list();
```
　　上例中的`.orderAsc(EmployeeDao.Properties.Salary)`就是对查询出来的Employee按工资进行升序排序。同理降序为`.orderDesc(EmployeeDao.Properties.Salary)`。　　
### 删除
　　删除主要有三种方式：
#### deleteBykey(Long key)
　　根据key进行删除。举例：删除Tencent公司中薪水小于10000的人，需先查询出Employee表中属于Tencent公司且薪水小于10000的Employee实体，再进行删除。
```java
//查询Company表中名为Tencent的公司
Company companyTencent = companyDao.queryBuilder()
.where(CompanyDao.Properties.CompanyName.eq("Tencent"))
.unique();
if (companyTencent != null) {
    //查询Employee表中属于Tencent公司且薪水小于10000的Employee
    List<Employee> employeeList = employeeDao.queryBuilder()
    .where(EmployeeDao.Properties.CompanyId.eq(companyTencent.getId()), 
    EmployeeDao.Properties.Salary.lt(10000))
    .list();
    if (employeeList != null) {
        for (Employee employee : employeeList) {
            //进行删除
            employeeDao.deleteByKey(employee.getId());
        }
    } else {
        Log.e("greendao_test", "delete:deleteList为空");
    }
} else {
    Log.e("greendao_test", "delete:company为空");
}
```
#### delete(Employee entity)
　　根据实体进行删除。举例：删除名为Tencent的公司。
```java
//查询Company表中名为Tencent的公司
Company companyTencent = companyDao.queryBuilder()
.where(CompanyDao.Properties.CompanyName.eq("Tencent"))
.unique();
companyDao.delete(companyTencent);    
```
#### deleteAll()
　 若需删除表中所有实体，则调用此方法。举例：删除所有雇员。
```java
employeeDao.deleteAll();
```
### 更新
　　若需对某个已存入数据库实体的属性进行修改，则需进行update操作。举例：修改Netease公司中薪水小于等于13000人的名字
```java
//查询Company表中名为Netease的公司
Company companyNetease = companyDao.queryBuilder()
.where(CompanyDao.Properties.CompanyName.eq("Netease"))
.unique();
if (companyNetease != null) {
    //查询Employee表中查询Employee表中属于Netease公司且薪水小于等于13000人的Employee
    List<Employee> employeeList = employeeDao.queryBuilder()
    .where(EmployeeDao.Properties.CompanyId.eq(companyNetease.getId()), 
    EmployeeDao.Properties.Salary.le(13000))
    .list();
    if (employeeList != null) {
        for (Employee employee : employeeList) {
            //修改属性
            employee.setEmployeeName("baozi");
            //进行更新
            employeeDao.update(employee);
        }
    } else {
        Log.e("greendao_test", "update:updateList为空");
    }
} else {
    Log.e("greendao_test", "update:company为空");
}
```
### 总结
　　到此，这一篇关于GreenDao3.0的集成，注解与使用就讲解完毕了。技术渣一枚，有写的不对的地方欢迎大神们留言指正，有什么疑惑或者不懂的地方也可以在Issues中提出，我会及时解答。

