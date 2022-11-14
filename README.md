# snakes-and-ladders
蛇梯游戏简化版

1、直接使用的RESTfull风格的http请求协议，所以完全没有考虑局内不同用户的操作同步显示给其他用户，只实现了题目要求的基本对局算法功能。
请求体和返回体都是json对象

2、因为暂时不考虑蛇和梯子，所以没必要在后端维护棋盘上每个位置具体是蛇头还是蛇尾的数组，每个用户的棋子都只保存从0到100的当前位置的值index。
前端可以用 i = (9 - (index / 10))  j = (i % 2 != 0) ? (index % 10) - 1 : 10 - (index % 10)
来得到当前下标对应的列表下标int[i][j]，如index = 14时为int[8][6],68时为 int[3][7]

springboot项目，默认端口13080
接口文档:
# 通用响应
| 字段名 | 类型 | 描述 |
| --- | --- | --- |
| code | int | 调用成功与否，0成功 1失败 |
| msg | string | 接口调用情况描述 |
| data | string | Json对象字符串 |

# 1.创建房间
创建一个新的游戏房间，并将房间信息和随机生成的用户id返回给前端，设定上，调用该接口的人会创建一个新的房间，并默认进入操作队列的第一位，房间状态为等待，当第一个用户调用投骰子的接口之后房间状态为进行中，这时无法再加入房间
### POST方法
/room/newRoom
### query参数:
| 字段名 | 名称 | 必填 |
| --- | --- | --- |
|  |  |  |
|  |  |  |

### 响应
| 字段名称 | 字段描述 | 字段类型 | 备注 |
| --- | --- | --- | --- |
| playerId | 创建该房间的当前用户id | string | <br /> |
| room | 房间对象 | <br /> | <br /> |
| ./id | 房间id | <br /> | <br /> |
| ./players | 玩家列表 | player对象列表 | <br /> |
| ././id | 用户id | string |  |
| ././name | 用户名称 | string | 没有实现 统一为“玩家” |
| ./current | 用户id--当前位置 的map | map |  |
| ./state | 房间状态 | int | 0未开始 1 进行中 2 已结束 |
| ./nextIndex | 下一个行动的玩家 | int | player对象列表的下标，默认为0 |

示例：
```json
{
  "data": {
    "room": {
      "id": "d3b04d7f-af60-40ec-a330-da39c983f648",
      "players": [
        {
          "id": "513498bf-e8f4-48b5-8a8b-9af543748f1a",
          "name": "玩家"
        }
          ],
          "current": {
          "513498bf-e8f4-48b5-8a8b-9af543748f1a": 0
        },
          "state": 0,
          "nextIndex": 0
        },
          "playerId": "513498bf-e8f4-48b5-8a8b-9af543748f1a"
        },
          "msg": "操作成功",
          "code": 0
        }
```
# 2.加入房间
根据房间id加入一个已有的房间，返回随机生成的用户id和房间信息
### POST方法
room/joinRoom/{roomId}
### qo参数
| 字段名 | 名称 | 必填 |
| --- | --- | --- |
| roomId | 房间id | Y |

### 响应
| 字段名称 | 字段描述 | 字段类型 | 备注 |
| --- | --- | --- | --- |
| playerId | 当前用户id | string | <br /> |
| room | 房间对象 | <br /> | <br /> |
| ./id | 房间id | <br /> | <br /> |
| ./players | 玩家列表 | player对象列表 | <br /> |
| ././id | 用户id | string |  |
| ././name | 用户名称 | string | 没有实现 统一为“玩家” |
| ./current | 用户id--当前位置 的map | map |  |
| ./state | 房间状态 | int | 0未开始 1 进行中 2 已结束 |
| ./nextIndex | 下一个行动的玩家 | int | player对象列表的下标，默认为0 |

