import time
import util as u
from getpass import getpass
import requests
from config import config
from urllib.parse import urljoin


def login():
    flag = True
    info = role = ""
    while flag:
        flag = False
        u.clear()
        print()
        print("============================================================")
        print("|                         [Login]                          |")
        print("|         Please Enter Number To Select Your Role          |")
        print("|----------------------------------------------------------|")
        print("|  1. Courier      2. Company Manager   3. Seaport Officer |")
        print("|  4. SUSTCManager                      Q. Exit            |")
        print("============================================================")
        print(info)
        print(">>> ", end="")
        op = input()
        if op == '1':
            role = "courier"
        elif op == '2':
            role = "company manager"
        elif op == '3':
            role = "seaport officer"
        elif op == '4':
            role = "sustc manager"
        elif op == 'Q':
            u.exiting()
        else:
            info = "<Wrong Selection>"
            flag = True
    info = ""
    u.clear()
    print("============================================================")
    print("|                        [Login]                           |")
    print("|        Please Enter Your Username and Password           |")
    print("============================================================")
    print(info)
    print(">>> Username: ", end="")
    username = input()
    passwd = getpass(prompt=">>> Password: ")
    r = requests.post(urljoin(config['base'], '/login'), data={
        "username": username,
        "role": role,
        "password": passwd
    })
    result = r.json()
    if (result):
        print("              =============================              ")
        print("              |    Login Successfully!    |              ")
        print("              =============================              ")
        time.sleep(1)
        success = True
    else:
        print("<Wrong Username or Password!>")
        time.sleep(1)
        success = False
    return username, passwd, role, success
