package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import controller.ToolCtrl;
import connectDB.KetNoiDatabase;
import entity.KhachHang;
import entity.NhaCungCap;

public class NhaCungCapDAO {
	
	private ToolCtrl tool = new ToolCtrl();
	
	public ArrayList<NhaCungCap> layListNCC() {
		ArrayList<NhaCungCap> list = new ArrayList<>();

		String query = """
				SELECT *
				FROM NhaCungCap
				ORDER BY
				    TRY_CAST(REPLACE(maNCC, 'TTNCC', '') AS INT);
				""";

		try (Connection con = KetNoiDatabase.getConnection();
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(query);) {

			while (rs.next()) {
				list.add(new NhaCungCap(rs.getString("maNCC"), rs.getString("tenNCC"), rs.getString("diaChi"), tool.chuyenSoDienThoai(rs.getString("sdt")), rs.getString("email")));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public NhaCungCap layNCC() {
		NhaCungCap ncc = new NhaCungCap();

		String query = """
				SELECT *
				FROM NhaCungCap
				WHERE maNCC = 'TTNCC1'
				""";

		try (Connection con = KetNoiDatabase.getConnection();
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(query);) {

			while (rs.next()) {
				ncc = new NhaCungCap(rs.getString("maNCC"), rs.getString("tenNCC"), rs.getString("diaChi"), tool.chuyenSoDienThoai(rs.getString("sdt")), rs.getString("email"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ncc;
	}
}
