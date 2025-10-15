package dao;

import java.sql.Connection;

import database.KetNoiDatabase;
import entity.KhuyenMai;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;

public class KhuyenMaiDAO {
	public ObservableList<KhuyenMai> layDanhSachKM(){
		ObservableList<KhuyenMai> list = FXCollections.observableArrayList();
		
		String query = "select *\r\n"
				+ "from [dbo].[KhuyenMai]\r\n"
				+ "where [trangThai] = 1\r\n"
				+ "order by TRY_CAST(REPLACE([maKM], 'TTKM', '') as int)";
		
		try {
			(Connection con = KetNoiDatabase.getConnection();
			 
			)
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
