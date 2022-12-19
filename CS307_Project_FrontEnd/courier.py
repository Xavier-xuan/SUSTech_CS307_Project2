import sys
import time
import util as u
import requests
from config import config
from urllib.parse import urljoin
from util import inputByType as iB
from util import checkResult as cR

def courier(username, passwd):
    role = "courier"
    info = ""
    flag = True
    while flag:
        u.clear()
        print()
        print("Hello! %s" %username)
        print("============================================================")
        print("|                       [Operations]                       |")
        print("|      Please Enter Number To Select Your Operation        |")
        print("|----------------------------------------------------------|")
        print("|                     1. New Item                          |")
        print("|                     2. Set Item State                    |")
        print("|----------------------------------------------------------|")
        print("|          X. Log Out                     Q. Quit          |")
        print("============================================================")
        print(info)
        print(">>> ", end="")
        op = input()
        if op == '1':
            item = iB("Item Name")
            itemType = iB("Item Type")
            price = iB("Item Price")
            fromCity = iB("From City")
            retrievalC = iB("Retrieval Courier")
            toCity = iB("To City")
            deliveryC = iB("Delivery Courier")
            exportCity = iB("Export City")
            exportOfficer = iB("Export Officer")
            exportTax = iB("Export Tax")
            importCity = iB("Import City")
            importOfficer = iB("Import Officer")
            importTax = iB("Import Tax")
            state = iB("Item State")
            result = new_item(username,passwd,item,itemType,price,state,fromCity,retrievalC,toCity,deliveryC,exportCity,exportOfficer,exportTax,importCity,importOfficer,importTax)
            cR(result)
        elif op =='2':
            item = iB("Item Name")
            state = iB("Item State")
            result = set_item_state(username,passwd,item,state)
            cR(result)
        elif op == 'X':
            flag = False
        elif op == 'Q':
            u.exit()
        else:
            info = ("<Wrong Selection>")

def new_item(username,passwd,
             item,itemType,price,state,
             fromCity,retrievalC,
             toCity,deliveryC,
             exportCity,exportOfficer,exportTax,
             importCity,importOfficer,importTax):
    result = requests.post(urljoin(config['base'], '/courier/new_item'), data = {
        'name': item,
        'class':itemType,
        'price':price,
        'state':state,
        'from_city':fromCity,
        'to_city':toCity,
        'retrieval_courier':retrievalC,
        'delivery_courier':deliveryC,
        'export_city':exportCity,
        'export_officer':exportOfficer,
        'export_tax':exportTax,
        'import_city': importCity,
        'import_officer': importOfficer,
        'import_tax': importTax
    }, headers={
        'username': username,
        'password': passwd,
        'role': 'courier'
    }).json()
    return result

def set_item_state(username,passwd,item,state):
    result = requests.post(urljoin(config['base'], '/courier/set_item_state'), data ={
        'state': state,
        'name': item
    }, headers={
        'username': username,
        'password': passwd,
        'role': 'courier'
    }).json()
    return result
