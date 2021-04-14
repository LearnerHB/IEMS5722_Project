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



if __name__ == "__main__":
    app.run(host='0.0.0.0')
