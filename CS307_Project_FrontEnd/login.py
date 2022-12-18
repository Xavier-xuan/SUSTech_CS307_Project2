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
        print("============================================================")
        print("|                        [Login]                           |")
        print("|      Please Enter Numer To Select Your Role First        |")
        print("|----------------------------------------------------------|")
        print("|  1.Courier       2.Company Manager    3.Seaport Officer  |")
        print("|  4.SUSTCManager                       X.Exit             |")
        print("============================================================")
        if info != "":
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
    u.clear()
    print("============================================================")
    print("|                        [Login]                           |")
    print("|        Please Enter Your Username and Password           |")
    print("============================================================")
    print(">>> Userbame: ", end="")
    username = input()
    passwd = getpass(prompt=">>> Password: ")

    r = requests.post(urljoin(config['base'], '/login'), data = {
        "username": username,
        "role": role,
        "password": passwd
    })
    result = r.json()
    if(result):
        print("Login Successfully!")
    else:
        print("Wrong Username or Password!")

if __name__ == '__main__':
    login()






