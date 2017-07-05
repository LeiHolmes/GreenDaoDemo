package com.holmeslei.greendaodemo.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import com.holmeslei.greendaodemo.database.DaoSession;
import com.holmeslei.greendaodemo.database.NoteStrokeDao;
import com.holmeslei.greendaodemo.database.NotePageDao;

/**
 * Description:   页
 * author         xulei
 * Date           2017/7/5
 */
@Entity
public class NotePage {
    @Id(autoincrement = true)
    private Long id;
    private int pageIndex;
    private long bookId;
    @ToMany(referencedJoinProperty = "pageId") //页线一对多
    private List<NoteStroke> strokeList;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 1639320209)
    private transient NotePageDao myDao;
    @Generated(hash = 852888909)
    public NotePage(Long id, int pageIndex, long bookId) {
        this.id = id;
        this.pageIndex = pageIndex;
        this.bookId = bookId;
    }
    @Generated(hash = 1457843902)
    public NotePage() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public int getPageIndex() {
        return this.pageIndex;
    }
    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }
    public long getBookId() {
        return this.bookId;
    }
    public void setBookId(long bookId) {
        this.bookId = bookId;
    }
    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1497590420)
    public List<NoteStroke> getStrokeList() {
        if (strokeList == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            NoteStrokeDao targetDao = daoSession.getNoteStrokeDao();
            List<NoteStroke> strokeListNew = targetDao
                    ._queryNotePage_StrokeList(id);
            synchronized (this) {
                if (strokeList == null) {
                    strokeList = strokeListNew;
                }
            }
        }
        return strokeList;
    }
    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 531561772)
    public synchronized void resetStrokeList() {
        strokeList = null;
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
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 165825040)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getNotePageDao() : null;
    }
}
