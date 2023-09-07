import java.util.Scanner;
import static java.util.Arrays.binarySearch;

public class Main {
    String vyr = new String();
    int numOperator;
    public static class BadValueException extends Exception {
        public BadValueException(String message) {
            super(message);
        }
    }
    public static void main(String[] args) throws BadValueException {
        Scanner sc = new Scanner(System.in);
        System.out.println("Введите выражение");
        String vyr = sc.next(); //Ввели выражение
        String[] sH = new String[3];
        String rezRim = new String();
        String[] rim = {"0", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X"};
        String[] rimTen = {"00", "X", "XX", "XXX", "XL", "L", "LX", "LXX", "LXXX", "XC", "C"};
        int rezol;
        int ind;
        int operCount;
        if (vyr.matches("[0-9,I.X.V.[-].+.*./]+")==false)
            throw new IllegalArgumentException("Вы ввели недопустимый(е) символ(ы)");
        if (    vyr.charAt(0) == '-' || vyr.charAt(0) == '+' ||
                vyr.charAt(0) == '*' || vyr.charAt(0) == '/')
            throw new IllegalArgumentException("Выражение не должно начинаться с оператора");
        if (    vyr.charAt(vyr.length() - 1) == '+' ||
                vyr.charAt(vyr.length() - 1) == '-' ||
                vyr.charAt(vyr.length() - 1) == '*' ||
                vyr.charAt(vyr.length() - 1) == '/')
            throw new IllegalArgumentException("Выражение не должно заканчиваться оператором");
        if (    (vyr.matches("[0-9,[-].+.*./]+") == false) &&
                (vyr.matches("[I.X.V.+.[-].*./]+") == false))
            throw new IllegalArgumentException("Одновременный ввод римских и арабских цифр невозможен");
        if (    vyr.charAt(0) == 0)
            throw new IllegalArgumentException("Выражение не должно начинаться с ноля");
        operCount = operNum(vyr);       //Уходим на подсчет операторов в выражении
        if (operCount == 0)
            throw new IllegalArgumentException("Отсутствует оператор");
        if (operCount > 1)
            throw new IllegalArgumentException("Оператор может быть только один");
        if (vyr.matches("[0-9,[-].+.*./]+")) // Исключаем все кроме араб и операторов
                {
                     ind = findNumberOper(vyr);  //Ищем индекс оператора
                     sH = shareVyr(vyr, ind);  //Делим строку на операторов и операндов
                     rezol = rezo(sH);  //Считаем
                     System.out.println("Ответ " + rezol);
        }
        else {
            if (vyr.matches("[I.X.V.+.[-].*./]+")) // Исключаем все, кроме рим и опер симв
                {
                    ind = findNumberOper(vyr);  //Ищем индекс оператора
                    sH = shareVyr(vyr, ind);  //Делим строку на операторов и операндов
                    rimToArab(sH, rim); //Переводим рим в араб
                    rezol = rezo(sH);  //Считаем
                if (rezol > 0) {
                    rezRim = arabToRim(rezol, rim, rimTen); //Переводим араб в рим
                    System.out.println("Римский ответ " + rezRim);
                } else {
                    throw new BadValueException ("Римские числа д.б. положительные");
                }
            } else {
                System.out.println("Какая-то неизвестная ошибка");
                System.exit(1);
            }
        }
    }
    public static int operNum(String vyr) //Считаем количество операторов, определяем индекс оператора
    {
        int counter = 0;
        for (int i = 1; i < (vyr.length() - 1); i++) {
            if (vyr.charAt(i) == '-' || vyr.charAt(i) == '+' || vyr.charAt(i) == '*' || vyr.charAt(i) == '/')
                counter = counter + 1;
        }
        return (counter);
    }
    public static int findNumberOper(String vyr) //Ищем индекс оператора
    {
        int counter = 0;
        for (int i = 1; i < (vyr.length() - 1); i++) {
            if (vyr.charAt(i) == '-' | vyr.charAt(i) == '+' | vyr.charAt(i) == '*' | vyr.charAt(i) == '/')
                counter = i + 1;
        }
        return counter;
    }
    public static String[] shareVyr(String vyr, int ind)  //Массив (число, оператор, число)
    {
        StringBuilder vyr1 = new StringBuilder(vyr);
        vyr1.insert(ind, " ");
        vyr1.insert(ind - 1, " ");
//                System.out.println(vyr1);
        String vyr2 = vyr1.toString();
        String[] vyrParts = vyr2.split(" ");
//                System.out.println(vyrParts[0]);
//                System.out.println(vyrParts[1]);
//                System.out.println(vyrParts[2]);
        return (vyrParts);
    }
    public static int rezo(String[] vyrParts)  //Сравнить числа c маской и посчитать
    {
        int a = Integer.parseInt(vyrParts[0]);
        int b = Integer.parseInt(vyrParts[2]);
        int x = 0;
        if (a > 0 && a < 11 && b > 0 && b < 11)
            switch (vyrParts[1]) {
                case "+":
                    x = a + b;
                    break;
                case "-":
                    x = a - b;
                    break;
                case "*":
                    x = a * b;
                    break;
                case "/":
                    x = a / b;
                    break;
            }

        else {
            throw new IllegalArgumentException("Операнд(ы) вне диапазона калькулятора");
        }
        return (x);
    }
    public static String[] rimToArab(String vyrParts[], String rim[]) {
        int a = 0;
        int b = 0;
        for (int i = 1; i <= 10; i++) {
            if (rim[i].equals(vyrParts[0]))
                a = i;
        }
        for (int j = 1; j <= 10; j++) {
            if (vyrParts[2].equals(rim[j]))
                b = j;
        }
        if (a > 0 && b > 0) {
            vyrParts[0] = Integer.toString(a);
            vyrParts[2] = Integer.toString(b);
            return (vyrParts);
        } else {
            throw new IllegalArgumentException("Введены ошибочные римские числа");
        }
    }
    public static String arabToRim(int rezol, String rim[], String rimTen[]) {
        String rez = new String();
        if (rezol < 11)
            rez = rim[rezol];
        else {
            int rezolTen = rezol / 10;
            int rezolEd = rezol - rezolTen * 10;
            if (rezolEd > 0)
                rez = rimTen[rezolTen] + rim[rezolEd];
            else rez = rimTen[rezolTen];
        }
        return (rez);
    }
}