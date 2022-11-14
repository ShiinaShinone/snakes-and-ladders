package com.shinone.snakesAndLadders.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @author 杨泽南
 * @version 1.0.0
 * CreateDate 2022/11/14 14:47
 * description.房间
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Room {
    /**
     * id
     */
    private String id;
    /**
     * 房间内玩家列表
     */
    private List<Player> players;
    /**
     * 玩家id对应的当前位置
     */
    private Map<String, Integer> current;

    /**
     * 0未开始，1开始
     */
    private Integer state;

    /**
     * 下一个行动的玩家index
     */
    private Integer nextIndex;
}
