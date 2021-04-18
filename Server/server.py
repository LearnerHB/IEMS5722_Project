from flask import Flask, request, jsonify
import json
import time
import mysql.connector

app = Flask(__name__)
app.debug = True

# !
# Since all the service have already built up,
# if the service has broken down, just run:
# `supervisorctl restart server`
# `sudo service nginx restart`
# to restart the Nginx process

class MyDatabase:
    conn = None
    cursor = None

    def __init__(self):
        self.connect()
        return
    
    def connect(self):
        self.conn = mysql.connector.connect(
            host = "localhost",
            port = 3306,
            user = "dbuser",
            password = "dbuser666",
            database = "project"
        )
        self.cursor = self.conn.cursor(dictionary = True)
        return

@app.route("/")
def hello_world():
    return "Hello World!"

# 添加朋友
@app.route("/api/project/add_friend", methods=["POST"])
def add_friend():
    print("-----ACTION: add_friend-----")
    request_id = request.form.get("request_id")
    receive_nickname = request.form.get("receive_nickname")
    print("--REQUEST CONTENT: request id: ", request_id, ", receive name: ", receive_nickname)
    mydb = MyDatabase()
    
    # 根据nickname 查看是否存在
    query = "select user_id FROM user where nickname='" + receive_nickname+"'"
    mydb.cursor.execute(query)
    receive_id = mydb.cursor.fetchone()
    
    if receive_id is None:
        return jsonify(status="0")  # 没有该用户
    print("QUERY: ", query)
    print("RESULT: ", receive_id)
    print("receive id: ", receive_id['user_id'])

    query = "select count(*) as count FROM relationship where user_id1='"+request_id+"' and user_id2='"+str(receive_id['user_id'])+"'"
    mydb.cursor.execute(query)
    is_friend = mydb.cursor.fetchone()
    print("QUERY: ", query)
    print("RESULT: ", is_friend)
    if is_friend['count'] != 0:
        return jsonify(status="2")  # 已经添加了
    else:
        # print(receive_id['user_id'])
        query = 'INSERT INTO waiting_list(request_user_id,receive_user_id) values(' + request_id + "," + str(receive_id['user_id']) + ")"
        print("QUERY: ", query)
        mydb.cursor.execute(query)
        mydb.conn.commit()
        return jsonify(status="1")  # 已发送请求

    conn.close()

# 获得添加列表
@app.route("/api/project/get_friend_request", methods=["GET"])
def get_friend_request():
    print("-----ACTION: get_friend_request-----")
    user_id = request.args.get("user_id")
    mydb = MyDatabase()
    query = "select nickname FROM waiting_list,user where receive_user_id=" + str(user_id) +" and status=0 and request_user_id=user_id"
    mydb.cursor.execute(query)
    request_users = mydb.cursor.fetchall()
    print("QUERY: ", query)
    print("RESULT: ", request_users)
    mydb.conn.close()
    return jsonify(status="ok", data=request_users)  # 已经添加了

# 获得联系人列表
@app.route("/api/project/get_friends", methods=["GET"])
def get_friends():
    print("-----ACTION: get_friends-----")
    user_id = request.args.get("user_id")
    mydb = MyDatabase()
    query = "select nickname FROM relationship,user where user_id1=" + str(user_id) +" and user_id=user_id2"
    mydb.cursor.execute(query)
    all_friends = mydb.cursor.fetchall()
    print("QUERY: ", query)
    print("RESULT: ", all_friends)
    mydb.conn.close()
    
    return jsonify(status="ok", data=all_friends)  # 已经添加了


