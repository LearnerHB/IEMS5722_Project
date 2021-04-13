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





if __name__ == "__main__":
    app.run(host='0.0.0.0')
