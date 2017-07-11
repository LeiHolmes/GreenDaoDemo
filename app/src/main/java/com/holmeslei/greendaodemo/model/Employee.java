package com.holmeslei.greendaodemo.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Description:   雇员实体类
 * author         xulei
 * Date           2017/7/10
 */
@Entity
public class Employee {
    @Id(autoincrement = true)
    private Long id;
    private long companyId; //指向Company主键
    private String employeeName;
    private int salary;

    @Generated(hash = 1405154464)
    public Employee(Long id, long companyId, String employeeName, int salary) {
        this.id = id;
        this.companyId = companyId;
        this.employeeName = employeeName;
        this.salary = salary;
    }

    @Generated(hash = 202356944)
    public Employee() {
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", companyId=" + companyId +
                ", employeeName='" + employeeName + '\'' +
                ", salary=" + salary +
                '}' + "\n";
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getCompanyId() {
        return this.companyId;
    }

    public void setCompanyId(long companyId) {
        this.companyId = companyId;
    }

    public String getEmployeeName() {
        return this.employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public int getSalary() {
        return this.salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }
}
