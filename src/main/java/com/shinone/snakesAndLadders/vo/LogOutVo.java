package com.shinone.snakesAndLadders.vo;

import com.shinone.snakesAndLadders.entity.Log;
import lombok.Data;

import java.util.List;

/**
 * @author 杨泽南
 * @version 1.0.0
 * CreateDate 2022/11/14 15:12
 * description.
 */
@Data
public class LogOutVo {
    private List<Log> logList;
}
