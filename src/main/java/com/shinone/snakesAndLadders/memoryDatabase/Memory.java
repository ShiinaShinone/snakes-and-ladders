package com.shinone.snakesAndLadders.memoryDatabase;

import com.shinone.snakesAndLadders.entity.Log;
import com.shinone.snakesAndLadders.entity.Player;
import com.shinone.snakesAndLadders.entity.Room;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 杨泽南
 * @version 1.0.0
 * CreateDate 2022/11/14 14:45
 * description.内存方式保存数据
 */
public class Memory {
    /**
     * id - 用户对象
     */
    public static final Map<String, Player> PLAYER = new HashMap<>();
    /**
     * id - 房间对象
     */
    public static final Map<String, Room> ROOM = new HashMap<>();
    /**
     * 玩家id - 房间
     */
    public static final Map<String, Room> PLAYER_ROOM = new HashMap<>();
    /**
     * 房间id - 日志列表
     */
    public static final Map<String, List<Log>> ROOM_LOG = new HashMap<>();
}
