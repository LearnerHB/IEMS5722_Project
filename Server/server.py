from flask import Flask, request, jsonify
import json
import time
import mysql.connector

app = Flask(__name__)

@app.route("/")
def hello_world():
    return "Hello World!"


# 用户注册
@app.route("/api/project/register", methods=["POST"])
def register():
    username = request.form.get("username")
    password = request.form.get("password")
    conn = mysql.connector.connect(host="localhost",
                                   port=3306,
                                   user="dbuser", password="password", database="project")  # dbuser password
    cursor = conn.cursor(dictionary=True)


    query = "INSERT INTO user(nickname,password) VALUES ('"+username+"',"+password+")"
    print(query)
    cursor.execute(query)
    conn.commit()
    cursor.close()
    conn.close()
    return jsonify(status="ok")



# 用户登入
@app.route("/api/project/login", methods=["POST"])
def login():
    username = request.form.get("username")
    password = request.form.get("password")
    conn = mysql.connector.connect(host="localhost",
                                   port=3306,
                                   user="dbuser", password="dbuser666", database="project")  # dbuser password
    cursor = conn.cursor(dictionary=True)

    query = "SELECT COUNT(*) AS count,user_id AS id FROM user WHERE nickname='"+username+"' and password='"+password+"'"
    cursor.execute(query)
    result = cursor.fetchone()
    print(result)
    conn.commit()
    cursor.close()
    conn.close()
    if result["count"] == 0:
        return jsonify(status="error")
    else:
        return jsonify(status="ok",id=result["id"])


# 添加朋友
@app.route("/api/project/add_friend", methods=["POST"])
def add_friend():
    request_id = request.form.get("request_id")
    receive_name = request.form.get("receive_nickname")

    # 查看是否已经是朋友
    conn = mysql.connector.connect(host="localhost",
                                   port=3306,
                                   user="dbuser", password="dbuser666", database="project")  # dbuser password
    cursor = conn.cursor(dictionary=True)

    # 根据nickname 查看是否存在
    query = "select user_id FROM user where nickname='" + receive_name+"'"
    cursor.execute(query)
    receive_id = cursor.fetchone()
    if receive_id is None:
        return jsonify(status="0")  # 没有该用户

    print(receive_id['user_id'])

    query = "select count(*) as count FROM relationship where user_id1='"+request_id+"' and user_id2='"+str(receive_id['user_id'])+"'"
    cursor.execute(query)
    is_friend = cursor.fetchone()
    print(is_friend)
    if is_friend['count'] != 0:
        return jsonify(status="2")  # 已经添加了
    else:
        # print(receive_id['user_id'])
        query = 'INSERT INTO waiting_list(request_user_id,receive_user_id) values(' + request_id + "," + str(receive_id['user_id']) + ")"
        cursor.execute(query)
        conn.commit()
        return jsonify(status="1")  # 已发送请求

    conn.close()


# 获得添加列表
@app.route("/api/project/get_friend_request", methods=["GET"])
def get_friend_request():
    user_id = request.args.get("user_id")
    conn = mysql.connector.connect(host="localhost",
                                   port=3306,
                                   user="dbuser", password="dbuser666", database="project")  # dbuser password
    cursor = conn.cursor(dictionary=True)
    query = "select nickname FROM waiting_list,user where receive_user_id=" + str(user_id) +" and status=0 and request_user_id=user_id"
    print(query)
    cursor.execute(query)
    request_users = cursor.fetchall()

    conn.close()
    print(request_users)
    return jsonify(status="ok", data=request_users)  # 已经添加了


# 获得联系人列表
@app.route("/api/project/get_friends", methods=["GET"])
def get_friends():
    user_id = request.args.get("user_id")
    conn = mysql.connector.connect(host="localhost",
                                   port=3306,
                                   user="dbuser", password="dbuser666", database="project")  # dbuser password
    cursor = conn.cursor(dictionary=True)
    query = "select nickname FROM relationship,user where user_id1=" + str(user_id) +" and user_id=user_id2"
    print(query)
    cursor.execute(query)
    all_friends = cursor.fetchall()

    conn.close()
    print(all_friends)
    return jsonify(status="ok", data=all_friends)  # 已经添加了



# 添加好友操作（接受或拒绝 接受1 拒绝0）
@app.route("/api/project/accpet_or_refuse", methods=["GET"])
def accpet_or_refuse():
    operation = request.args.get("operation")
    request_name = request.args.get("request_name")
    receive_id = request.args.get("receive_id")
    print(request_name)
    print(receive_id)

    conn = mysql.connector.connect(host="localhost",
                                   port=3306,
                                   user="dbuser", password="dbuser666", database="project")  # dbuser password
    cursor = conn.cursor(dictionary=True)

    # 先找出该用户名对应的id
    query = "SELECT user_id FROM user WHERE nickname =" + request_name
    print(query)
    cursor.execute(query)
    result = cursor.fetchone()
    request_id = result['user_id']
    print(request_id)

    # waiting_list 删除该记录
    query = "delete FROM waiting_list WHERE receive_user_id=" + str(receive_id) +\
            " and request_user_id="+str(request_id)
    cursor.execute(query)
    conn.commit()


    if(operation == '1'):
        print("接受")
        query = "INSERT INTO relationship values("+str(request_id)+","+str(receive_id)+")"
        cursor.execute(query)
        query = "INSERT INTO relationship values(" + str(receive_id) + "," + str(request_id) + ")"
        cursor.execute(query)
        conn.commit()

    else:
        print("拒绝")

    # query = "select nickname FROM relationship,user where user_id1=" + str(user_id) +" and user_id=user_id2"
    # print(query)
    # cursor.execute(query)
    # all_friends = cursor.fetchall()

    conn.close()

    return jsonify(status="ok")  # 已经添加了

# 发朋友圈
@app.route("/api/project/posts", methods=["POST"])
def new_posts():
    user_id = request.form.get("user_id")
    posts_content = request.form.get("posts_content")

    # 点赞数 默认为0
    # 评论数 默认为0

    conn = mysql.connector.connect(host="localhost",
                                   port=3306,
                                   user="dbuser", password="dbuser666", database="project")  # dbuser password
    cursor = conn.cursor(dictionary=True)

    query = 'INSERT INTO circle_posts (user_id,post_content) values('+user_id+",'"+posts_content+"')"
    print(query)

    cursor.execute(query)
    conn.commit()
    conn.close()
    return jsonify(status="ok")  # 发送朋友圈成功


# 获取朋友圈列表
@app.route("/api/project/posts", methods=["GET"])
def get_posts():
    user_id = request.args.get("user_id")
    # 点赞数 默认为0
    # 评论数 默认为0
    conn = mysql.connector.connect(host="localhost",
                                   port=3306,
                                   user="dbuser", password="dbuser666", database="project")  # dbuser password
    cursor = conn.cursor(dictionary=True)

    query = "SELECT m.*,n.is_like FROM(" \
            "SELECT circle_posts.*,nickname FROM circle_posts,user WHERE circle_posts.user_id IN (" \
            "select DISTINCT(user_id2) AS user_id FROM relationship where " \
            "user_id1 = "+user_id+" OR user_id2="+user_id+") AND circle_posts.user_id = user.user_id) as m " \
            "left join (SELECT * FROM likes_info WHERE user_id="+user_id+") AS n ON m.post_id=n.post_id ORDER BY post_time DESC"
    print(query)
    cursor.execute(query)
    result = cursor.fetchall()
    print(result)
    conn.close()
    return jsonify(status="ok", data=result)  # 发送朋友圈成功



if __name__ == "__main__":
    app.run(host='0.0.0.0')
