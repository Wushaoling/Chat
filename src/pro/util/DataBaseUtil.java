package pro.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import pro.model.Info;

public class DataBaseUtil {
	private static String info = null;
	private static Connection con = null;

	public static String connection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/db_chat", "root",
					"root");
			info = new String("数据库连接成功!");
		} catch (Exception e) {
			e.printStackTrace();
			info = new String("数据库连接失败!");
			closedatabases();
		}
		return info;
	}

	public static String closedatabases() {
		try {
			con.close();
			info = new String("数据库关闭成功");
		} catch (SQLException e) {
			info = new String("数据库关闭失败");
		}
		return info;
	}

	public static boolean checkLoginInfo(String id, String pwd) {
		try {
			connection();
			String sql = "select * from t_chat where account = ?";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, id);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				if(rs.getString(3).equals(pwd)){
					closedatabases();
					return true;
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			closedatabases();
			return false;
		}
		return false;
	}
	
	public static boolean checkRegisterInfo(String id) {
		try {
			connection();
			String sql = "select account from t_chat where account = ?";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, id);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				closedatabases();
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			closedatabases();
			return false;
		}
		return true;
	}
	
	public static boolean saveInfo(Info info) {
		try {
			String[] infos = info.getContent().split(",");
			connection();
			String sql = "insert into t_chat values(null,?,?,?,?,?)";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, info.getFromUser());
			ps.setString(2, infos[0]);
			ps.setString(3, infos[1]);
			ps.setString(4, new String(DateFormatUtil.getTime(new Date())));
			ps.setString(5, infos[2]);
			ps.executeUpdate();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			closedatabases();
			return false;
		}
	}
}
