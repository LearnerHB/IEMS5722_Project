# IEMS5722_Project
## Group Members

|    Member    | Student ID |  Commit_Author.name  |
| :----------: | :--------: | :------------------: |
|   HU Bing    | 1155151103 |       LearnerHB      |
| CHEN Jiaxian | 1155152636 |   CHENNNJX & 橙夹馅   |
|  SUN Suwei   | 1155152413 |       Eillotsun      |



## Features Realized

### 1. User Register & Login

<img src="https://tencent-hb666-1253906972.cos.ap-chengdu.myqcloud.com/photo/2021-04-19-112156.png" alt="image-20210419102531676" style="zoom:50%;" />

### 2. Contact: Add Friends via User ID

<img src="https://tencent-hb666-1253906972.cos.ap-chengdu.myqcloud.com/photo/2021-04-19-112202.png" alt="image-20210419102604658" style="zoom:50%;" />


### 3. Chat: Private Chat & Chatroom

<img src="https://tencent-hb666-1253906972.cos.ap-chengdu.myqcloud.com/photo/2021-04-19-112207.png" alt="image-20210419102728600" style="zoom:50%;" />

### 4. Moments

<img src="https://tencent-hb666-1253906972.cos.ap-chengdu.myqcloud.com/photo/2021-04-19-112213.png" alt="image-20210419132155984" style="zoom:50%;" />

### 5. Comments & Scan QR Code (TODO)

<img src="https://tencent-hb666-1253906972.cos.ap-chengdu.myqcloud.com/photo/2021-04-19-112218.png" alt="image-20210419103729609" style="zoom:50%;" />




## Database Design

### user

|          | Type        | Constraint          |
| -------- | ----------- | ------------------- |
| user_id  | INT         | KEY, AUTO_INCREMENT |
| nickname | VARCHAR(20) | UNIQUE NOT NULL     |
| password | VARCHAR(20) | NOT NULL            |

PRIMARY KEY (user_id)



### waiting_list

|                 | Type | Constraint  |
| --------------- | ---- | ----------- |
| request_user_id | INT  | FOREIGN KEY |
| receive_user_id | INT  | FOREIGN KEY |
| status          | INT  | Default 0   |

PRIMARY KEY (request_user_id, receive_user_id)



### relationship

|           | Type | Constraint  |
| --------- | ---- | ----------- |
| user_id_1 | INT  | FOREIGN KEY |
| user_id_2 | INT  | FOREIGN KEY |

PRIMARY KEY (user_id_1, user_id_2)



### posts

|              | Type         | Constraint                 |
| ------------ | ------------ | -------------------------- |
| post_id      | INT          | PRIMARYKEY, AUTO_INCREMENT |
| user_id      | INT          | FOREIGN KEY                |
| post_content | VARCHAR(200) | NOT NULL                   |
| post_time    | TIMESTAMP    | DEFAULT CURRENT_TIMESTAMP  |
| likes        | INT          | DEFAULT 0                  |

PRIMARY KEY (post_id)



### likes_info

|         | Type       | Constraint  |
| ------- | ---------- | ----------- |
| post_id | INT        | FOREIGN KEY |
| user_id | INT        | FOREIGN KEY |
| is_like | TINYINT(1) | DEFAULT 0   |

PRIMARY KEY (post_id, user_id)



### messages

|              | Type         | Constraint                |
| ------------ | ------------ | ------------------------- |
| message_id   | INT          | AUTO_INCREMENT            |
| send_user    | INT          | FOREIGN KEY               |
| receive_user | INT          | FOREIGN KEY               |
| message      | VARCHAR(200) | NOT NULL                  |
| message_time | TIMESTAMP    | DEFAULT CURRENT_TIMESTAMP |

PRIMARY KEY (message_id, send_user, receive_user)



### post_comments (todo)

|                 | Type         | Constraint                |
| --------------- | ------------ | ------------------------- |
| comment_id      | INT          | AUTO_INCREMENT            |
| user_id         | INT          | FOREIGN KEY               |
| post_id         | INT          | FOREIGN KEY               |
| comment_content | VARCHAR(200) | NOT NULL                  |
| comment_time    | TIMESTAMP    | DEFAULT CURRENT_TIMESTAMP |

PRIMARY KEY (comment_id)



## APIs

### 1. POST	/api/project/register

**Description**

- API for user register 