# 添加好友操作（接受或拒绝 接受1 拒绝0）
@app.route("/api/project/accpet_or_refuse", methods=["GET"])
def accpet_or_refuse():
    print("-----ACTION: accept_or_refuse-----")
    operation = request.args.get("operation")
    request_name = request.args.get("request_name")
    receive_id = request.args.get("receive_id")
    print("operation: ", operation)
    print("request name: ", request_name)
    print("receive id: ", receive_id)

    mydb = MyDatabase()
    # 先找出该用户名对应的id
    query = "SELECT user_id FROM user WHERE nickname =" + request_name
    mydb.cursor.execute(query)
    result = mydb.cursor.fetchone()
    print("QUERY: ", query)
    print("RESULT: ", result)
    request_id = result['user_id']
    print("request id: ", request_id)

    # waiting_list 删除该记录
    query = "delete FROM waiting_list WHERE receive_user_id=" + str(receive_id) +\
            " and request_user_id="+str(request_id)
    print("QUERY: ", query)
    mydb.cursor.execute(query)
    mydb.conn.commit()

    if(operation == '1'):
        print("--Accept--")
        query = "INSERT INTO relationship values("+str(request_id)+","+str(receive_id)+")"
        print("QUERY: ", query)
        mydb.cursor.execute(query)
        query = "INSERT INTO relationship values(" + str(receive_id) + "," + str(request_id) + ")"
        print("QUERY: ", query)
        mydb.cursor.execute(query)
        mydb.conn.commit()

    else:
        print("--Refuse--")

    # query = "select nickname FROM relationship,user where user_id1=" + str(user_id) +" and user_id=user_id2"
    # print(query)
    # cursor.execute(query)
    # all_friends = cursor.fetchall()

    mydb.conn.close()

    return jsonify(status="ok")  # 已经添加了

# 发朋友圈
@app.route("/api/project/posts", methods=["POST"])
def new_posts():
    print("-----ACTION: new posts-----")
    user_id = request.form.get("user_id")
    posts_content = request.form.get("posts_content")

    # 点赞数 默认为0
    # 评论数 默认为0
    mydb = MyDatabase()
    query = 'INSERT INTO circle_posts (user_id,post_content) values('+user_id+",'"+posts_content+"')"
    print("QUERY: ", query)
    mydb.cursor.execute(query)
    mydb.conn.commit()
    mydb.conn.close()
    return jsonify(status="ok")  # 发送朋友圈成功


# 获取朋友圈列表
@app.route("/api/project/posts", methods=["GET"])
def get_posts():
    print("-----ACTION: get posts-----")
    user_id = request.args.get("user_id")
    # 点赞数 默认为0
    # 评论数 默认为0
    mydb = MyDatabase()
    query = "SELECT m.*,n.is_like FROM(" \
            "SELECT circle_posts.*,nickname FROM circle_posts,user WHERE circle_posts.user_id IN (" \
            "select DISTINCT(user_id2) AS user_id FROM relationship where " \
            "user_id1 = "+user_id+" OR user_id2="+user_id+") AND circle_posts.user_id = user.user_id) as m " \
            "left join (SELECT * FROM likes_info WHERE user_id="+user_id+") AS n ON m.post_id=n.post_id ORDER BY post_time DESC"
    print("QUERY: ", query)
    mydb.cursor.execute(query)
    result = mydb.cursor.fetchall()
    print("RESULT: ", result)
    mydb.conn.close()
    return jsonify(status="ok", data=result)  # 发送朋友圈成功

# 点赞
@app.route("/api/project/like", methods=["POST"])
def give_like():
    print("-----ACTION: like-----")
    user_id = request.form.get("user_id")
    post_id = request.form.get("post_id")
    mydb = MyDatabase()
    query = "REPLACE INTO likes_info VALUES("+post_id+","+user_id+",1)"
    print("QUERY: ", query)
    # 将表中的值+1
    query = "UPDATE circle_posts SET likes=likes+1 WHERE post_id = "+post_id
    print("QUERY: ", query)
    mydb.cursor.execute(query)
    mydb.conn.commit()
    mydb.cursor.close()
    mydb.conn.close()

    return jsonify(status="ok")

