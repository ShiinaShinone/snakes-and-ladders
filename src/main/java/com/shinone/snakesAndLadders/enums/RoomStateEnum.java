package com.shinone.snakesAndLadders.enums;

/**
 * @author 杨泽南
 * @version 1.0.0
 * CreateDate 2022/11/14 15:34
 * description.
 */
public enum RoomStateEnum {
    WAIT(0, "未开始"),
    BEGIN(1, "已开始"),
    END(2, "结束");

    private int state;
    private String dec;
    RoomStateEnum(int state, String dec) {
        this.state = state;
        this.dec = dec;
    }
    public int getState() {
        return this.state;
    }
}
