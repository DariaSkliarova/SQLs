import java.sql.*;
import java.util.Scanner;


public class Main {

    public static void main(String[] args) {
        Main main = new Main();
        main.exe();
    }

    private void exe() {

        Scanner scanner = new Scanner(System.in);
        System.out.println("""
                Введите цифрру от 1 до 5                
                1 - Создать таблицу
                2 - Удалить таблицу
                3 - Добавить новое поле
                4 - Показать отсортированную таблицу
                5 - Найти студента в таблице""");
        scanner.hasNextInt();
        while (true) {

            int userInput = scanner.nextInt();
            switch (userInput) {

                case 1:
                    System.out.println("Ваша таблица создается...");
                    createStatement();
                    break;
                case 2:
                    System.out.println("Ваша таблица удаляется...");
                    deleteStatement();
                    break;
                case 3:
                    System.out.println("Добавляем поле...");
                    System.out.println("Введите уникальный ID студента:");
                    scanner.nextLine();
                    String id = scanner.nextLine();
                    System.out.println("Введите имя студента:");
                    scanner.hasNextLine();
                    String name = scanner.nextLine();
                    System.out.println("Введите возраст студента:");
                    scanner.hasNextLine();
                    String age = scanner.nextLine();
                    System.out.println("Введите название группы студента:");
                    scanner.hasNextLine();
                    String grade = scanner.nextLine();

                    addField(id, name, age, grade);
                    break;
                case 4:
                    System.out.println("Сортируем таблицу...");
                    showSortedStatement();
                    break;
                case 5:
                    System.out.println("Введите имя студента для поиска:");
                    scanner.nextLine();
                    String piece = scanner.nextLine();
                    findStudents(piece);
                    break;

                default:
                    System.out.println("Вы ввели неверное значение");
            }
        }
    }

    private void createStatement() {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:sample.db")) {
            Statement statement = connection.createStatement();
            statement.executeUpdate("Create Table Statement(id Integer, name Varchar(20), " + "age Integer, grade Varchar(5))");
            System.out.println("Таблица создана.");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.out.println("Вы уже создали таблицу.");
        }
    }

    private void deleteStatement() {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:sample.db")) {
            Statement statement = connection.createStatement();
            statement.executeUpdate("Drop table Statement");
            System.out.println("Таблица удалена.");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.out.println("Нет таблицы для удаления.");
        }
    }

    private void addField(String id, String name, String age, String grade) {
        StringBuilder builder = new StringBuilder();
        String sql = builder.append("Insert into Statement Values(").append(id).append(", '").append(name).append("', ").append(age).append(", '").append(grade).append("')").toString();
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:sample.db")) {
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
            System.out.println("Поле добавлено в таблицу.");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.out.println("Что-то пошло не так...");
        }
    }

    private void showSortedStatement() {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:sample.db")) {
            Statement statement = connection.createStatement();
            ResultSet cursor = statement.executeQuery("Select id, name, age, grade From Statement" + " Order by Name Asc");
            while (cursor.next()) {
                int id = cursor.getInt("id");
                String name = cursor.getString("name");
                int age = cursor.getInt("age");
                String grade = cursor.getString("grade");

                System.out.println("Студент: " + name + " | Возраст: " + age + " | Группа: " + grade + " | ID: " + id);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.out.println("Что-то пошло не так...");
        }
    }

    private void findStudents(String piece) {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:sample.db")) {
            PreparedStatement statement = connection.prepareStatement("SELECT id, name, age, grade FROM Statement WHERE name = ?" + "Order by Name Asc");
            statement.setString(1, piece);
            ResultSet set = statement.executeQuery();
            while (set.next()) {
                int id = set.getInt("id");
                String name = set.getString("name");
                int age = set.getInt("age");
                String grade = set.getString("grade");

                System.out.println("Студент: " + name + " | Возраст: " + age + " | Группа: " + grade + " | ID: " + id);
            }
            System.out.println("Поиск выполнен.");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.out.println("Что-то пошло не так...");
        }
    }
}
