package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import database.KetNoiDatabase;
import entity.Thue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ThueDAO {
	public ObservableList<Thue> listThue = FXCollections.observableArrayList();;

	public ObservableList<Thue> layListThue() {
		listThue.clear();

		try (Connection con = KetNoiDatabase.getConnection();
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(
						"SELECT * FROM Thue ")) {

			while (rs.next()) {

				Thue thue = new Thue(
						rs.getString("maThue"), 
						rs.getString("loaiThue"),
						rs.getDouble("tyLeThue"),
						rs.getString("moTa")
						
						);
						
				listThue.add(thue);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return listThue;
	}
}