# 取消点赞
@app.route("/api/project/dislike", methods=["POST"])
def cancel_like():
    print("-----ACTION: dislike-----")
    user_id = request.form.get("user_id")
    post_id = request.form.get("post_id")
    mydb = MyDatabase()
    query = "DELETE FROM likes_info WHERE post_id="+post_id+" AND user_id="+user_id
    print("QUERY: ", query)
    mydb.cursor.execute(query)
    query = "UPDATE circle_posts SET likes=likes-1 WHERE post_id = " + post_id
    print("QUERY: ", query)
    mydb.cursor.execute(query)
    mydb.conn.commit()
    mydb.cursor.close()
    mydb.conn.close()
    return jsonify(status="ok")

# 用户注册
@app.route("/api/project/register", methods=["POST"])
def register():
    print("-----ACTION: register-----")
    username = request.form.get("username")
    password = request.form.get("password")
    mydb = MyDatabase()
    query = "INSERT INTO user(nickname,password) VALUES ('"+username+"',"+password+")"
    print("QUERY: ", query)
    mydb.cursor.execute(query)
    mydb.conn.commit()
    mydb.cursor.close()
    mydb.conn.close()
    return jsonify(status="ok")

# 用户登入
@app.route("/api/project/login", methods=["POST"])
def login():
    print("-----ACTION: login-----")
    username = request.form.get("username")
    password = request.form.get("password")
    mydb = MyDatabase()
    query = "SELECT COUNT(*) AS count,user_id AS id FROM user WHERE nickname='"+username+"' and password='"+password+"'"
    mydb.cursor.execute(query)
    result = mydb.cursor.fetchone()
    print("QUERY: ", query)
    print("RESULT: ", result)    
    mydb.conn.commit()
    mydb.cursor.close()
    mydb.conn.close()
    if result["count"] == 0:
        return jsonify(status="error")
    else:
        return jsonify(status="ok",id=result["id"])


# 获得所有通讯录上的人
@app.route("/api/project/rooms", methods=["GET"])
def get_rooms():
    print("-----ACTION: get_rooms-----")
    user_id = request.args.get("user_id")
    mydb = MyDatabase()
    query = "SELECT nickname,user_id FROM user WHERE user_id in(" \
            "SELECT user_id2 FROM relationship WHERE user_id1="+user_id+")"
    mydb.cursor.execute(query)
    result = mydb.cursor.fetchall()
    print("QUERY: ", query)
    print("RESULT: ", result)    
    mydb.conn.commit()
    mydb.cursor.close()
    mydb.conn.close()
    return jsonify(status="ok", data=result)


# 获得对应好友的聊天消息
@app.route("/api/project/messages", methods=["GET"])
def get_messages():
    print("-----ACTION: get_messages-----")
    curr_user = request.args.get("curr_user")
    target_user = request.args.get("target_user")
    mydb = MyDatabase()
    query = "SELECT message_id,send_user,receive_user,message,DATE_FORMAT(message_time,'%Y-%m-%d %H:%i') as message_time FROM messages WHERE (send_user="+curr_user+" and receive_user="+target_user+\
            ") OR (send_user="+target_user+" and receive_user="+curr_user+") ORDER BY message_time DESC"
    print("QUERY: ", query)
    mydb.cursor.execute(query)
    result = mydb.cursor.fetchall()
    print("RESULT: ", result)
    mydb.conn.commit()
    mydb.cursor.close()
    mydb.conn.close()
    return jsonify(status="ok", data=result)


# 发送消息
@app.route("/api/project/messages", methods=["POST"])
def post_message():
    print("-----ACTION: post_messages-----")
    send_user = request.form.get("send_user")
    receive_user = request.form.get("receive_user")
    message = request.form.get("message")

    mydb = MyDatabase()
    query = "INSERT INTO messages(send_user,receive_user,message) VALUES " \
            "("+send_user+","+receive_user+",'"+message+"')"
    print("QUERY: ", query)
    mydb.cursor.execute(query)
    result = mydb.cursor.fetchone()
    print("RESULT: ", result)
    mydb.conn.commit()
    mydb.cursor.close()
    mydb.conn.close()
    return jsonify(status="ok")


if __name__ == "__main__":
    app.run(host='0.0.0.0')
