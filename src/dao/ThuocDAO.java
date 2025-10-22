package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

import controller.ToolCtrl;
import database.KetNoiDatabase;
import entity.DonViTinh;
import entity.KeThuoc;
import entity.KhachHang;
import entity.NhaCungCap;
import entity.NhanVien;
import entity.Thue;
import entity.Thuoc;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ThuocDAO {

	ToolCtrl tool = new ToolCtrl();

	public ObservableList<Object[]> layDanhSachThuocChoKM() {
		ObservableList<Object[]> listThuoc = FXCollections.observableArrayList();
		String query = "select t.maThuoc, t.tenThuoc, t.phanLoai, dvt.tenDVT, cthd.donGia\r\n"
				+ "from [dbo].[DonViTinh] dvt JOIN [dbo].[Thuoc] t on dvt.maDVT = t.maDVT\r\n"
				+ "JOIN [dbo].[CT_HoaDon] cthd on t.maThuoc = cthd.maThuoc\r\n" + "where t.trangThai = 1\r\n"
				+ "order by TRY_CAST(REPLACE(t.maThuoc, 'TTTH', '') as int)";

		try (Connection con = KetNoiDatabase.getConnection()) {
			Statement stmt = con.createStatement();
			ResultSet res = stmt.executeQuery(query);

			while (res.next()) {
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
	
	public ObservableList<Thuoc> layListThuoc() {
	    ObservableList<Thuoc> listThuoc = FXCollections.observableArrayList();

	    String sql = """
	            SELECT maThuoc, maThue, maNCC, maKe, tenThuoc, dangThuoc, phanLoai,
	                   giaBan, hanSuDung, trangThai, anh, maDVT
	            FROM Thuoc
	            ORDER BY TRY_CAST(SUBSTRING(maThuoc, 5, LEN(maThuoc)) AS INT)
	        """;

	    try (Connection con = KetNoiDatabase.getConnection();
	         Statement stmt = con.createStatement();
	         ResultSet rs = stmt.executeQuery(sql)) {

	        while (rs.next()) {
	            // Khởi tạo các entity con (chỉ cần mã, vì các bảng phụ có thể join sau)
	            Thue thue = new Thue(rs.getString("maThue"));
	            KeThuoc keThuoc = new KeThuoc(rs.getString("maKe"));
	            DonViTinh dvt = new DonViTinh(rs.getString("maDVT"));
	            NhaCungCap ncc = new NhaCungCap(rs.getString("maNCC"));

	            // Lấy dữ liệu chính của Thuoc
	            String maThuoc = rs.getString("maThuoc");
	            String tenThuoc = rs.getString("tenThuoc");
	            String dangThuoc = rs.getString("dangThuoc");
	            String phanLoai = rs.getString("phanLoai");
	            float giaBan = rs.getFloat("giaBan");
	            LocalDate hanSuDung = rs.getDate("hanSuDung").toLocalDate();
	            Boolean trangThai = rs.getBoolean("trangThai");
	            String anh = rs.getString("anh");

	            // Tạo đối tượng Thuoc
	            Thuoc thuoc = new Thuoc(
	                maThuoc, thue, keThuoc, dvt, ncc,
	                tenThuoc, dangThuoc, phanLoai,
	                giaBan, hanSuDung, trangThai, anh
	            );

	            listThuoc.add(thuoc);
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return listThuoc;
	}


	// ========== TÌM KHÁCH HÀNG THEO MÃ ==========
	public Thuoc timThuocTheoMa(String maThuoc) {
		ObservableList<Thuoc> listThuoc = layListThuoc();
		for (Thuoc thuoc : listThuoc) {
			if (thuoc.getMaThuoc().equalsIgnoreCase(maThuoc))
				return thuoc;
		}
		return null;
	}
}
