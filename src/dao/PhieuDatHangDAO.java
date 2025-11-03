package dao;

import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

import controller.ToolCtrl;
import entity.KhachHang;
import entity.NhanVien;
import entity.PhieuDatHang;
import connectDB.KetNoiDatabase;

public class PhieuDatHangDAO {
	private ToolCtrl toolCtrl = new ToolCtrl();
	NhanVienDAO nvDAO = new NhanVienDAO();
	KhachHangDAO khDAO = new KhachHangDAO();
	ThuocDAO thuocDAO = new ThuocDAO();

	public boolean capNhatMaHDChoPhieuDatHang(String maPDH, String maHD) {
		String sql = "UPDATE PhieuDatHang SET maHD = ? WHERE maPDH = ?";
		try (Connection con = KetNoiDatabase.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, maHD);
			ps.setString(2, maPDH);

			return ps.executeUpdate() > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public int taoHoaDonVaChiTiet(String maHD, String maKH, String maNV, String loaiTT, LocalDate ngayLap,
			String diaChiHT, String tenHT, String ghiChu, String hotline, double tienNhan, boolean trangThai,
			List<Object[]> dsChiTiet) {
		String sqlHoaDon = "INSERT INTO HoaDon (maHD, maKH, maNV, loaiTT, ngayLap, diaChiHT, tenHT, ghiChu, hotline, tienNhan, trangThai) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		String sqlCT = "INSERT INTO CT_HoaDon (maHD, maThuoc, soLuong, donGia, maDVT) VALUES (?, ?, ?, ?, ?)";

		try (Connection con = KetNoiDatabase.getConnection()) {
			con.setAutoCommit(false);

			// Insert vào bảng HoaDon
			try (PreparedStatement psHD = con.prepareStatement(sqlHoaDon)) {
				psHD.setString(1, maHD);
				psHD.setString(2, maKH);
				psHD.setString(3, maNV);
				psHD.setString(4, loaiTT);
				psHD.setDate(5, java.sql.Date.valueOf(ngayLap));
				psHD.setString(6, diaChiHT);
				psHD.setString(7, tenHT);
				psHD.setString(8, ghiChu);
				psHD.setString(9, hotline);
				psHD.setDouble(10, tienNhan);
				psHD.setBoolean(11, trangThai);
				psHD.executeUpdate();
			}

			// Insert chi tiết hóa đơn
			try (PreparedStatement psCT = con.prepareStatement(sqlCT)) {
				for (Object[] ct : dsChiTiet) {
					psCT.setString(1, maHD);
					psCT.setString(2, (String) ct[0]); // maThuoc
					psCT.setInt(3, (int) ct[1]); // soLuong
					psCT.setDouble(4, (double) ct[2]); // donGia
					psCT.setString(5, (String) ct[3]); // maDVT
					psCT.addBatch();
				}
				psCT.executeBatch();
			}

			con.commit();
			return 1; // thành công

		} catch (SQLException e) {
			e.printStackTrace();
			try {
				// rollback nếu lỗi
				Connection con = KetNoiDatabase.getConnection();
				if (con != null)
					con.rollback();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
			return -1; // thất bại
		}
	}

	/**
	 * @return 1 = Thành công 0 = Không đủ tồn kho -1 = Lỗi SQL hoặc lỗi khác
	 */
	public int taoPhieuDatHangVaChiTiet(String maPDH, String maKH, String maNV, Date ngayDat, Date ngayHen,
			String ghiChu, List<Object[]> dsChiTiet) {

		String sqlPhieu = """
				INSERT INTO PhieuDatHang
				(maPDH, maHD, maKH, maNV, ngayDat, ngayHen, ghiChu, trangThai, diaChiHT, tenHT, hotline)
				VALUES (?, NULL, ?, ?, ?, ?, ?, N'Chờ hàng', ?, ?, ?)
				""";

		String sqlCheckKho = "SELECT soLuongTon FROM CT_Kho WHERE maThuoc = ?";
		String sqlInsertCT = "INSERT INTO CT_PhieuDatHang (maPDH, maThuoc, soLuong, maDVT, donGia) VALUES (?, ?, ?, ?, ?)";
		String sqlUpdateKho = "UPDATE CT_Kho SET soLuongTon = soLuongTon - ? WHERE maThuoc = ?";

		try (Connection con = KetNoiDatabase.getConnection()) {
			con.setAutoCommit(false);

			try (PreparedStatement psPhieu = con.prepareStatement(sqlPhieu);
					PreparedStatement psCheck = con.prepareStatement(sqlCheckKho);
					PreparedStatement psInsertCT = con.prepareStatement(sqlInsertCT);
					PreparedStatement psUpdateKho = con.prepareStatement(sqlUpdateKho)) {

				// === 1️⃣ Thêm phiếu đặt hàng ===
				psPhieu.setString(1, maPDH);
				psPhieu.setString(2, maKH);
				psPhieu.setString(3, maNV);
				psPhieu.setDate(4, new java.sql.Date(ngayDat.getTime()));
				psPhieu.setDate(5, new java.sql.Date(ngayHen.getTime()));
				psPhieu.setString(6, ghiChu);
				psPhieu.setString(7, "456 Nguyễn Huệ, TP.HCM");
				psPhieu.setString(8, "Hiệu Thuốc Tam Thanh");
				psPhieu.setString(9, "+84-912345689");

				if (psPhieu.executeUpdate() == 0) {
					con.rollback();
					return -1;
				}

				// === 2️⃣ Thêm chi tiết và cập nhật kho ===
				for (Object[] ct : dsChiTiet) {
					String maThuoc = (String) ct[0];
					int soLuong = (int) ct[1];
					String maDVT = (String) ct[2];
					double donGia = (double) ct[3];

					// kiểm tra tồn kho
					psCheck.setString(1, maThuoc);
					try (ResultSet rs = psCheck.executeQuery()) {
						if (rs.next()) {
							int ton = rs.getInt("soLuongTon");
							if (ton < soLuong) {
								con.rollback();
								return 0; // ❌ không đủ tồn kho
							}
						} else {
							con.rollback();
							return 0; // ❌ thuốc không tồn tại trong kho
						}
					}

					// thêm chi tiết phiếu
					psInsertCT.setString(1, maPDH);
					psInsertCT.setString(2, maThuoc);
					psInsertCT.setInt(3, soLuong);
					psInsertCT.setString(4, maDVT);
					psInsertCT.setDouble(5, donGia);
					psInsertCT.executeUpdate();

					// trừ kho
					psUpdateKho.setInt(1, soLuong);
					psUpdateKho.setString(2, maThuoc);
					psUpdateKho.executeUpdate();
				}

				con.commit();
				return 1; // ✅ thành công

			} catch (SQLException e) {
				con.rollback();
				e.printStackTrace();
				return -1;
			} finally {
				con.setAutoCommit(true);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}

	public ArrayList<PhieuDatHang> layListPhieuDatHang() {
		ArrayList<PhieuDatHang> list = new ArrayList<>();

		String sql = """
				SELECT PDH.maPDH, PDH.maKH, KH.tenKH, PDH.maNV, NV.tenNV,
				       PDH.ngayDat, PDH.ngayHen, PDH.diaChiHT, PDH.tenHT,
				       PDH.ghiChu, PDH.hotline, PDH.trangThai
				FROM PhieuDatHang PDH
				JOIN KhachHang KH ON PDH.maKH = KH.maKH
				JOIN NhanVien NV ON PDH.maNV = NV.maNV
				ORDER BY TRY_CAST(SUBSTRING(PDH.maPDH, 6, LEN(PDH.maPDH)) AS INT)
				""";

		try (Connection conn = KetNoiDatabase.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				String maPDH = rs.getString("maPDH");

				// Khách hàng
				KhachHang kh = new KhachHang();
				kh.setMaKH(rs.getString("maKH"));
				kh.setTenKH(rs.getString("tenKH"));

				// Nhân viên
				NhanVien nv = new NhanVien();
				nv.setMaNV(rs.getString("maNV"));
				nv.setTenNV(rs.getString("tenNV"));

				LocalDate ngayDat = rs.getDate("ngayDat").toLocalDate();
				LocalDate ngayHen = rs.getDate("ngayHen").toLocalDate();

				String diaChiHT = rs.getString("diaChiHT");
				String tenHT = rs.getString("tenHT");
				String ghiChu = rs.getString("ghiChu");
				String hotline = rs.getString("hotline");
				String trangThai = rs.getString("trangThai");

				PhieuDatHang pdh = new PhieuDatHang(maPDH, kh, nv, ngayDat, ngayHen, diaChiHT, tenHT, ghiChu, hotline,
						trangThai);
				list.add(pdh);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public ArrayList<Object[]> layDanhSachThuocTheoPDH(String maPDH) {
		ArrayList<Object[]> list = new ArrayList<>();
		String sql = """
				    SELECT
				        ct.maPDH,
				        ct.maThuoc,
				        t.tenThuoc,
				        ct.soLuong,
				        ct.maDVT,
				        dvt.tenDVT,
				        ct.donGia
				    FROM CT_PhieuDatHang ct
				    JOIN Thuoc t ON ct.maThuoc = t.maThuoc
				    JOIN DonViTinh dvt ON ct.maDVT = dvt.maDVT
				    WHERE ct.maPDH = ?
				""";

		try (Connection con = KetNoiDatabase.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, maPDH);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Object[] row = { rs.getString("maPDH"), rs.getString("maThuoc"), rs.getString("tenThuoc"),
						rs.getInt("soLuong"), rs.getString("maDVT"), rs.getString("tenDVT"), rs.getDouble("donGia") };
				list.add(row);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public boolean themPhieuDatHangBangObject(Object[] pdh, String maNV) {
		String sql = "INSERT INTO PhieuDatHang (maPDH, maKH, maNV, ngayDat, ngayHen, diaChiHT, tenHT, ghiChu, hotline, trangThai) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try (Connection con = KetNoiDatabase.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, pdh[0].toString());
			ps.setString(2, pdh[1].toString());
			ps.setString(3, pdh[2].toString());
			ps.setDate(4, java.sql.Date.valueOf(pdh[3].toString()));
			ps.setDate(5, java.sql.Date.valueOf(pdh[4].toString()));
			ps.setString(6, pdh[5].toString());
			ps.setString(7, pdh[6].toString());
			ps.setString(8, pdh[7] != null ? pdh[7].toString() : "");
			ps.setString(9, pdh[8].toString());
			ps.setString(10, pdh[9].toString());

			return ps.executeUpdate() > 0;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean themChiTietPhieuDatHangBangObject(Object[] ct) {
		String sql = "INSERT INTO CT_PhieuDatHang (maPDH, maThuoc, soLuong, donGia, maDVT) VALUES (?, ?, ?, ?, ?)";
		try (Connection con = KetNoiDatabase.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, ct[0].toString());
			ps.setString(2, ct[1].toString());
			ps.setInt(3, Integer.parseInt(ct[2].toString()));
			ps.setDouble(4, Double.parseDouble(ct[3].toString()));
			ps.setString(5, ct[4].toString());
			return ps.executeUpdate() > 0;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public String layMaPDHMoiNhat() {
		String sql = "SELECT TOP 1 pdh.maPDH\r\n" + "FROM PhieuDatHang pdh\r\n"
				+ "ORDER BY TRY_CAST(SUBSTRING(pdh.maPDH, 6, LEN(pdh.maPDH)) AS INT) DESC;";
		try (Connection con = KetNoiDatabase.getConnection();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			if (rs.next()) {
				return rs.getString("maPDH");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean capNhatTrangThaiPhieu(String maPhieu, String trangThaiMoi) {
		String sql = "UPDATE PhieuDatHang SET trangThai = ? WHERE maPDH = ?";
		try (Connection con = KetNoiDatabase.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, trangThaiMoi);
			ps.setString(2, maPhieu);
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
}
