
import util as u
import requests
from config import config
from urllib.parse import urljoin
from util import inputByType as iB
from util import checkResult as cR

global role
role = "company manager"
def companyManager(username, passwd):
    info = ""
    flag = True

    while flag:
        u.clear()
        print()
        print("Hello! %s: %s" % (role, username))
        print("============================================================")
        print("|                       [Operations]                       |")
        print("|      Please Enter Number To Select Your Operation        |")
        print("|----------------------------------------------------------|")
        print("|                  1. Get Import Tax Rate                  |")
        print("|                  2. Get Export Tax Rate                  |")
        print("|                 3. Load Item To Container                |")
        print("|                 4. Load Container To Ship                |")
        print("|                   5. Ship Start Sailing                  |")
        print("|                     6. Unload Item                       |")
        print("|                7. Item Wait For Checking                 |")
        print("|----------------------------------------------------------|")
        print("|          X. Log Out                     Q. Quit          |")
        print("============================================================")
        print(info)
        print(">>> ", end="")
        op = input()
        if op == '1':
            city = iB("City")
            itemType = iB("Item Type")
            get_import_tax_rate(username, passwd, city, itemType)
        elif op == '2':
            city = iB("City")
            itemType = iB("Item Type")
            get_export_tax_rate(username, passwd, city, itemType)
        elif op == '3':
            itemName = iB("Item")
            containerCode = iB("Container Code")
            result = load_item_to_container(username, passwd, itemName, containerCode)
            cR(result)
        elif op == '4':
            containerCode = iB("Container Code")
            shipName = iB("Ship Name")
            result = load_container_to_ship(username, passwd, shipName, containerCode)
            cR(result)
        elif op == '5':
            shipName = iB("Ship Name")
            result = ship_start_sailing(username, passwd, shipName)
            cR(result)
        elif op == '6':
            itemName = iB("Item Name")
            result = unload_item(username, passwd, itemName)
            cR(result)
        elif op == '7':
            itemName = iB("Item Name")
            result = item_wait_for_checking(username, passwd, itemName)
            cR(result)
        elif op == 'X':
            flag = False
        elif op == 'Q':
            u.exiting()
        else:
            info = "<Wrong Selection>"


def get_import_tax_rate(username, passwd, city, itemType):
    result = requests.get(urljoin(config['base'], '/company_manager/get_import_tax_rate/{0}/{1}'.format(city, itemType)), headers={
        'username': username,
        'password': passwd,
        'role': role
    })
    if result != "" :
        print("City: {0}, Import Tax Rate:{1}".format(city,result))
    else:
        print("Failed!")
    input("Press Enter to continue...")

def get_export_tax_rate(username, passwd, city, itemType):
    result = requests.get(urljoin(config['base'], '/company_manager/get_export_tax_rate/{0}/{1}'.format(city, itemType)), headers={
        'username': username,
        'password': passwd,
        'role': role
    })
    if result != "" :
        print("City: {0}, Export Tax Rate:{1}".format(city,result))
    else:
        print("Failed!")
    input("Press Enter to continue...")

def load_item_to_container(username,passwd,itemName,containerCode):
    result = requests.post(urljoin(config['base'], '/company_manager/load_item_to_container'), data ={
        'item_name': itemName,
        'container_code': containerCode
    }, headers={
        'username': username,
        'password': passwd,
        'role': role
    }).json()
    return result

def load_container_to_ship(username,passwd,shipName,containerCode):
    result = requests.post(urljoin(config['base'], '/company_manager/load_container_to_ship'), data ={
        'ship_name': shipName,
        'container_code': containerCode
    }, headers={
        'username': username,
        'password': passwd,
        'role': role
    }).json()
    return result


def ship_start_sailing(username,passwd,shipName):
    result = requests.post(urljoin(config['base'], '/company_manager/ship_start_sailing'), data ={
        'ship_name': shipName,
    }, headers={
        'username': username,
        'password': passwd,
        'role': role
    }).json()
    return result

def unload_item(username,passwd,item):
    result = requests.post(urljoin(config['base'], '/company_manager/unload_item'), data ={
        'item_name': item,
    }, headers={
        'username': username,
        'password': passwd,
        'role': role
    }).json()
    return result

def item_wait_for_checking(username,passwd,item):
    result = requests.post(urljoin(config['base'], '/company_manager/item_wait_for_checking'), data ={
        'item_name': item,
    }, headers={
        'username': username,
        'password': passwd,
        'role': role
    }).json()
    return result