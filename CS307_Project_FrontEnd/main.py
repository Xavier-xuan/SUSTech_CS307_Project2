from login import login
from courier import courier
from officer import officer
global username, passwd, role
while True:
    username, passwd, role = login()
    if role == "courier":
        courier(username, passwd)
    if role == "seaport officer":
        officer(username, passwd)