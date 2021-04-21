# Database Design



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

