from login import login
from courier import courier
from officer import officer
from companyManager import companyManager
from sustcManager import sustcManager
global username, passwd, role

while True:
    username, passwd, role, success= login()
    if (success):
        if role == "courier":
            courier(username, passwd)
        elif role == "company manager":
            companyManager(username, passwd)
        elif role == "seaport officer":
            officer(username, passwd)
        elif role == "sustc officer":
            sustcManager(username, passwd)
    else:
        continue
