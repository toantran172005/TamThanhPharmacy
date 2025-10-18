package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import database.KetNoiDatabase;
import entity.KeThuoc;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class KeThuocDAO {

	public ObservableList<KeThuoc> layListKeThuoc() {
		ObservableList<KeThuoc> list = FXCollections.observableArrayList();

		String query = "SELECT [maKe]\r\n" + "      ,[loaiKe]\r\n" + "      ,[sucChua]\r\n" + "      ,[moTa]\r\n"
				+ "      ,[trangThai]\r\n" + "  FROM KeThuoc\r\n" + "";
		try (Connection con = KetNoiDatabase.getConnection();
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(query);) {

			while (rs.next()) {
				list.add(new KeThuoc(rs.getString("maKe"), rs.getString("loaiKe"), rs.getInt("sucChua"),
						rs.getString("moTa"), rs.getBoolean("trangThai")));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

}
