import sqlite3
from tkinter import *
from tkcalendar import DateEntry
import tkinter.messagebox as mb


def click():
    mb.showinfo('Record added', f"Record of {cname.get()} was successfully added")
    db = sqlite3.connect("version1.db", detect_types=sqlite3.PARSE_DECLTYPES)
    cursor = db.cursor()
    vacc_name = ['BCG', 'OPV', 'IPV', 'PENTAVALENT', 'MEASLES1', 'VITAMINA', 'DPT']

    update_sql = "INSERT INTO account(p_name,c_name,c_dob,email) VALUES(?,?,?,?)"
    cursor.execute(update_sql, (pname.get(), cname.get(), dob.get(), emailid.get(),))
    cursor.connection.commit()
    final_list = []
    for name in vacc_name:
        cal_date = '''SELECT date(?,(SELECT vacc_due FROM vaccine_master WHERE vacc_name = ?))'''
        for i in cursor.execute(cal_date, (dob.get(), name)):
            date = str(i[0])
            final_list.append(date)
    temp_tuple = (final_list[0],)
    t = list(temp_tuple)
    final_list.pop(0)
    for x in final_list:
        t.append(x)
    final_tuple = tuple(t)
    temp_list = []
    temp_list.append(final_tuple)

    for x in temp_list:
        cursor.execute("""INSERT INTO vaccine_date(BCG , OPV , IPV , PENTAVALENT , MEASLES1 , VITAMINA , DPT )
         VALUES (?, ?, ?,?,?,?,?)""", x)

    cursor.connection.commit()
    cursor.close()
    db.close()
    pname.delete(0, END)
    cname.delete(0, END)
    dob.delete(0, END)
    emailid.delete(0, END)


root = Tk()
root.title('Vaccination Portal')
root.geometry("450x250")
Label(root, text=" Enter following details:- \n", font=("Times New Roman", 11)).grid(row=0, column=0)
Label(root, text="Child's name ", font=("", 10)).grid(row=1, column=0)
Label(root, text="Parent's name ", font=("", 10)).grid(row=2, column=0)
Label(root, text="Email id ", font=("", 10)).grid(row=3, column=0)
Label(root, text="Child's DOB ", font=("", 10)).grid(row=4, column=0)


cname = Entry(root, width=30, borderwidth=5)
cname.grid(row=1, column=1, pady=5)
pname = Entry(root, width=30, borderwidth=5)
pname.grid(row=2, column=1, pady=5)
emailid = Entry(root, width=30, borderwidth=5)
emailid.grid(row=3, column=1, pady=5)
dob = DateEntry(root, width=20, borderwidth=2, selectmode='day', date_pattern='yyyy-MM-dd')
dob.grid(row=4, column=1, pady=5)

button = Button(root, text="Submit", font=("Times New Roman", 11), padx=40, fg="#150734", bg="#CAE7DF",
                command=click)
button.grid(row=5, column=1, pady=7)
root.mainloop()