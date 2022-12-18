from login import login
from courier import courier
username, password, role = login()

if role == "courier":
    courier(username, password)