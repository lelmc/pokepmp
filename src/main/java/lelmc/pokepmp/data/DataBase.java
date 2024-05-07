package lelmc.pokepmp.data;

import lelmc.pokepmp.ranking.Data;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.service.sql.SqlService;

import javax.sql.DataSource;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static lelmc.pokepmp.ranking.pmp.BanList;

public class DataBase {
    private static final String tableName = "pokepmp";
    private static DataSource dataSource;

    public static void setDB(final File file) {
        File DATABASE = new File(file, "data");
        String jdbcURL = "jdbc:h2:" + DATABASE;
        SqlService sqlManager = Sponge.getServiceManager().provide(SqlService.class).get();
        try {
            dataSource = sqlManager.getDataSource(jdbcURL);
        } catch (SQLException e) {
            e.fillInStackTrace();
        }
        createTable();
        banTable();
    }

    public static void createTable() {
        String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + tableName +
                " (`uuid` VARCHAR (40) PRIMARY KEY, `name` VARCHAR(16), `score` INTEGER, `winner` INTEGER,`loser` INTEGER,`isBan` INTEGER)";
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(CREATE_TABLE)
        ) {
            statement.execute();
        } catch (SQLException e) {
            e.fillInStackTrace();
        }
    }

    public static void banTable() {
        String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS BAN (`uuid` VARCHAR (40) PRIMARY KEY)";
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(CREATE_TABLE)
        ) {
            statement.execute();
        } catch (SQLException e) {
            e.fillInStackTrace();
        }
    }

    public static void getBan() {
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement("select * FROM BAN")
        ) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String uuid = resultSet.getString("uuid");
                BanList.add(UUID.fromString(uuid));
            }
            statement.execute();
        } catch (SQLException e) {
            e.fillInStackTrace();
        }
    }

    public static void UpBan(String uuid) {
        String Up_Account = "INSERT INTO BAN (`uuid`) VALUES ('" + uuid + "');";
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(Up_Account)
        ) {
            statement.execute();
        } catch (SQLException e) {
            e.fillInStackTrace();
        }
    }

    //设置玩家积分
    public static void UpScore(UUID uuid, int score) {
        String Up_Account = "update " + tableName + " SET score='" + score + "' where uuid = '" + uuid + "';";
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(Up_Account)
        ) {
            statement.execute();
        } catch (SQLException e) {
            e.fillInStackTrace();
        }
    }

    public static void UpAccount(UUID uuid, int score, int winner, int loser) {
        if (score < 1) {
            score = 0;
        }
        String Up_Account = "update " + tableName + " SET score='" + score + "',winner='" + winner + "',loser='" + loser + "' where uuid = '" + uuid + "';";
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(Up_Account)
        ) {
            statement.execute();
        } catch (SQLException e) {
            e.fillInStackTrace();
        }
    }

    public static void createAcc(UUID uuid, String name, int score, int winner, int loser) {
        String CREATE_TABLE = "INSERT INTO " + tableName + " (`uuid`, `name`, `score`, `winner`, `loser`) VALUES (?, ?, ?, ?, ?)";
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(CREATE_TABLE)
        ) {
            statement.setString(1, uuid.toString());
            statement.setString(2, name);
            statement.setInt(3, score);
            statement.setInt(4, winner);
            statement.setInt(5, loser);
            statement.execute();
        } catch (SQLException e) {
            e.fillInStackTrace();
        }
    }

    public static Data getAccount(UUID uuid) {
        Data data = new Data();
        String get_Account = "SELECT `name`, `score`, `winner`, `loser` FROM " + tableName + " WHERE uuid = ?";
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(get_Account)
        ) {
            statement.setString(1, uuid.toString());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                data.setID(resultSet.getString("name"));
                data.setScore(resultSet.getInt("score"));
                data.setWinne(resultSet.getInt("winner"));
                data.setLoser(resultSet.getInt("loser"));
            }
            statement.execute();
        } catch (SQLException e) {
            e.fillInStackTrace();
        }
        return data;
    }

    public static List<Data> getTop() {
        List<Data> p = new ArrayList<>();
        String TOP_SQL = "SELECT * FROM " + tableName + " ORDER BY score DESC";
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(TOP_SQL)
        ) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String id = resultSet.getString("name");
                int score = resultSet.getInt("score");
                int win = resultSet.getInt("winner");
                int los = resultSet.getInt("loser");
                p.add(new Data(id, score, win, los));
            }
            statement.execute();
        } catch (SQLException e) {
            e.fillInStackTrace();
        }
        return p;
    }

    public static void clear() {
        String clear_SQL = "TRUNCATE TABLE " + tableName;
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(clear_SQL)
        ) {
            statement.execute();
        } catch (SQLException e) {
            e.fillInStackTrace();
        }
    }

    public static void clearBan() {
        String clear_SQL = "TRUNCATE TABLE BAN;";
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(clear_SQL)
        ) {
            statement.execute();
        } catch (SQLException e) {
            e.fillInStackTrace();
        }
    }
}

