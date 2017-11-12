#!/usr/bin/python
# -*- coding: iso-8859-15 -*-
import os, sys, time

from flask import Flask, redirect, url_for, request, render_template, session
from pymongo import MongoClient
from validate_email import validate_email
#import udacity
import json, urllib
import libgenapi

import os

PORT = int(os.getenv('VCAP_APP_PORT', '8000'))

global name, flag1, flag2, user, password, account_number, balance, amount, account_number2, balance2, error, typo

app = Flask(__name__)
app.secret_key = "123213"


@app.route('/')
def welcome():
    return render_template('start.html')

@app.route('/loginpage')
def loginpage():
    return render_template('login.html')

@app.route('/signuppage')
def signuppage():
    return render_template('signup.html')

@app.route('/home')
def home():
    return render_template('welcome.html')

@app.route('/login', methods=['POST', 'GET'])
def login():
    client = MongoClient("mongodb://lnmhacks:lnmlnm@ds255715.mlab.com:55715/lnmhacks")
    db = client.lnmhacks

    flag1 = 0
    user = 0
    password = 0
    error=0

    if request.method == 'POST':
        user = request.form['user_id']
        password = request.form['password']
    cursor = db.users.find({"id": user}, {"password": 1, "_id": 0})

    if cursor.count() == 0:
        error=1

    for document in cursor:
        value = document['password']
        if value == password:
            flag1 = 1
        elif value!=password:
            error = 2

    
    cursor = db.users.find({"id": user}, {"name": 1, "_id": 0})
    for document in cursor:
        session['name'] = document['name']

    cursor2 = db.customer.find({"id": user}, {"interest1": 1, "interest2": 1, "interest3": 1, "_id": 0})
    for document in cursor2:
        session['interest1'] = document['interest1']
        session['interest2'] = document['interest2']
        session['interest3'] = document['interest3']
    
          

    if flag1 and error == 0:
        session['username'] = user
        return render_template('welcome.html')
    elif error==1 or error==2:
        return render_template('login.html',result=error)


    return render_template('login.html')

@app.route('/signup', methods=['POST', 'GET'])
def signup():
    client = MongoClient("mongodb://lnmhacks:lnmlnm@ds255715.mlab.com:55715/lnmhacks")
    db = client.lnmhacks

    #if request.method == 'POST':
    user = request.form['user_id']
    password = request.form['password']
    name = request.form['name']
    age = request.form['age']
    email = request.form['email']
    mobile = request.form['mobile']

    is_valid = validate_email(email)
    length = len(mobile)

    cursor = db.users.find({"id": user})

    if cursor.count()!=0:
        error=1
        return render_template('signup.html',result=error)

    elif cursor.count()==0 and is_valid and length == 10:
        db.customer.insert_one(
            {
                "id": user,
                "name": name,
                "age": age,
                "mobile": mobile,
                "email": email
            }
        )
        error = -1
        db.users.insert_one(
            {
                "id": user,
                "name": name,
                "password": password
            }
        )
        session['username']=user
        return render_template('interest.html',result=error)

    elif not(is_valid) or length != 10:
        error=2
        return render_template('signup.html',result=error)

    else:
        return render_template('signup.html')

@app.route('/interest', methods=['POST', 'GET'])
def interest():
    client = MongoClient("mongodb://lnmhacks:lnmlnm@ds255715.mlab.com:55715/lnmhacks")
    db = client.lnmhacks

    interest1 = request.form['interest1']
    interest2 = request.form['interest2']
    interest3 = request.form['interest3']

    

    if interest1!=interest2 and interest2!=interest3 and interest3!=interest1:
        error=0
        db.customer.update_one({"id": session['username']}, {"$set": {"interest1": interest1, "interest2": interest2, "interest3": interest3}})
        return render_template('login.html', result=error)
    else:
        error =1
        return render_template('interest.html',result=error)    
    
@app.route('/courses1', methods=['POST', 'GET'])
def courses1():

    response = urllib.urlopen('https://www.udacity.com/public-api/v0/courses')
    json_responses = json.loads(response.read())

    # for courses in json_responses['courses']:
    #     course = courses['title'].lower()
    #     if course.find(interest1) != -1:
    #         courselist.append(courses['title'])
    return render_template('courses_interest1.html',result=json_responses,result2=0,result3=0)
            
@app.route('/courses2', methods=['POST', 'GET'])
def courses2():

    a=[]
    b=[]
    c=[]
    d=[]
    e=[]
    response = urllib.urlopen('https://www.udacity.com/public-api/v0/courses')
    json_responses = json.loads(response.read())

    # for courses in json_responses['courses']:
    #     course = courses['title'].lower()
    #     a=session['interest1'].split()
    #     if course.find(session['interest1'].lower()) != -1 or course.find(a[0].lower()) != -1 :





    return render_template('courses_interest2.html',result=json_responses)

@app.route('/courses3', methods=['POST', 'GET'])
def courses3():


    response = urllib.urlopen('https://www.udacity.com/public-api/v0/courses')
    json_responses = json.loads(response.read())


    return render_template('courses_interest3.html',result=json_responses)

@app.route('/books1', methods=['POST', 'GET'])
def books1():
    
    lg=libgenapi.Libgenapi(["http://gen.lib.rus.ec"])
    r = lg.search(session['interest1'],1)

    return render_template('books_interest1.html',result=r)

@app.route('/books2', methods=['POST', 'GET'])
def books2():
    
    lg=libgenapi.Libgenapi(["http://gen.lib.rus.ec"])
    r = lg.search(session['interest2'],1)

    return render_template('books_interest2.html',result=r)

@app.route('/books3', methods=['POST', 'GET'])
def books3():
    
    lg=libgenapi.Libgenapi(["http://gen.lib.rus.ec"])
    r = lg.search(session['interest3'],1)

    return render_template('books_interest3.html',result=r)


@app.route('/viewinterest', methods=['POST', 'GET'])
def viewinterest():
    status=0    
    return render_template('viewinterest.html',result=status)

@app.route('/changeinterest', methods=['POST', 'GET'])
def changeinterest():

    client = MongoClient("mongodb://lnmhacks:lnmlnm@ds255715.mlab.com:55715/lnmhacks")
    db = client.lnmhacks

    interest1 = request.form['interest1']
    interest2 = request.form['interest2']
    interest3 = request.form['interest3']

    db.customer.update_one({"id": session['username']}, {"$set": {"interest1": interest1, "interest2": interest2, "interest3": interest3}})

    session['interest1'] = interest1
    session['interest2'] = interest2
    session['interest3'] = interest3
    status = 1

    return render_template('viewinterest.html',result=status)


if __name__ == '__main__':
    app.run(host='0.0.0.0', port=int(PORT), debug=True)