示例：
```json
{
    "data": {
        "room": {
            "id": "d3b04d7f-af60-40ec-a330-da39c983f648",
            "players": [
                {
                    "id": "513498bf-e8f4-48b5-8a8b-9af543748f1a",
                    "name": "玩家"
                },
                {
                    "id": "9e3a1c0d-6b71-4bcc-b963-ecf85a8121da",
                    "name": "玩家"
                },
                {
                    "id": "1167e6fc-4d4b-47f5-8d93-018d77d8eefd",
                    "name": "玩家"
                }
            ],
            "current": {
                "9e3a1c0d-6b71-4bcc-b963-ecf85a8121da": 0,
                "1167e6fc-4d4b-47f5-8d93-018d77d8eefd": 0,
                "513498bf-e8f4-48b5-8a8b-9af543748f1a": 0
            },
            "state": 0,
            "nextIndex": 0
        },
        "playerId": "1167e6fc-4d4b-47f5-8d93-018d77d8eefd"
    },
    "msg": "操作成功",
    "code": 0
}
```
# 3.投骰子
默认从用户列表第一个开始轮流投骰子，返回骰子点数、房间信息，如果没有加入房间、不在自己的轮次、该房间游戏已结束时无法投骰子<br />返回的state表示当前房间状态，如果为2表示游戏结束，最后一个投骰子的玩家获胜
### 方法名
room/roll/{userId}
### qo参数
| 字段名 | 名称 | 必填 |
| --- | --- | --- |
| userId | 用户id | Y |

### 响应
| 字段名称 | 字段描述 | 字段类型 | 备注 |
| --- | --- | --- | --- |
| playerId | 当前用户id | string | <br /> |
| state | 房间状态 | int | 为1，继续游戏<br />为2，游戏结束当前用户获胜 |
| dice | 这一次投的骰子点数 | int |  |
| room | 房间对象 | <br /> | <br /> |
| ./id | 房间id | <br /> | <br /> |
| ./players | 玩家列表 | player对象列表 | <br /> |
| ././id | 用户id | string |  |
| ././name | 用户名称 | string | 没有实现 统一为“玩家” |
| ./current | 用户id--当前位置 的map | map |  |
| ./state | 房间状态 | int | 0未开始 1 进行中 2 已结束 |
| ./nextIndex | 下一个行动的玩家 | int | player对象列表的下标，默认为0 |

示例：
```json
eg1:
{
    "data": null,
    "msg": "不是您的回合，无法投骰子",
    "code": -1
}

eg2:
{
    "data": {
        "room": {
            "id": "d3b04d7f-af60-40ec-a330-da39c983f648",
            "players": [
                {
                    "id": "513498bf-e8f4-48b5-8a8b-9af543748f1a",
                    "name": "玩家"
                },
                {
                    "id": "9e3a1c0d-6b71-4bcc-b963-ecf85a8121da",
                    "name": "玩家"
                },
                {
                    "id": "1167e6fc-4d4b-47f5-8d93-018d77d8eefd",
                    "name": "玩家"
                }
            ],
            "current": {
                "9e3a1c0d-6b71-4bcc-b963-ecf85a8121da": 5,
                "1167e6fc-4d4b-47f5-8d93-018d77d8eefd": 1,
                "513498bf-e8f4-48b5-8a8b-9af543748f1a": 3
            },
            "state": 1,
            "nextIndex": 0
        },
        "playerId": "1167e6fc-4d4b-47f5-8d93-018d77d8eefd",
        "dice": 1,
        "state": 1
    },
    "msg": "操作成功",
    "code": 0
}
eg3:
{
    "data": null,
    "msg": "当前对局已结束,获胜玩家为Player(id=71601f80-f37f-4a3a-a430-ae2c2017b3d7, name=玩家)",
    "code": -1
}
```
# 4.日志查询
查看某个房间的历史记录
### 方法名
room/log/{roomId}
### query参数:
| 字段名 | 名称 | 必填 |
| --- | --- | --- |
| roomId | 房间id | Y |

### 响应
| 字段名称 | 字段描述 | 字段类型 | 备注 |
| --- | --- | --- | --- |
| logList | 日志列表 | <br /> | <br /> |
| ./id | id | string | <br /> |
| ./playerId | 玩家id | string |  |
| ./roomId | 房间id | string |  |
| ./dice | 这次投掷的点数 | int |  |
| ./oldIndex | 原本位置 | int |  |
| ./newIndex | 新的位置 | int |  |
|  |  |  |  |

