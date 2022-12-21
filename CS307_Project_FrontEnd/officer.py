import time

import util as u
import requests
from config import config
from urllib.parse import urljoin
from util import inputByType as iB
from util import checkResult as cR


def officer(username, passwd):
    role = "officer"
    info = ""
    flag = True
    while flag:
        u.clear()
        print()
        print("Hello! %s" % username)
        print("============================================================")
        print("|                       [Operations]                       |")
        print("|      Please Enter Number To Select Your Operation        |")
        print("|----------------------------------------------------------|")
        print("|                 1. Get All Item At Port                  |")
        print("|                 2. Set Item Check State                  |")
        print("|----------------------------------------------------------|")
        print("|          X. Log Out                     Q. Quit          |")
        print("============================================================")
        print(info)
        print(">>> ", end="")
        op = input()
        if op == '1':
            get_all_items_at_port(username, passwd)
        elif op == '2':
            item = iB("Item Name")
            state = iB("Check State(true/false)")
            result = set_item_check_state(username, passwd, item, state)
            cR(result)
        elif op == 'X':
            flag = False
        elif op == 'Q':
            u.exiting()
        else:
            info = ("<Wrong Selection>")


def get_all_items_at_port(username, passwd):
    result = requests.get(urljoin(config['base'], '/officer/get_all_items_at_port'), headers={
        'username': username,
        'password': passwd,
        'role': 'officer'
    }).json()
    print(result)
    input("Press Enter to continue...")

def set_item_check_state(username, passwd, item, state):
    result = requests.post(urljoin(config['base'], '/officer/set_item_check_state'), data={
        'success': state,
        'item_name': item
    }, headers={
        'username': username,
        'password': passwd,
        'role': 'officer'
    }).json()
    return result
