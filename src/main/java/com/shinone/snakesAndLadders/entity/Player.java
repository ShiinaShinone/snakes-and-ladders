package com.shinone.snakesAndLadders.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 杨泽南
 * @version 1.0.0
 * CreateDate 2022/11/14 14:41
 * description.玩家
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Player {
    private String id;
    private String name;
}
