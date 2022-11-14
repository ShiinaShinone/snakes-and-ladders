package com.shinone.snakesAndLadders.controller;

import com.shinone.snakesAndLadders.entity.Log;
import com.shinone.snakesAndLadders.entity.Player;
import com.shinone.snakesAndLadders.entity.Room;
import com.shinone.snakesAndLadders.enums.RoomStateEnum;
import com.shinone.snakesAndLadders.memoryDatabase.Memory;
import com.shinone.snakesAndLadders.message.ResultMessage;
import com.shinone.snakesAndLadders.vo.LogOutVo;
import com.shinone.snakesAndLadders.vo.NewRoomOutVo;
import com.shinone.snakesAndLadders.vo.RoomOutVo;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @author 杨泽南
 * @version 1.0.0
 * CreateDate 2022/11/14 15:03
 * description.
 */
@RestController()
@RequestMapping("/room")
public class RoomController {

    @PostMapping("/newRoom")
    @ResponseBody
    public ResultMessage<NewRoomOutVo> newRoom() {
        String userId = UUID.randomUUID().toString();
        String roomId = UUID.randomUUID().toString();
        Room room = new Room();
        room.setId(roomId);
        room.setState(0);
        room.setNextIndex(0);

        HashMap<String, Integer> map = new HashMap<>(8);
        map.put(userId, 0);
        room.setCurrent(map);

        Player player = new Player();
        player.setId(userId);
        player.setName("玩家");
        List<Player> players = new ArrayList<>();
        players.add(player);
        room.setPlayers(players);

        Memory.ROOM.put(roomId, room);
        Memory.PLAYER.put(userId, player);
        Memory.ROOM_LOG.put(roomId, new ArrayList<>());
        Memory.PLAYER_ROOM.put(userId, room);

        NewRoomOutVo outVo = new NewRoomOutVo();
        outVo.setPlayerId(userId);
        outVo.setRoom(room);
        return ResultMessage.success(outVo);
    }

    @PostMapping("/joinRoom/{roomId}")
    @ResponseBody
    public ResultMessage<NewRoomOutVo> joinRoom(@PathVariable("roomId") String roomId) {
        String userId = UUID.randomUUID().toString();
        if (!Memory.ROOM.containsKey(roomId)) {
            return ResultMessage.error("不存在当前房间");
        }
        Room room = Memory.ROOM.get(roomId);
        if (room.getState().equals(RoomStateEnum.BEGIN.getState())) {
            return ResultMessage.error("当前房间已开始游戏无法加入");
        }
        Player player = new Player();
        player.setId(userId);
        player.setName("玩家");
        room.getCurrent().put(player.getId(), 0);
        room.getPlayers().add(player);
        Memory.PLAYER.put(userId, player);
        Memory.PLAYER_ROOM.put(userId, room);
        NewRoomOutVo outVo = new NewRoomOutVo();
        outVo.setRoom(room);
        outVo.setPlayerId(userId);
        return ResultMessage.success(outVo);
    }
    @PostMapping("/roll/{userId}")
    @ResponseBody
    /**
     * 核心方法，后端roll一个点数之后直接更新到当前的房间状态中，并将点数返回给前端
     */
    public ResultMessage<RoomOutVo> roll(@PathVariable("userId") String userId) {
        Room room = Memory.PLAYER_ROOM.get(userId);
        if (room == null) {
            return ResultMessage.error("当前用户不在对局中");
        }
        if (room.getState().equals(RoomStateEnum.END.getState())) {
            return ResultMessage.error("当前对局已结束,获胜玩家为" + room.getPlayers().get(room.getNextIndex()));
        }
        if (!room.getPlayers().get(room.getNextIndex()).getId().equals(userId)) {
            return ResultMessage.error("不是您的回合，无法投骰子");
        }
        room.setState(RoomStateEnum.BEGIN.getState());

        Integer oldIndex = room.getCurrent().get(userId);
        Random random = new Random();
        // 投骰子
        int dice = random.nextInt(5) + 1;
        // 有用户到达100，设置房间为结束状态，该用户胜利
        if (oldIndex + dice == 100) {
            room.setState(RoomStateEnum.END.getState());
            room.getCurrent().put(userId, 100);
            // 记录日志
            this.log(userId, room.getId(), oldIndex, 100, dice);
            RoomOutVo outVo = new RoomOutVo();
            outVo.setState(RoomStateEnum.END.getState());
            outVo.setPlayerId(userId);
            outVo.setRoom(room);
            return ResultMessage.success(outVo);
        }
        // 超过100回退格子
        Integer newIndex = oldIndex + dice > 100 ? (200 - oldIndex - dice) : oldIndex + dice;
        // 下一个投骰子的用户下标
        room.setNextIndex((room.getNextIndex() + 1) % room.getPlayers().size());
        // 更新当前用户的位置状态
        room.getCurrent().put(userId, newIndex);
        // 记录日志
        this.log(userId, room.getId(), oldIndex, newIndex, dice);
        // 返回当前棋盘状态
        RoomOutVo outVo = new RoomOutVo();
        outVo.setState(RoomStateEnum.BEGIN.getState());
        outVo.setPlayerId(userId);
        outVo.setRoom(room);
        outVo.setDice(dice);
        return ResultMessage.success(outVo);
    }

    private void log(String uId, String rId, int oldIndex, int newIndex, int dice) {
        Log log = new Log();
        log.setId(UUID.randomUUID().toString());
        log.setDice(dice);
        log.setPlayerId(uId);
        log.setRoomId(rId);
        log.setOldIndex(oldIndex);
        log.setNewIndex(newIndex);
        Memory.ROOM_LOG.get(rId).add(log);
    }

    @PostMapping("/log/{roomId}")
    @ResponseBody
    public ResultMessage<LogOutVo> log(@PathVariable("roomId") String roomId) {
        LogOutVo logOutVo = new LogOutVo();
        logOutVo.setLogList(Memory.ROOM_LOG.get(roomId));
        return ResultMessage.success(logOutVo);
    }
}
