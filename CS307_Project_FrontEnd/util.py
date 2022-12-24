import os
import platform
import time


def clear():
    if platform.system() == "Windows":
        os.system("cls")
    else:
        os.system("clear")


def inputByType(itemType):
    print("Please Enter the %s:" % itemType)
    print(">>> ", end="")
    return input()


def checkResult(result):
    if result:
        print("Success!")
    else:
        print("Failed!")
    time.sleep(1)


def exiting():
    print("Exiting...")
    time.sleep(1)
    exit()
