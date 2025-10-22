package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import controller.ToolCtrl;
import database.KetNoiDatabase;
import entity.Thuoc;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ThuocDAO {
	
	ToolCtrl tool = new ToolCtrl();
	
	public ObservableList<Object[]> layDanhSachThuocChoKM(){
		ObservableList<Object[]> listThuoc = FXCollections.observableArrayList();
		String query = "select t.maThuoc, t.tenThuoc, t.phanLoai, dvt.tenDVT, cthd.donGia\r\n"
				+ "from [dbo].[DonViTinh] dvt JOIN [dbo].[Thuoc] t on dvt.maDVT = t.maDVT\r\n"
				+ "JOIN [dbo].[CT_HoaDon] cthd on t.maThuoc = cthd.maThuoc\r\n"
				+ "where t.trangThai = 1\r\n"
				+ "order by TRY_CAST(REPLACE(t.maThuoc, 'TTTH', '') as int)";
		
		try (Connection con = KetNoiDatabase.getConnection()) {
			Statement stmt = con.createStatement();
			ResultSet res = stmt.executeQuery(query);
			
			while(res.next()) {
				Object[] row = new Object[5];
				
				row[0] = res.getString("maThuoc");
	            row[1] = res.getString("tenThuoc");
	            row[2] = res.getString("phanLoai");
	            row[3] = res.getString("tenDVT");
	            row[4] = res.getDouble("donGia");
	            
	            listThuoc.add(row);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return listThuoc;
	}
}