示例：
```json
{
    "data": {
        "logList": [
            {
                "id": "16f6c697-7f9f-43a2-a2d8-bd72dc5c7665",
                "playerId": "b8e3e424-c337-465c-bb82-0fa38c31e3fb",
                "roomId": "92cad0df-94e4-4a48-adb8-cde6c31bd233",
                "dice": 3,
                "oldIndex": 0,
                "newIndex": 3
            },
            {
                "id": "f652f7d0-5663-4af8-90d6-fdd5c82fc82a",
                "playerId": "b8e3e424-c337-465c-bb82-0fa38c31e3fb",
                "roomId": "92cad0df-94e4-4a48-adb8-cde6c31bd233",
                "dice": 5,
                "oldIndex": 3,
                "newIndex": 8
            },
            {
                "id": "4dbd44ab-ccb8-4a67-bd0d-5d0ea6c08c2f",
                "playerId": "b8e3e424-c337-465c-bb82-0fa38c31e3fb",
                "roomId": "92cad0df-94e4-4a48-adb8-cde6c31bd233",
                "dice": 4,
                "oldIndex": 8,
                "newIndex": 12
            },
            {
                "id": "482fb124-96a3-4477-98e6-d1ac594e1d3b",
                "playerId": "b8e3e424-c337-465c-bb82-0fa38c31e3fb",
                "roomId": "92cad0df-94e4-4a48-adb8-cde6c31bd233",
                "dice": 2,
                "oldIndex": 12,
                "newIndex": 14
            },
            {
                "id": "f5dd4392-d644-4b87-92cf-ef996c8d6655",
                "playerId": "b8e3e424-c337-465c-bb82-0fa38c31e3fb",
                "roomId": "92cad0df-94e4-4a48-adb8-cde6c31bd233",
                "dice": 4,
                "oldIndex": 14,
                "newIndex": 18
            },
            {
                "id": "9de4cd4d-51ac-4b00-ac5a-3ff94484b10e",
                "playerId": "b8e3e424-c337-465c-bb82-0fa38c31e3fb",
                "roomId": "92cad0df-94e4-4a48-adb8-cde6c31bd233",
                "dice": 4,
                "oldIndex": 18,
                "newIndex": 22
            },
            {
                "id": "f8378795-f36e-4303-b20a-0ffdf5340df7",
                "playerId": "b8e3e424-c337-465c-bb82-0fa38c31e3fb",
                "roomId": "92cad0df-94e4-4a48-adb8-cde6c31bd233",
                "dice": 4,
                "oldIndex": 22,
                "newIndex": 26
            },
            {
                "id": "329a019c-7629-4878-b8aa-68cb9627914f",
                "playerId": "b8e3e424-c337-465c-bb82-0fa38c31e3fb",
                "roomId": "92cad0df-94e4-4a48-adb8-cde6c31bd233",
                "dice": 3,
                "oldIndex": 26,
                "newIndex": 29
            },
            {
                "id": "cbecf59a-9bbe-42cc-8869-a870fc2597fc",
                "playerId": "b8e3e424-c337-465c-bb82-0fa38c31e3fb",
                "roomId": "92cad0df-94e4-4a48-adb8-cde6c31bd233",
                "dice": 2,
                "oldIndex": 29,
                "newIndex": 31
            },
            {
                "id": "13fba3ed-9d72-4d55-a04e-f27e04e213d9",
                "playerId": "b8e3e424-c337-465c-bb82-0fa38c31e3fb",
                "roomId": "92cad0df-94e4-4a48-adb8-cde6c31bd233",
                "dice": 3,
                "oldIndex": 31,
                "newIndex": 34
            },
            {
                "id": "0c365633-66ea-4619-96b6-ab3d8d6a1674",
                "playerId": "b8e3e424-c337-465c-bb82-0fa38c31e3fb",
                "roomId": "92cad0df-94e4-4a48-adb8-cde6c31bd233",
                "dice": 1,
                "oldIndex": 34,
                "newIndex": 35
            },
            {
                "id": "393d2ea1-1c63-478c-b2e7-92105d66ad83",
                "playerId": "b8e3e424-c337-465c-bb82-0fa38c31e3fb",
                "roomId": "92cad0df-94e4-4a48-adb8-cde6c31bd233",
                "dice": 2,
                "oldIndex": 35,
                "newIndex": 37
            },
            {
                "id": "b384f59b-0606-4f1c-a1cf-3fcf0dec0b20",
                "playerId": "b8e3e424-c337-465c-bb82-0fa38c31e3fb",
                "roomId": "92cad0df-94e4-4a48-adb8-cde6c31bd233",
                "dice": 5,
                "oldIndex": 37,
                "newIndex": 42
            },
            {
                "id": "17558bc9-c03d-4589-9fd9-b15774ca4049",
                "playerId": "b8e3e424-c337-465c-bb82-0fa38c31e3fb",
                "roomId": "92cad0df-94e4-4a48-adb8-cde6c31bd233",
                "dice": 5,
                "oldIndex": 42,
                "newIndex": 47
            },
            {
                "id": "7c530242-4a3a-4b28-bbb6-14816790e743",
                "playerId": "b8e3e424-c337-465c-bb82-0fa38c31e3fb",
                "roomId": "92cad0df-94e4-4a48-adb8-cde6c31bd233",
                "dice": 2,
                "oldIndex": 47,
                "newIndex": 49
            },
            {
                "id": "a5f1a816-a680-4814-a3b3-dc1b07953e9d",
                "playerId": "b8e3e424-c337-465c-bb82-0fa38c31e3fb",
                "roomId": "92cad0df-94e4-4a48-adb8-cde6c31bd233",
                "dice": 3,
                "oldIndex": 49,
                "newIndex": 52
            },
            {
                "id": "07336655-9316-444b-a63b-87ae7a2b8442",
                "playerId": "b8e3e424-c337-465c-bb82-0fa38c31e3fb",
                "roomId": "92cad0df-94e4-4a48-adb8-cde6c31bd233",
                "dice": 2,
                "oldIndex": 52,
                "newIndex": 54
            },
            {
                "id": "93b7e2c3-dd49-4a66-b0a3-5947b234b314",
                "playerId": "b8e3e424-c337-465c-bb82-0fa38c31e3fb",
                "roomId": "92cad0df-94e4-4a48-adb8-cde6c31bd233",
                "dice": 4,
                "oldIndex": 54,
                "newIndex": 58
            },
            {
                "id": "cddee873-06b5-4cc1-b35e-766628cf63bf",
                "playerId": "b8e3e424-c337-465c-bb82-0fa38c31e3fb",
                "roomId": "92cad0df-94e4-4a48-adb8-cde6c31bd233",
                "dice": 4,
                "oldIndex": 58,
                "newIndex": 62
            },
            {
                "id": "858dd802-d581-44b3-b1c5-252e340be63e",
                "playerId": "b8e3e424-c337-465c-bb82-0fa38c31e3fb",
                "roomId": "92cad0df-94e4-4a48-adb8-cde6c31bd233",
                "dice": 3,
                "oldIndex": 62,
                "newIndex": 65
            },
            {
                "id": "0022e2c4-cb19-4067-97fb-9eab400de8ec",
                "playerId": "b8e3e424-c337-465c-bb82-0fa38c31e3fb",
                "roomId": "92cad0df-94e4-4a48-adb8-cde6c31bd233",
                "dice": 1,
                "oldIndex": 65,
                "newIndex": 66
            },
            {
                "id": "ede13b92-0205-4074-9788-61269dc21120",
                "playerId": "b8e3e424-c337-465c-bb82-0fa38c31e3fb",
                "roomId": "92cad0df-94e4-4a48-adb8-cde6c31bd233",
                "dice": 5,
                "oldIndex": 66,
                "newIndex": 71
            },
            {
                "id": "7d7e8028-6a2a-4967-85a5-4fed179abc57",
                "playerId": "b8e3e424-c337-465c-bb82-0fa38c31e3fb",
                "roomId": "92cad0df-94e4-4a48-adb8-cde6c31bd233",
                "dice": 1,
                "oldIndex": 71,
                "newIndex": 72
            },
            {
                "id": "8908b699-e5c8-4626-8802-d571e45a228e",
                "playerId": "b8e3e424-c337-465c-bb82-0fa38c31e3fb",
                "roomId": "92cad0df-94e4-4a48-adb8-cde6c31bd233",
                "dice": 5,
                "oldIndex": 72,
                "newIndex": 77
            },
            {
                "id": "02081291-f41a-4d4c-9f0d-0ab8d1f8a737",
                "playerId": "b8e3e424-c337-465c-bb82-0fa38c31e3fb",
                "roomId": "92cad0df-94e4-4a48-adb8-cde6c31bd233",
                "dice": 1,
                "oldIndex": 77,
                "newIndex": 78
            },
            {
                "id": "04ec29af-b2a6-4dec-8257-a4415a7b8c7b",
                "playerId": "b8e3e424-c337-465c-bb82-0fa38c31e3fb",
                "roomId": "92cad0df-94e4-4a48-adb8-cde6c31bd233",
                "dice": 2,
                "oldIndex": 78,
                "newIndex": 80
            },
            {
                "id": "fc543e23-8e9b-4135-879c-afbeb3c0223c",
                "playerId": "b8e3e424-c337-465c-bb82-0fa38c31e3fb",
                "roomId": "92cad0df-94e4-4a48-adb8-cde6c31bd233",
                "dice": 4,
                "oldIndex": 80,
                "newIndex": 84
            },
            {
                "id": "2bfc1ea1-f7b8-4daa-beea-a28510739346",
                "playerId": "b8e3e424-c337-465c-bb82-0fa38c31e3fb",
                "roomId": "92cad0df-94e4-4a48-adb8-cde6c31bd233",
                "dice": 4,
                "oldIndex": 84,
                "newIndex": 88
            },
            {
                "id": "57430104-5a18-489d-845c-fb842555d928",
                "playerId": "b8e3e424-c337-465c-bb82-0fa38c31e3fb",
                "roomId": "92cad0df-94e4-4a48-adb8-cde6c31bd233",
                "dice": 1,
                "oldIndex": 88,
                "newIndex": 89
            },
            {
                "id": "13035190-1f8d-4ae6-9fa9-cb14e000300f",
                "playerId": "b8e3e424-c337-465c-bb82-0fa38c31e3fb",
                "roomId": "92cad0df-94e4-4a48-adb8-cde6c31bd233",
                "dice": 4,
                "oldIndex": 89,
                "newIndex": 93
            },
            {
                "id": "c5b361b7-73a9-47e7-b7e3-4788fc6c62a6",
                "playerId": "b8e3e424-c337-465c-bb82-0fa38c31e3fb",
                "roomId": "92cad0df-94e4-4a48-adb8-cde6c31bd233",
                "dice": 3,
                "oldIndex": 93,
                "newIndex": 96
            },
            {
                "id": "8e75e4b2-5229-41ea-b826-c6e0f522b54c",
                "playerId": "b8e3e424-c337-465c-bb82-0fa38c31e3fb",
                "roomId": "92cad0df-94e4-4a48-adb8-cde6c31bd233",
                "dice": 5,
                "oldIndex": 96,
                "newIndex": 99
            },
            {
                "id": "35c3a7af-a865-46e6-ad70-58e34bb432b6",
                "playerId": "b8e3e424-c337-465c-bb82-0fa38c31e3fb",
                "roomId": "92cad0df-94e4-4a48-adb8-cde6c31bd233",
                "dice": 4,
                "oldIndex": 99,
                "newIndex": 97
            },
            {
                "id": "cf264dce-326b-445b-9529-5fefaf472971",
                "playerId": "b8e3e424-c337-465c-bb82-0fa38c31e3fb",
                "roomId": "92cad0df-94e4-4a48-adb8-cde6c31bd233",
                "dice": 3,
                "oldIndex": 97,
                "newIndex": 100
            }
        ]
    },
    "msg": "操作成功",
    "code": 0
}
```
