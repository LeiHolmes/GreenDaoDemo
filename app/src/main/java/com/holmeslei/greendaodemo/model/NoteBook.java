package com.holmeslei.greendaodemo.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;

import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

import com.holmeslei.greendaodemo.database.DaoSession;
import com.holmeslei.greendaodemo.database.NotePageDao;
import com.holmeslei.greendaodemo.database.NoteBookDao;

/**
 * Description:   本
 * author         xulei
 * Date           2017/7/5
 */
@Entity
public class NoteBook {
    @Id(autoincrement = true)
    private Long id;
    private String bookName;
    @ToMany(referencedJoinProperty = "bookId") //本页一对多，NotePage类的bookId作为外键，与NoteBook的主键相连。
    private List<NotePage> pageList;
    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /**
     * Used for active entity operations.
     */
    @Generated(hash = 1692630944)
    private transient NoteBookDao myDao;

    @Generated(hash = 372042495)
    public NoteBook(Long id, String bookName) {
        this.id = id;
        this.bookName = bookName;
    }

    @Generated(hash = 2066935268)
    public NoteBook() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBookName() {
        return this.bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1213247421)
    public List<NotePage> getPageList() {
        if (pageList == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            NotePageDao targetDao = daoSession.getNotePageDao();
            List<NotePage> pageListNew = targetDao._queryNoteBook_PageList(id);
            synchronized (this) {
                if (pageList == null) {
                    pageList = pageListNew;
                }
            }
        }
        return pageList;
    }

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 852283546)
    public synchronized void resetPageList() {
        pageList = null;
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
    @Generated(hash = 1888691330)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getNoteBookDao() : null;
    }
}
