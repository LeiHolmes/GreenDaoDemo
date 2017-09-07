package com.holmeslei.greendaodemo.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;

import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

import com.holmeslei.greendaodemo.database.DaoSession;
import com.holmeslei.greendaodemo.database.EmployeeDao;
import com.holmeslei.greendaodemo.database.CompanyDao;

/**
 * Description:   公司实体类
 * author         xulei
 * Date           2017/7/10
 */
@Entity
public class Company {
    @Id(autoincrement = true) //自增
    private Long id; //主键
    private String companyName; //公司名称
    private String industry; //行业
    @ToMany(referencedJoinProperty = "companyId") //公司与雇员建立一对多关系，设置外键companyId
    private List<Employee> employeeList;
    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /**
     * Used for active entity operations.
     */
    @Generated(hash = 458770942)
    private transient CompanyDao myDao;

    @Generated(hash = 564257326)
    public Company(Long id, String companyName, String industry) {
        this.id = id;
        this.companyName = companyName;
        this.industry = industry;
    }

    @Generated(hash = 1096856789)
    public Company() {
    }

    @Override
    public String toString() {
        return "Company{" +
                "id=" + id +
                ", companyName='" + companyName + '\'' +
                ", industry='" + industry + '\'' +
                '}';
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompanyName() {
        return this.companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getIndustry() {
        return this.industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1108837057)
    public List<Employee> getEmployeeList() {
        if (employeeList == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            EmployeeDao targetDao = daoSession.getEmployeeDao();
            List<Employee> employeeListNew = targetDao
                    ._queryCompany_EmployeeList(id);
            synchronized (this) {
                if (employeeList == null) {
                    employeeList = employeeListNew;
                }
            }
        }
        return employeeList;
    }

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 738888862)
    public synchronized void resetEmployeeList() {
        employeeList = null;
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 1533027800)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getCompanyDao() : null;
    }
}
