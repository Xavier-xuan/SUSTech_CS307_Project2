from login import login
from courier import courier
global username, passwd, role
while True:
    username, passwd, role = login()
    if role == "courier":
        courier(username, passwd)
