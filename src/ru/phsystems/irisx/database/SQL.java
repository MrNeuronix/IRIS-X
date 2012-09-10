package ru.phsystems.irisx.database;

/**
 * IRIS-X Project
 * Author: Nikolay A. Viguro
 * WWW: smart.ph-systems.ru
 * E-Mail: nv@ph-systems.ru
 * Date: 10.09.12
 * Time: 13:27
 */

import java.sql.*;

public class SQL {

    private Connection connection = null;

    public SQL() throws SQLException {

        // Загружаем класс драйвера
        try {
            Class.forName("org.hsqldb.jdbcDriver");
        } catch (ClassNotFoundException e) {
            System.err.println("НЕ удалось загрузить драйвер ДБ.");
            e.printStackTrace();
            System.exit(1);
        }

        // Cоздаем соединение, здесь dbpath это путь к папке где будут хранится
        // файлы БД. dbname имя базы данных. SA это имя пользователя который
        // создается автоматически при создании БД пароль для него пустой. Если
        // такой базы данных нет она будет автоматически создана.

        try {
            connection = DriverManager.getConnection(
                    "jdbc:hsqldb:file:./sql/database", "SA", "");
        } catch (SQLException e) {
            System.err.println("[sql] Cant open connection");
            e.printStackTrace();
            System.exit(1);
        }
    }

    // Выполнения произвольного запроса

    public boolean doQuery(String sql) {
        try {
            Statement statement = connection.createStatement();
            // создаем таблицу со столбцами id и value.
            try {
                statement.executeUpdate(sql);
            } catch (SQLException e) {
                // если таблица создана, будет исключение, игнорируем его.
                //в реальных проектах так не делают
            }
            statement.close();
        } catch (SQLException e1) {
            return false;
        }
        return true;
    }

    // Метод вытаскивания данных

    public ResultSet select(String sql) {
        ResultSet resultSet = null;

        try {
            Statement statement = connection.createStatement();
            try {
                resultSet = statement.executeQuery(sql);
            } catch (SQLException e) {
            }
            statement.close();
        } catch (SQLException e1) {
        }
        return resultSet;
    }

    // Метод отключения от БД

    public void doDisconnect() throws SQLException {
        Statement statement = null;
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        try {
            statement.execute("SHUTDOWN");
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        statement.close();
    }
}
