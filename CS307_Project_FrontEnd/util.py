import os
import platform
import sys
import time

def clear():
    if (platform.system() == "Windows"):
        os.system("cls")
    else:
        os.system("clear")

def inputByType(type):
    print("Please Enter the %s:"%type)
    print(">>> ", end="")
    return input();

def checkResult(result):
    if result:
        print("Success!")
    else:
        print("Failed!")
    time.sleep(1)
def exit():
    print("Exiting...")
    time.sleep(1)
    sys.exit()