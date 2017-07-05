package com.holmeslei.greendaodemo.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Description:   点
 * author         xulei
 * Date           2017/7/5
 */
@Entity
public class NotePoint {
    @Id(autoincrement = true)
    private Long id;
    private long strokeId;
    private float pX;
    private float pY;
    private float testTime;
    private float firstPress;
    private float press;
    private int pageIndex;
    private int pointType; //down:1 move:2 up:3

    @Generated(hash = 490379115)
    public NotePoint(Long id, long strokeId, float pX, float pY, float testTime,
                     float firstPress, float press, int pageIndex, int pointType) {
        this.id = id;
        this.strokeId = strokeId;
        this.pX = pX;
        this.pY = pY;
        this.testTime = testTime;
        this.firstPress = firstPress;
        this.press = press;
        this.pageIndex = pageIndex;
        this.pointType = pointType;
    }

    @Generated(hash = 1614831008)
    public NotePoint() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getStrokeId() {
        return this.strokeId;
    }

    public void setStrokeId(long strokeId) {
        this.strokeId = strokeId;
    }

    public float getPX() {
        return this.pX;
    }

    public void setPX(float pX) {
        this.pX = pX;
    }

    public float getPY() {
        return this.pY;
    }

    public void setPY(float pY) {
        this.pY = pY;
    }

    public float getTestTime() {
        return this.testTime;
    }

    public void setTestTime(float testTime) {
        this.testTime = testTime;
    }

    public float getFirstPress() {
        return this.firstPress;
    }

    public void setFirstPress(float firstPress) {
        this.firstPress = firstPress;
    }

    public float getPress() {
        return this.press;
    }

    public void setPress(float press) {
        this.press = press;
    }

    public int getPageIndex() {
        return this.pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPointType() {
        return this.pointType;
    }

    public void setPointType(int pointType) {
        this.pointType = pointType;
    }

    @Override
    public String toString() {
        return "NotePoint{" +
                "id=" + id +
                ", strokeId=" + strokeId +
                ", x坐标=" + pX +
                ", y坐标=" + pY +
                ", 时间=" + testTime +
                ", 初始压力=" + firstPress +
                ", 实际压力=" + press +
                ", 页码=" + pageIndex +
                ", 点类型=" + pointType +
                '}' + "\n";
    }
}