**Input Parameters**

| Paramter | Required | Type   | Description |
| :------- | :------- | :----- | ----------- |
| username | Yes      | string | User name   |
| password | Yes      | string | Password    |

**Example**

Post the following to the API:

username=test&password=1234

**Sample Output**

```json
{
    "status": "ok"
}
```



### 2. POST	/api/project/login

**Description**

- API for user login 

**Input Parameters**

| Paramter | Required | Type   | Description |
| :------- | :------- | :----- | ----------- |
| username | Yes      | string | User name   |
| password | Yes      | string | Password    |

**Example**

Post the following to the API:

username=test&password=1234

**Sample Output**

```json
{
  	"id": 71,
    "status": "ok"
}
```



### 3. Get	/api/project/get_friends

**Description**

- API for getting all friends in a certain user's friend list

**Input  Parameters**

| Paramter Name | Required | Type | Description        |
| :------------ | :------- | :--- | ------------------ |
| user_id       | Yes      | int  | the unique user id |

**Example**

/api/project/rooms?&user_id=1

**Sample Output**

```json
{
    "data": [
        {
            "nickname": "CHENN"
        }
    ],
    "status": "ok"
}
```



### 4. GET	/api/project/messages

**Description**

- API for posting message to a certain friend

**Input  Parameters**

| Paramter Name | Required | Type | Description                          |
| :------------ | :------- | :--- | ------------------------------------ |
| curr_user     | Yes      | int  | current logined user id              |
| target_user   | Yes      | int  | certain user id to find the messages |

**Example**

/api/project/messages?curr_user=34&target_user=23

**Sample Output**

```json
{
    "data": [
        {
            "message": "test",
            "message_id": 10,
            "message_time": "2021-04-15 07:10",
            "receive_user": 34,
            "send_user": 23
        },
        {
            "message": "TT",
            "message_id": 8,
            "message_time": "2021-04-15 06:54",
            "receive_user": 23,
            "send_user": 34
        }
    ],
    "status": "ok"
}
```



### 5. POST	/api/project/messages

**Description**

- API for getting messages from a certain friend

**Input  Parameters**

| Paramter Name | Required | Type   | Description                                 |
| :------------ | :------- | :----- | ------------------------------------------- |
| send_user     | Yes      | int    | current logined user id, the message sender |
| receive_user  | Yes      | int    | certain user id, the message receiver       |
| message       | Yes      | string | message sent by the send_user               |

**Example**

Post the following to the API:

/api/project/messages?curr_user=34&target_user=23&message=hi

**Sample Output**

```json
{
    "status": "ok"
}
```



### 6. GET	/api/a3/get_chatrooms

**Description**

- API for retrieving a list of chatrooms

**Input  Parameters**

No input parameters is required. 

**Example**

/api/a3/get_chatrooms

**Sample Output**

```json
{
    "data": [
        {
            "id": 1,
            "name": "chatroom1"
        },
        {
            "id": 2,
            "name": "chatroom2"
        }
    ],
    "status": "OK"
}
```



### 7. GET	/api/a3/get_messages

**Description**

- API for retrieving a list of messages in a specific chatroom

**Input  Parameters**

| Paramter Name | Required | Type | Description                             |
| :------------ | :------- | :--- | --------------------------------------- |
| chatroom_id   | Yes      | int  | the Id of the chatroom                  |
| page          | Yes      | int  | the page number of the list of messages |

**Example**

/api/a3/get_messages?chatroom_id=1&page=1

**Sample Output**

```json
{
    "data": {
        "current_page": 1,
        "messages": [
            {
                "chatroom_id": 1,
                "id": 226,
                "message": "hhhhhhhh\n",
                "message_time": "2021-04-17 23:47:01",
                "name": "HU Bing"
            },
            {
                "chatroom_id": 1,
                "id": 224,
                "message": "TT",
                "message_time": "2021-04-17 23:46:37",
                "name": "CHEN Jiaxian"
            }
        ],
        "total_pages": 22
    },
    "status": "OK"
}
```



### 8. POST	/api/a3/send_message

**Description**

- API for sending a message in a specific chatroom

**Input  Parameters**

| Paramter Name | Required | Type   | Description                    |
| :------------ | :------- | :----- | ------------------------------ |
| chatroom_id   | Yes      | int    | the Id of the chatroom         |
| user_id       | Yes      | int    | the unique user id             |
| name          | Yes      | string | the displayed name of the user |
| messages      | Yes      | string | the message input by the user  |

