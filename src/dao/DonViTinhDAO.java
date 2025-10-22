package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import database.KetNoiDatabase;
import entity.DonViTinh;
import entity.KhachHang;
import entity.NhanVien;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DonViTinhDAO {
	public ObservableList<DonViTinh> layListDVT(){
		ObservableList<DonViTinh> listDVT = FXCollections.observableArrayList();
		
		String sql = """
			    SELECT *
				FROM DonViTinh dvt
				ORDER BY TRY_CAST(SUBSTRING(dvt.maDVT, 5, LEN(dvt.maDVT)) AS INT)
			""";

	try (Connection con = KetNoiDatabase.getConnection();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql)) {

		while (rs.next()) {
			DonViTinh dvt = new DonViTinh(rs.getString("maDVT"), rs.getString("tenDVT"));
			listDVT.add(dvt);
		}

	} catch (SQLException e) {
		e.printStackTrace();
	}

	return listDVT;
	}
	
}
