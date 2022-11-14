package com.shinone.snakesAndLadders.vo;

import com.shinone.snakesAndLadders.entity.Room;
import lombok.Data;

/**
 * @author 杨泽南
 * @version 1.0.0
 * CreateDate 2022/11/14 15:12
 * description.
 */
@Data
public class RoomOutVo {
    private Room room;
    private String playerId;
    private Integer dice;
    private Integer state;
}
