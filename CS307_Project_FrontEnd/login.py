import time
import courier
import util as u
from getpass import getpass
import requests
from config import config
from urllib.parse import urljoin

def login() :
    flag = True
    info = ""
    while flag:
        flag = False
        u.clear()
        print()
        print("============================================================")
        print("|                       [Courier]                          |")
        print("|      Please Enter Numer To Select Your Operation         |")
        print("|----------------------------------------------------------|")
        print("|  1.Courier       2.Company Manager    3.Seaport Officer  |")
        print("|  4.SUSTCManager                       X.Exit             |")
        print("============================================================")
        print(info)
        print(">>> ",end="")
        op = input();
        if op == '1':
            role = "courier"
        elif op =='2':
            role = "company manager"
        elif op == '3':
            role = "seaport officer"
        elif op == '4':
            role = "sustc manager"
        else:
            info = ("<Wrong Selection>")
            flag = True
    info = ""
    flag = True
    while flag==True:
        flag = False
        u.clear()
        print("============================================================")
        print("|                        [Login]                           |")
        print("|        Please Enter Your Username and Password           |")
        print("============================================================")
        print(info)
        print(">>> Username: ", end="")
        username = input()
        passwd = getpass(prompt=">>> Password: ")

        r = requests.post(urljoin(config['base'], '/login'), data = {
            "username": username,
            "role": role,
            "password": passwd
        })
        result = r.json()
        if(result):
            print("              =============================              ")
            print("              |    Login Successfully!    |              ")
            print("              =============================              ")
            time.sleep(1)
        else:
            info = ("Wrong Username or Password!")
            flag = True
    if role == "courier":
        courier.courier()
    # elif role == "company manager":
    #
    # elif role == "seaport officer":
    #
    # elif role == "sustc manager":