**Example**

Post the following to the API:

chatroom_id=1&user_id=1&name=test&message=hi

**Sample Output**

```json
{
    "status": "ok"
}
```



### 9. POST	/api/project/add_friend

**Description**

- API for send a add friend request to a certain user

**Input  Parameters**

| Paramter Name    | Required | Type   | Description                     |
| :--------------- | :------- | :----- | ------------------------------- |
| request_id       | Yes      | int    | the id of current user          |
| receive_nickname | Yes      | string | the add friend request receiver |

**Example**

Post the following to the API:

request_id=35&receive_nickname=CHENN

**Sample Output**

```json
{
    "status": "2"
}
```



### 10. GET	/api/project/get_friend_request

**Description**

- API for retrieving a list of add-friend request to the current user

**Input  Parameters**

| Paramter Name | Required | Type | Description            |
| :------------ | :------- | :--- | ---------------------- |
| user_id       | Yes      | int  | the id of current user |

**Example**

/api/project/get_friend_request?user_id=35

**Sample Output** 

```json
{
    "data": [
        {
            "nickname": "CHEN Jiaxian"
        }
    ],
    "status": "ok"
}
```



### 11. GET	/api/project/accept_or_refuse

**Description**

- API for accept or refuse a certain add friend request

**Input  Parameters**

| Paramter Name | Required | Type   | Description                        |
| :------------ | :------- | :----- | ---------------------------------- |
| operation     | Yes      | int    | 1 for accept, else refuse          |
| request_name  | Yes      | string | name of user who send this request |
| receive_id    | Yes      | int    | user id of request receiver        |

**Example**

/api/project/accept_or_refuse?operation=1&request_name=HU Bing&receive_id=47

**Sample Output** 

```json
{
    "status": "ok"
}
```



### 12. POST	/api/project/posts

**Description**

- API for send a post to the "Moment"

**Input  Parameters**

| Paramter Name | Required | Type   | Description                        |
| :------------ | :------- | :----- | ---------------------------------- |
| user_id       | Yes      | int    | the unique id for a certain user   |
| posts_content | Yes      | string | Content of the post sent to Moment |

**Example**

Post the following to the API:

user_id=23&posts_content=todayisagoodday

**Sample Output** 

```json
{
    "status": "ok"
}
```



### 13. GET	/api/project/posts

**Description**

- API for getting all the posts from a certain user's friends

**Input  Parameters**

| Paramter Name | Required | Type | Description                      |
| :------------ | :------- | :--- | -------------------------------- |
| user_id       | Yes      | int  | the unique id for a certain user |

**Example**

/api/project/posts?user_id=23

**Sample Output** 

```json
{
    "data": [
        {
            "is_like": null,
            "likes": 0,
            "nickname": "SUN Suwei",
            "post_content": "Test",
            "post_id": 4,
            "post_time": "Sat, 17 Apr 2021 08:45:36 GMT",
            "user_id": 35
        },
        {
            "is_like": 1,
            "likes": 0,
            "nickname": "CHENN",
            "post_content": "thanks for your",
            "post_id": 1,
            "post_time": "Tue, 13 Apr 2021 14:03:23 GMT",
            "user_id": 23
        }
    ],
    "status": "ok"
}
```



### 14. POST	/api/project/like

**Description**

- API for send a "like" to a certain post

**Input  Parameters**

| Paramter Name | Required | Type | Description                      |
| :------------ | :------- | :--- | -------------------------------- |
| user_id       | Yes      | int  | the unique id for a certain user |
| post_id       | Yes      | int  | the unique id for a certain post |

**Example**

Post the following to the API:

user_id=47&post_id=18

**Sample Output** 

```json
{
    "status": "ok"
}
```



### 15. POST	/api/project/dislike

**Description**

- API for withdrawing a "like" from a certain post

**Input  Parameters**

| Paramter Name | Required | Type | Description                      |
| :------------ | :------- | :--- | -------------------------------- |
| user_id       | Yes      | int  | the unique id for a certain user |
| post_id       | Yes      | int  | the unique id for a certain post |

**Example**

Post the following to the API:

user_id=47&posy_id=18

**Sample Output** 

```json
{
    "status": "ok"
}
```























