import util as u
import requests
from config import config
from urllib.parse import urljoin
from util import inputByType as iB

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
            item_info(username, passwd, itemName)
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
    }).json()
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
    }).json()
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
    }).json()
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
    }).json()
    if result != -1:
        print("Ship Count: {0}".format(result))
    else:
        print("Failed!")
    input("Press Enter to continue...")


def item_info(username, passwd, itemName):
    result = requests.get(urljoin(config['base'], '/sustc_manager/item_info/{}'.format(itemName)), headers={
        'username': username,
        'password': passwd,
        'role': role
    }).json()
    if result is not None:
        print("Name:\t{1}\n"
              "Class:\t{2}\n"
              "Price:\t{3}\n"
              "State:\t{4}\n"
              "Retrieval City:\t{5}\n"
              "Retrieval Courier:\t{6}\n"
              "Delivery City:\t{7}\n"
              "Delivery Courier:\t{8}\n"
              "Export City:\t{9}\n"
              "Export Officer:\t{10}\n"
              "Export Tax:\t{11}\n"
              "Import City:\t{12}\n"
              "Import Officer:\t{13}\n"
              "Import Tax:\t{14}\n".format(
                None,
                result['name'],
                result['$class'],
                result['price'],
                result['state'],
                result['retrieval']['city'],
                result['retrieval']['courier'],
                result['delivery']['city'],
                result['delivery']['courier'],
                result['export']['city'],
                result['export']['officer'],
                result['export']['tax'],
                result['$import']['city'],
                result['$import']['officer'],
                result['$import']['tax']
              )
              )
    else:
        print("Failed!")
    input("Press Enter to continue...")


def ship_info(username, passwd, shipName):
    result = requests.get(urljoin(config['base'], '/sustc_manager/ship_info/{}'.format(shipName)), headers={
        'username': username,
        'password': passwd,
        'role': role
    }).json()
    if result is not None:
        print("Name:\t{0}\n"
              "Owner:\t{1}\n"
              "Sailing:\t{2}\n".format(
                result['name'],
                result['owner'],
                result['sailing']))
    else:
        print("Failed!")
    input("Press Enter to continue...")


def container_info(username, passwd, containerCode):
    result = requests.get(urljoin(config['base'], '/sustc_manager/container_info/{}'.format(containerCode)), headers={
        'username': username,
        'password': passwd,
        'role': role
    }).json()
    if result is not None:
        print("Code:\t{0}\n"
              "Type:\t{1}\n"
              "Using:\t{2}\n".format(result['code'], result['type'], result['using']))
    else:
        print("Failed!")
    input("Press Enter to continue...")


def staff_info(username, passwd, staffName):
    result = requests.get(urljoin(config['base'], '/sustc_manager/staff_info/{}'.format(staffName)), headers={
        'username': username,
        'password': passwd,
        'role': role
    }).json()
    if result is not None:
        print(result)
        print("Username:\t{}\n"
              "Role:\t{}\n"
              "City:\t{}\n"
              "Is Female:\t{}\n"
              "Age:\t{}\n"
              "Phone Number:\t{}\n".format(
                result['basicInfo']['name'],
                result['basicInfo']['type'],
                result['city'],
                result['isFemale'],
                result['age'],
                result['phoneNumber']))
    else:
        print("Failed!")
    input("Press Enter to continue...")
