package com.holmeslei.greendaodemo.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.holmeslei.greendaodemo.R;
import com.holmeslei.greendaodemo.database.CompanyDao;
import com.holmeslei.greendaodemo.database.DaoSession;
import com.holmeslei.greendaodemo.database.EmployeeDao;
import com.holmeslei.greendaodemo.entity.Company;
import com.holmeslei.greendaodemo.entity.Employee;
import com.holmeslei.greendaodemo.util.GreenDaoUtil;

import java.util.List;

/**
 * Description:   GreenDao3.0使用Demo
 * author         xulei
 * Date           2017/7/11 10:30
 */
public class GreenDaoActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tvText;
    private CompanyDao companyDao;
    private EmployeeDao employeeDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_green_dao);
        initView();
        initData();
        initListener();
    }

    private void initData() {
        companyDao = GreenDaoUtil.getCompanyDao();
        employeeDao = GreenDaoUtil.getEmployeeDao();
        quaryAll();
    }

    private void initView() {
        tvText = (TextView) findViewById(R.id.main_text);
    }

    private void initListener() {
        findViewById(R.id.main_insert).setOnClickListener(this);
        findViewById(R.id.main_delete).setOnClickListener(this);
        findViewById(R.id.main_delete_all).setOnClickListener(this);
        findViewById(R.id.main_update).setOnClickListener(this);
        findViewById(R.id.main_quary).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_insert: //插入
                insert();
                break;
            case R.id.main_delete: //删除
                delete();
                break;
            case R.id.main_delete_all: //删除所有
                deleteAll();
                break;
            case R.id.main_update: //更新
                update();
                break;
            case R.id.main_quary: //查询
                quary();
                break;
        }
    }

    /**
     * 增
     * id传null回自动设置自增长
     */
    private void insert() {
        //插入公司
        Company companyNetease = new Company();
        companyNetease.setId(null);
        companyNetease.setCompanyName("Netease");
        companyNetease.setIndustry("news");
        Company companyTencent = new Company();
        companyTencent.setId(null);
        companyTencent.setCompanyName("Tencent");
        companyTencent.setIndustry("chat");
        companyDao.insert(companyNetease);
        companyDao.insert(companyTencent);

        //插入不同公司的雇员
        for (int i = 0; i < 5; i++) {
            Employee employee = new Employee(null, companyNetease.getId(), "Sherlock" + i, 11000 + i * 1000);
            employeeDao.insert(employee);
        }
        for (int i = 0; i < 5; i++) {
            Employee employee = new Employee(null, companyTencent.getId(), "Richard" + i, 8000 + i * 1000);
            employeeDao.insert(employee);
        }

        quaryAll();
    }

    /**
     * 删除Tencent公司中薪水小于10000的人
     * deleteBykey(Long key) ：根据主键删除一条记录。
     * delete(User entity) ：根据实体类删除一条记录，一般结合查询方法，查询出一条记录之后删除。
     * deleteAll()： 删除所有记录。
     */
    private void delete() {
        Toast.makeText(this, "删除Tencent公司中薪水小于10000的人", Toast.LENGTH_LONG).show();
        //注意不要插入两次数据，否则此处会报查询结果不唯一的Exception
        Company companyTencent = companyDao.queryBuilder().where(CompanyDao.Properties.CompanyName.eq("Tencent")).unique();
        if (companyTencent != null) {
            List<Employee> employeeList = employeeDao.queryBuilder().where(EmployeeDao.Properties.CompanyId.eq(companyTencent.getId()),
                    EmployeeDao.Properties.Salary.lt(10000)).list();
            if (employeeList != null) {
                for (Employee employee : employeeList) {
                    employeeDao.deleteByKey(employee.getId());
                }
            } else {
                Log.e("greendao_test", "delete:deleteList为空");
            }
            quaryAll();
        } else {
            Log.e("greendao_test", "delete:company为空");
        }
    }

    /**
     * 清空数据库数据
     */
    private void deleteAll() {
        companyDao.deleteAll();
        employeeDao.deleteAll();
        quaryAll();
    }

    /**
     * 修改Netease公司中薪水小于等于13000人的名字
     */
    private void update() {
        Toast.makeText(this, "修改Netease公司中薪水小于等于13000人的名字", Toast.LENGTH_LONG).show();
        //注意不要插入两次数据，否则此处会报查询结果不唯一的Exception
        Company companyNetease = companyDao.queryBuilder().where(CompanyDao.Properties.CompanyName.eq("Netease")).unique();
        if (companyNetease != null) {

            List<Employee> employeeList = employeeDao.queryBuilder().where(EmployeeDao.Properties.CompanyId.eq(companyNetease.getId()),
                    EmployeeDao.Properties.Salary.le(13000)).list();
            if (employeeList != null) {
                for (Employee employee : employeeList) {
                    employee.setEmployeeName("baozi");
                    employeeDao.update(employee);
                }
            } else {
                Log.e("greendao_test", "update:updateList为空");
            }
            quaryAll();
        } else {
            Log.e("greendao_test", "update:company为空");
        }
    }

    /**
     * 查询Tencent公司中薪水大于等于10000的职员
     */
    private void quary() {
        Toast.makeText(this, "查询Tencent公司中薪水大于等于10000的职员", Toast.LENGTH_LONG).show();
        Company company = companyDao.queryBuilder().where(CompanyDao.Properties.CompanyName.eq("Tencent")).unique();
        List<Employee> employeeList = employeeDao.queryBuilder().where(EmployeeDao.Properties.CompanyId.eq(company.getId()),
                EmployeeDao.Properties.Salary.ge(10000)).orderDesc(EmployeeDao.Properties.Salary).list();
        if (employeeList != null) {
            tvText.setText(employeeList.toString());
        }

//        Company company = companyDao.queryBuilder().where(CompanyDao.Properties.CompanyName.eq("Tencent")).unique();
//        Query query = employeeDao.queryBuilder().where(EmployeeDao.Properties.CompanyId.eq(company.getId()),
//                EmployeeDao.Properties.Salary.ge(10000)).build();
//        //修改参数
//        query.setParameter(0, company.getId());
//        query.setParameter(0, 11000);
//        List<Employee> employeeList = query.list();
    }

    /**
     * 查
     * loadAll()：查询所有记录
     * load(Long key)：根据主键查询一条记录
     * queryBuilder().list()：返回：List
     * queryBuilder().where(UserDao.Properties.Name.eq("")).list()
     * queryRaw(String where,String selectionArg)：返回：List
     */
    private void quaryAll() {
        List<Company> companyList = companyDao.loadAll();
        List<Employee> employeeList = employeeDao.loadAll();
        if (companyList != null && employeeList != null)
            tvText.setText(companyList.toString() + "\n" + employeeList.toString());
        else {
            Log.e("greendao_test", "quary:quaryList为空");
        }
    }

}
