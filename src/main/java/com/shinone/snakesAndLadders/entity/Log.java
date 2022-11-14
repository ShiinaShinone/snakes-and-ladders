package com.shinone.snakesAndLadders.entity;

import lombok.Data;

/**
 * @author 杨泽南
 * @version 1.0.0
 * CreateDate 2022/11/14 14:59
 * description.操作日志
 */
@Data
public class Log {
    private String id;
    private String playerId;
    private String roomId;
    /**
     * 骰子点数
     */
    private int dice;
    /**
     * 原本位置
     */
    private int oldIndex;
    /**
     * 新的位置
     */
    private int newIndex;
}
