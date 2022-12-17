import util as u

def login() :

    flag = True
    info = ""
    while flag:
        flag = False
        u.clear()
        print("============================================================")
        print("|                        [Login]                           |")
        print("|      Please Enter Numer To Select Your Role First        |")
        print("|----------------------------------------------------------|")
        print("|  1.Courier       2.Company Manager    3.Seaport Officer  |")
        print("|  4.SUSTCManger                        X.Exit             |")
        print("============================================================")
        if info != "":
            print(info)
        print(">>> ",end="")
        op = input();
        if op == '1':
            role = "Courier"
        elif op =='2':
            role = "Company Manager"
        elif op == '3':
            role = "Seaport Officer"
        elif op == '4':
            role = "SUSTCManger"
        else:
            info = ("<Wrong Selection>")
            flag = True
    u.clear()
    print("============================================================")
    print("|                        [Login]                           |")
    print("|        Please Enter Your Username and Password           |")
    print("============================================================")
    print(">>> Userbame: ", end="")
    username = input();
    print(">>> Password: ", end="")
    passwd = input();





