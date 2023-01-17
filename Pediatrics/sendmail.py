import smtplib
import sqlite3
from click import command
from tabulate import tabulate
import tkinter.messagebox as mb
from tkinter import *
db = sqlite3.connect("version1.db")
cursor = db.cursor()
vacc_name = ['BCG', 'OPV', 'IPV', 'PENTAVALENT', 'MEASLES1', 'VITAMINA', 'DPT']
root = Tk()
root.title('Admin login')
root.geometry("450x250")


def submitted():
    global user_strvar, pass_strvar, id_intvar
    admin = user_strvar.get()
    password = pass_strvar.get()
    if admin == "adminlog":
        if password == "admin@1234":
            Top = Toplevel()
            Top.title('Schedule generator')
            Top.geometry("250x250")
            Label(Top, text=" Enter Client id:- \n", font=("Times New Roman", 11)).pack()
            id_input = Entry(Top, width=30, borderwidth=5,textvariable= id_intvar).pack()
            button1 = Button(Top, text="Submit", font=("Times New Roman", 11), padx=40, fg="#150734", bg="#CAE7DF",
                             command=send_mail).pack()
        else:
            mb.showerror('Error!', "Incorrect password !!")
    else:
        mb.showerror('Error!', "Incorrect User ID!!")


def send_mail():
        global id_intvar
        id_input = id_intvar.get()
        extract_email = '''Select email from account where client_id=?'''
        cursor.execute(extract_email, (id_input,))
        temp_email = cursor.fetchall()
        final_email = temp_email[0][0]
        extarct_data = '''Select * from vaccine_date where vaccine_id=? '''
        cursor.execute(extarct_data, (id_input,))
        temp_data = cursor.fetchall()
        date = []
        for i in temp_data:
            date.append(i)
        final_date = []
        for i in range(1, 8):
            temp = date[0][i]
            final_date.append(temp)
        table = []
        for i in range(len(vacc_name)):
            table.append([vacc_name[i], final_date[i]])
        result = tabulate(table, headers=["Vaccine Name", "Scheduled Date"])
        print(result)

        server = smtplib.SMTP('smtp.gmail.com', 587)

        server.ehlo()
        server.starttls()
        server.ehlo()
        server.login('debugcoder472@gmail.com', 'bkenstdtjanmglye')

        subject = ' VACCINE SCHEDULE '
        body = result

        msg = f'Subject: {subject}\n\n {body}'

        server.sendmail('debugcoder472@gmail.com', final_email, msg)
        mb.showinfo('Email sent', f"Email sent to {final_email}")



user_strvar = StringVar()
pass_strvar = StringVar()
id_intvar = IntVar()
Label(root, text=" Enter Admin details:- \n", font=("Times New Roman", 11)).grid(row=0, column=0)
Label(root, text="User id ", font=("", 10)).grid(row=1, column=0)
Label(root, text="Password ", font=("", 10)).grid(row=2, column=0)
admin = Entry(root, width=30,textvariable=user_strvar, borderwidth=5)
admin.grid(row=1, column=1, pady=5)
password = Entry(root, width=30, textvariable=pass_strvar,borderwidth=5)
password.grid(row=2, column=1, pady=5)
button = Button(root, text="Submit", font=("Times New Roman", 11), padx=40, fg="#150734", bg="#CAE7DF",command=submitted)
button.grid(row=5, column=1, pady=7)
root.mainloop()

cursor.close()
db.close()
