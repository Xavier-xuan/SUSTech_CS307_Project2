import time
import util as u
from getpass import getpass
import requests
from config import config
from urllib.parse import urljoin

def courier(username,passwd):
    # while flag:
    #     flag = False
    #     u.clear()
    #     print()
    #     print("============================================================")
    #     print("|                        [Login]                           |")
    #     print("|      Please Enter Numer To Select Your Role First        |")
    #     print("|----------------------------------------------------------|")
    #     print("|  1.Courier       2.Company Manager    3.Seaport Officer  |")
    #     print("|  4.SUSTCManager                       X.Exit             |")
    #     print("============================================================")
    #     print(info)
    #     print(">>> ",end="")
    #     op = input();
    #     if op == '1':
    #         role = "courier"
    #     elif op =='2':
    #         role = "company manager"
    #     elif op == '3':
    #         role = "seaport officer"
    #     elif op == '4':
    #         role = "sustc manager"
    #     else:
    #         info = ("<Wrong Selection>")
    #         flag = True

    ## set item state
    item_name = "xxxxxxx"
    result = requests.post(urljoin(config['base'], '/courier/set_item_state'), data ={
        'state': 'To-Export Transporting',
        'name': item_name
    }, headers={
        'username': username,
        'password': passwd,
        'role': 'courier'
    }).json()
    if result:
        print("Success!")
    else:
        print("Failed!")    