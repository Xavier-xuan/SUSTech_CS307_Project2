
import util as u
import requests
from config import config
from urllib.parse import urljoin
from util import inputByType as iB
from util import checkResult as cR

global role
role = "sustc manager"
def sustcManager(username, passwd):
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
        print("|                   1. Get Company Count                   |")
        print("|                     2. Get City Count                    |")
        print("|                   3. Get Courier Count                   |")
        print("|                     4. Get Ship Count                    |")
        print("|                     5. Get Item Info                     |")
        print("|                     6. Get Ship Info                     |")
        print("|                   7. Get Container Info                  |")
        print("|                     8. Get Staff Info                    |")
        print("|----------------------------------------------------------|")
        print("|          X. Log Out                     Q. Quit          |")
        print("============================================================")
        print(info)
        print(">>> ", end="")
        op = input()
        if op == '1':
            company_count(username, passwd)
        elif op == '2':
            city_count(username, passwd)
        elif op == '3':
            courier_count(username, passwd)
        elif op == '4':
            ship_count(username, passwd)
        elif op == '5':
            itemName = iB("Item Name")
            item_info(username, passwd,itemName)
        elif op == '6':
            shipName = iB("Ship Name")
            ship_info(username, passwd, shipName)
        elif op == '7':
            containerCode = iB("Container Code")
            container_info(username, passwd, containerCode)
        elif op == '8':
            staffName = iB("Staff Name")
            staff_info(username, passwd, staffName)
        elif op == 'X':
            flag = False
        elif op == 'Q':
            u.exiting()
        else:
            info = "<Wrong Selection>"


def company_count(username, passwd):
    result = requests.get(urljoin(config['base'], '/sustc_manager/company_count'), headers={
        'username': username,
        'password': passwd,
        'role': role
    })
    if result != -1:
        print("Company Count: {0}".format(result))
    else:
        print("Failed!")
    input("Press Enter to continue...")
def city_count(username, passwd):
    result = requests.get(urljoin(config['base'], '/sustc_manager/city_count'), headers={
        'username': username,
        'password': passwd,
        'role': role
    })
    if result != -1:
        print("City Count: {0}".format(result))
    else:
        print("Failed!")
    input("Press Enter to continue...")
def courier_count(username, passwd):
    result = requests.get(urljoin(config['base'], '/sustc_manager/courier_count'), headers={
        'username': username,
        'password': passwd,
        'role': role
    })
    if result != -1:
        print("Courier Count: {0}".format(result))
    else:
        print("Failed!")
    input("Press Enter to continue...")
def ship_count(username, passwd):
    result = requests.get(urljoin(config['base'], '/sustc_manager/ship_count'), headers={
        'username': username,
        'password': passwd,
        'role': role
    })
    if result != -1:
        print("Ship Count: {0}".format(result))
    else:
        print("Failed!")
    input("Press Enter to continue...")
def item_info(username, passwd, itemName):
def ship_info(username, passwd, shipName):
def container_info(username, passwd, containerCode):
def staff_info(username, passwd, staffName):