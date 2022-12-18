import os
import platform
import requests


def clear():
    if (platform.system() == "Windows"):
        os.system("cls")
    else:
        os.system("clear")