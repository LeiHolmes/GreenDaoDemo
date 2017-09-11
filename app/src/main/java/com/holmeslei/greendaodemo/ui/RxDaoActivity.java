package com.holmeslei.greendaodemo.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.rx.RxDao;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Description:   GreenDao3.0+RxJava1.0
 * 目前GreenDao还未适配到RxJava2.0
 * author         xulei
 * Date           2017/9/7 16:56
 */
public class RxDaoActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tvText;
    private RxDao<Company, Long> companyDao;
    private RxDao<Employee, Long> employeeDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_dao);
        initView();
        initData();
        initListener();
    }

    private void initData() {
        companyDao = GreenDaoUtil.getCompanyDao().rx();
        employeeDao = GreenDaoUtil.getEmployeeDao().rx();
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
        companyDao.insert(companyNetease)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Company>() {
                    @Override
                    public void call(Company company) {
                        Log.e("test_gd_rx", "insert：Netease插入完毕");
                    }
                });
        companyDao.insert(companyTencent)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Company>() {
                    @Override
                    public void call(Company company) {
                        Log.e("test_gd_rx", "insert：Tencent插入完毕");
                    }
                });

        //插入不同公司的雇员
        for (int i = 0; i < 5; i++) {
            Employee employee = new Employee(null, companyNetease.getId(), "Sherlock" + i, 11000 + i * 1000);
            employeeDao.insert(employee);
        }
        for (int i = 0; i < 5; i++) {
            Employee employee = new Employee(null, companyTencent.getId(), "Richard" + i, 8000 + i * 1000);
            employeeDao.insert(employee);
        }
        //查询所有数据并更新UI
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
        GreenDaoUtil.getCompanyQuery().where(CompanyDao.Properties.CompanyName.eq("Tencent")).rx().unique()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Company>() {
                    @Override
                    public void call(Company company) {
                        if (company != null) {
                            GreenDaoUtil.getEmployeeQuery().where(EmployeeDao.Properties.CompanyId.eq(company.getId()),
                                    EmployeeDao.Properties.Salary.lt(10000)).rx().list()
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .flatMap(new Func1<List<Employee>, Observable<Employee>>() {
                                        @Override
                                        public Observable<Employee> call(List<Employee> employees) {
                                            return Observable.from(employees);
                                        }
                                    })
                                    .subscribe(new Action1<Employee>() {
                                        @Override
                                        public void call(Employee employee) {
                                            employeeDao.deleteByKey(employee.getId()).subscribeOn(Schedulers.io());
                                        }
                                    });
                        }
                    }
                });
    }

    /**
     * 清空数据库数据
     */
    private void deleteAll() {
        companyDao.deleteAll()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Log.e("test_gd_rx", "deleteAll：Company删除完毕");
                    }
                });
        employeeDao.deleteAll()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Log.e("test_gd_rx", "deleteAll：Employee删除完毕");
                    }
                });
        quaryAll();
    }

    /**
     * 修改Netease公司中薪水小于等于13000人的名字
     */
    private void update() {
        Toast.makeText(this, "修改Netease公司中薪水小于等于13000人的名字", Toast.LENGTH_LONG).show();
        GreenDaoUtil.getCompanyQuery().where(CompanyDao.Properties.CompanyName.eq("Netease")).rx().unique()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Company>() {
                    @Override
                    public void call(Company company) {
                        if (company != null) {
                            GreenDaoUtil.getEmployeeQuery().where(EmployeeDao.Properties.CompanyId.eq(company.getId()),
                                    EmployeeDao.Properties.Salary.le(13000)).rx().list()
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .flatMap(new Func1<List<Employee>, Observable<Employee>>() {
                                        @Override
                                        public Observable<Employee> call(List<Employee> employees) {
                                            return Observable.from(employees);
                                        }
                                    })
                                    .subscribe(new Action1<Employee>() {
                                        @Override
                                        public void call(Employee employee) {
                                            employee.setEmployeeName("baozi");
                                            employeeDao.update(employee).subscribeOn(Schedulers.io());
                                        }
                                    });
                        }
                    }
                });
    }

    /**
     * 查询Tencent公司中薪水大于等于10000的职员
     */
    private void quary() {
        Toast.makeText(this, "查询Tencent公司中薪水大于等于10000的职员", Toast.LENGTH_LONG).show();
        GreenDaoUtil.getCompanyQuery().where(CompanyDao.Properties.CompanyName.eq("Tencent")).rx().unique()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Company>() {
                    @Override
                    public void call(Company company) {
                        GreenDaoUtil.getEmployeeQuery().where(EmployeeDao.Properties.CompanyId.eq(company.getId()),
                                EmployeeDao.Properties.Salary.ge(10000)).orderDesc(EmployeeDao.Properties.Salary).rx().list()
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Action1<List<Employee>>() {
                                    @Override
                                    public void call(List<Employee> employees) {
                                        if (!employees.isEmpty()) {
                                            tvText.setText(employees.toString());
                                        }
                                    }
                                });
                    }
                });
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
        final StringBuilder sb = new StringBuilder();
        companyDao.loadAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Company>>() {
                    @Override
                    public void call(List<Company> companies) {
                        if (!companies.isEmpty()) {
                            sb.append(companies.toString());
                            tvText.setText(sb + "\n");
                        } else Log.e("test_gd_rx", "quary:companies为空");
                    }
                });
        employeeDao.loadAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Employee>>() {
                    @Override
                    public void call(List<Employee> employees) {
                        if (!employees.isEmpty()) {
                            sb.append(employees.toString());
                            tvText.setText(sb);
                        } else Log.e("test_gd_rx", "quary:employees为空");
                    }
                });
    }
}
