import java.util.*;

class Kstack {
    int[] cost, top, next;
    int free_top, cap, k;

    Kstack(int k1, int n) {
        k = k1;
        cap = n;
        cost = new int[cap];
        top = new int[k];
        next = new int[cap];
        for (int i = 0; i < k; i++) {
            top[i] = -1;
        }
        free_top = 0;
        for (int i = 0; i < cap - 1; i++)
            next[i] = i + 1;
        next[cap - 1] = -1;
    }

    void push(int x, int tn) {
        int i = free_top;
        free_top = next[i];
        next[i] = top[tn];
        top[tn] = i;
        cost[i] = x;
    }

    int pop(int tn) {
        int i = top[tn];
        top[tn] = next[i];
        next[i] = free_top;
        free_top = i;
        return cost[i];
    }
}

public class Restaurant {
    static double[] code = {1.1, 1.2, 1.3, 1.4, 1.5, 2.1, 2.2, 2.3, 2.4, 2.5, 3.1, 3.2, 3.3, 3.4, 3.5, 4.1, 4.2, 4.3, 4.4, 4.5, 5.1, 5.2, 5.3, 5.4, 5.5, 6.1, 6.2, 6.3, 6.4, 6.5};
    static String[] dish = {"Veg.Tomato Soup", "Veg. Manchow Soup", "Veg. Clear Soup", "Sweet Corn Soup", "Carrot Tomato Soup", "Veg. Hakka Noodles", "Veg. Manchurian Dry", "Panner Chilly dry", "Veg. Crispy", "Mushroom Chilly", "Panner Tikka Masala", "Veg. Kolhapuri", "Kaju Curry", "Dal Tadaka", "Malai Kofta", "Veg. Pulao", "Curd Rice", "Jeera Rice", "Steam Rice", "Veg. Schezwan Rice", "Butter Nan", "Wheat Butter Roti", "Chapati", "Tandoori Roti", "Garlic Nan", "Hot Chocolate", "Cold Coffee", "Mix fruit Juice", "Tea", "Coke"};
    static int[] cost = {70, 70, 70, 80, 100, 140, 130, 170, 140, 160, 190, 140, 160, 120, 170, 150, 130, 110, 90, 150, 30, 20, 20, 20, 30, 30, 50, 30, 20, 40};
    int[] order_cost = new int[500];
    int[] order_quantity = new int[500];
    String[] order_dish = new String[500];
    int number = -1;
    String cust_name;

    Restaurant(int i, int q, String n) {
        this.cust_name = n;
        this.number++;
        this.order_cost[number] = cost[i];
        this.order_dish[number] = dish[i];
        this.order_quantity[number] = q;
    }

    void addOrder(int i, int q) {
        int position;
        for (int j = 0; j <= this.number; j++) {
            if (Objects.equals(order_dish[j], dish[i])) {
                position = j;
                this.order_quantity[position] += q;
                return;
            }
        }
        this.number++;
        this.order_cost[number] = cost[i];
        this.order_dish[number] = dish[i];
        this.order_quantity[number] = q;
    }

    void displayorder() {
        if (number > -1) {
            System.out.println("-----------------------------------------------------------------");
            System.out.format("%-30s %-30s %-6s%n", "Dish Name", "Cost per unit quantity", "Qty");
            for (int i = 0; i <= this.number; i++) {
                String format = "%-30s Rs. %-26d %-7d%n";
                System.out.format(format, order_dish[i], order_cost[i], order_quantity[i]);
            }
        } else
            System.out.print("\nNothing ordered at this table.");
    }

    static int search(double lcode) {
        int pos = -1;
        for (int i = 0; i < code.length; i++) {
            if (lcode == code[i])
                pos = i;
        }
        return pos;
    }

    static void displayMenu() {
        System.out.format("%-6s %-32s %-6s%n", "Code", "Dish Name", "Cost");
        for (int i = 0; i < code.length; i++)
            System.out.format("%-6s %-32s Rs. %-6s%n", code[i], dish[i], cost[i]);
    }

    static String centerString(int width, String s) {
        return String.format("%-" + width + "s", String.format("%" + (s.length() + (width - s.length()) / 2) + "s", s));
    }

    public static void main(String[] args) {
        Restaurant[] tblno = new Restaurant[10];
        Kstack s = new Kstack(10, 500);
        Scanner sc = new Scanner(System.in);
        System.out.println();
        System.out.println(centerString(44, "Menu"));
        displayMenu();
        int choice;
        do {
            System.out.print("\nEnter Table number to order: ");
            int table_no = sc.nextInt();
            table_no--;
            sc.nextLine();
            if (table_no < 10) {
                System.out.print("\nEnter Code Dish to order: ");
                double mcode1 = sc.nextDouble();
                int index = search(mcode1);
                if (index != -1) {
                    System.out.print("\nEnter Quantity: ");
                    int quant = sc.nextInt();
                    sc.nextLine();
                    s.push(cost[index] * quant, table_no);
                    if (tblno[table_no] == null) {
                        System.out.print("\nEnter Your Name: ");
                        String name = sc.nextLine();
                        tblno[table_no] = new Restaurant(index, quant, name);
                    } else if (tblno[table_no].number > -1) {
                        tblno[table_no].addOrder(index, quant);
                    }
                } else {
                    System.out.print("Wrong Code. Try Again!");
                }
            }
            System.out.print("\nEnter 1 to order more\nEnter 2 to show Bill\nEnter 3 to log off:");
            choice = sc.nextInt();
            if (choice == 2) {
                do {
                    System.out.print("\nEnter Table Number To Show the bill for: ");
                    int btable = sc.nextInt();
                    btable--;
                    if (tblno[btable] == null) {
                        System.out.println("No food ordered by this table!");
                    } else {
                        int tprt = btable + 1;
                        System.out.println("-----------------------------------------------------------------");
                        System.out.println("Bill for Table no. " + tprt);
                        System.out.println("Customer Name: " + tblno[btable].cust_name);
                        System.out.println("-----------------------------------------------------------------");
                        System.out.println(centerString(65, "Bill"));
                        tblno[btable].displayorder();
                        int sum = 0;
                        while (s.top[btable] != -1) {
                            sum += s.pop(btable);
                        }
                        System.out.format("%n%-30s Rs. %-8s%n", "Total", sum);
                        double tax = sum * 0.1;
                        System.out.format("%-30s Rs. %-6.2f%n", "Tax (5% CGST + 5% SGST)", tax);
                        System.out.format("%-30s Rs. %-6.2f%n", "Final Cost", (tax + sum));
                        System.out.println("\nThank you and visit again");
                        System.out.println("-----------------------------------------------------------------");
                        tblno[btable] = null;
                    }
                    System.out.print("\nEnter 1 to order more\nEnter 2 to display other bill\nEnter 3 to log off:");
                    choice = sc.nextInt();
                } while (choice == 2);
            }
        } while (choice != 3);
        System.out.println("Shutting Down");
    }
}