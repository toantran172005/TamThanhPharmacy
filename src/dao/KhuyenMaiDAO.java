package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import controller.ToolCtrl;
import database.KetNoiDatabase;
import entity.KhuyenMai;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;

public class KhuyenMaiDAO {
	
	ToolCtrl tool = new ToolCtrl();
	
	public ObservableList<KhuyenMai> layDanhSachKM(){
		ObservableList<KhuyenMai> list = FXCollections.observableArrayList();
		
		String query = "select *\r\n"
				+ "from [dbo].[KhuyenMai]\r\n"
				+ "where [trangThai] = 1\r\n"
				+ "order by TRY_CAST(REPLACE([maKM], 'TTKM', '') as int)";
		
		try (Connection con = KetNoiDatabase.getConnection(); 
				Statement stmt = con.createStatement();
				ResultSet res = stmt.executeQuery(query);)
		{
			while(res.next()) {
				list.add(new KhuyenMai(res.getString("maKM"), res.getString("tenKM"), res.getString("phuongThucKM"), res.getInt("mucKM"), tool.sqlDateSangLocalDate(res.getDate("ngayBD")), tool.sqlDateSangLocalDate(res.getDate("ngayKT")), res.getBoolean("trangThai")));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}
}
