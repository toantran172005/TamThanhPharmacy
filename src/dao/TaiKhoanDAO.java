package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import connectDB.KetNoiDatabase;
import entity.NhanVien;
import entity.TaiKhoan;

public class TaiKhoanDAO {
	public NhanVienDAO nvDao = new NhanVienDAO();
	public TaiKhoan kiemTraDangNhap(String tenDN, String matKhau) {
	    String sql = """
	        SELECT *
	        FROM TaiKhoan
	        WHERE tenDangNhap = ?
	          AND matKhau = ?
	          AND trangThai = 1
	    """;

	    try (Connection con = KetNoiDatabase.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {

	        ps.setString(1, tenDN);
	        ps.setString(2, matKhau);

	        ResultSet rs = ps.executeQuery();
	        if (rs.next()) {
	        	 String maNV = rs.getString("maNV");
	             NhanVien nv = nvDao.timNhanVienTheoMa(maNV); 

	             return new TaiKhoan(
	                 rs.getString("maTK"),
	                 nv,
	                 rs.getString("tenDangNhap"),
	                 rs.getString("matKhau"),
	                 rs.getBoolean("trangThai"),
	                 rs.getString("loaiTK"),
	                 rs.getString("email")	            
	                 );
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return null;
	}
}
