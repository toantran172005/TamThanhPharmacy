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
	public ToolCtrl toolCtrl = new ToolCtrl();
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

	// 1 = Thành công 0 = Không đủ tồn kho -1 = Lỗi SQL hoặc lỗi khác
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
				    ct.donGia,
					SUM(CASE
				       WHEN km.loaiKM = N'Giảm giá' THEN ct.soLuong * ct.donGia * (1 - km.mucKM / 100.0)
				       WHEN km.loaiKM = N'Mua tặng' THEN
				           (ct.soLuong - FLOOR(ct.soLuong / (km.soLuongMua + km.soLuongTang)) * km.soLuongTang) * ct.donGia
				       ELSE ct.soLuong * ct.donGia
				   END) AS thanhTien
				FROM CT_PhieuDatHang ct
				JOIN Thuoc t ON ct.maThuoc = t.maThuoc
				JOIN DonViTinh dvt ON ct.maDVT = dvt.maDVT
				LEFT JOIN KhuyenMai km ON t.maKM = km.maKM
				WHERE ct.maPDH = ?
				GROUP BY  ct.maPDH, ct.maThuoc, t.tenThuoc, ct.soLuong, ct.maDVT, dvt.tenDVT, ct.donGia
				""";

		try (Connection con = KetNoiDatabase.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, maPDH);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Object[] row = { rs.getString("maPDH"), rs.getString("maThuoc"), rs.getString("tenThuoc"),
						rs.getInt("soLuong"), rs.getString("maDVT"), rs.getString("tenDVT"), rs.getDouble("donGia"),
						rs.getDouble("thanhTien") };
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

	// 0: Thành công | 1: Kho không đủ hàng | 2: Lỗi SQL | 3: Không thay đổi
	public int capNhatTrangThaiPhieu(String maPhieu, String trangThaiMoi) {
		Connection con = null;
		PreparedStatement psGetOldStatus = null;
		PreparedStatement psGetItems = null;
		PreparedStatement psCheckKho = null;
		PreparedStatement psUpdateKho = null;
		PreparedStatement psUpdateStatus = null;
		ResultSet rs = null;

		String sqlGetOldStatus = "SELECT trangThai FROM PhieuDatHang WHERE maPDH = ?";
		String sqlGetItems = "SELECT maThuoc, soLuong FROM CT_PhieuDatHang WHERE maPDH = ?";
		String sqlCheckKho = "SELECT SUM(soLuongTon) as tongTon FROM CT_Kho WHERE maThuoc = ?";
		String sqlUpdateKho = "UPDATE CT_Kho SET soLuongTon = soLuongTon + ? WHERE maThuoc = ?";
		String sqlUpdateStatus = "UPDATE PhieuDatHang SET trangThai = ? WHERE maPDH = ?";

		try {
			con = KetNoiDatabase.getConnection();
			con.setAutoCommit(false);

			psGetOldStatus = con.prepareStatement(sqlGetOldStatus);
			psGetOldStatus.setString(1, maPhieu);
			rs = psGetOldStatus.executeQuery();

			String trangThaiCu = "";
			if (rs.next()) {
				trangThaiCu = rs.getString("trangThai");
			} else {
				return 2;
			}

			if (trangThaiCu.equalsIgnoreCase(trangThaiMoi))
				return 3;

			psGetItems = con.prepareStatement(sqlGetItems);
			psGetItems.setString(1, maPhieu);
			ResultSet rsItems = psGetItems.executeQuery();

			List<Object[]> listThuoc = new ArrayList<>();
			while (rsItems.next()) {
				listThuoc.add(new Object[] { rsItems.getString("maThuoc"), rsItems.getInt("soLuong") });
			}

			if (trangThaiMoi.equals("Chờ hàng") && trangThaiCu.equals("Đã hủy")) {

				psCheckKho = con.prepareStatement(sqlCheckKho);
				for (Object[] row : listThuoc) {
					String maThuoc = (String) row[0];
					int soLuongCan = (Integer) row[1];

					psCheckKho.setString(1, maThuoc);
					ResultSet rsKho = psCheckKho.executeQuery();

					if (rsKho.next()) {
						int tonKho = rsKho.getInt("tongTon");
						if (tonKho < soLuongCan) {
							con.rollback();
							return 1;
						}
					} else {
						con.rollback();
						return 1;
					}
				}

				psUpdateKho = con.prepareStatement(sqlUpdateKho);
				for (Object[] row : listThuoc) {
					psUpdateKho.setInt(1, -(Integer) row[1]);
					psUpdateKho.setString(2, (String) row[0]);
					psUpdateKho.executeUpdate();
				}

			}

			else if (trangThaiMoi.equals("Đã hủy") && !trangThaiCu.equals("Đã hủy")) {

				psUpdateKho = con.prepareStatement(sqlUpdateKho);
				for (Object[] row : listThuoc) {
					psUpdateKho.setInt(1, (Integer) row[1]);
					psUpdateKho.setString(2, (String) row[0]);
					psUpdateKho.executeUpdate();
				}
			}

			psUpdateStatus = con.prepareStatement(sqlUpdateStatus);
			psUpdateStatus.setString(1, trangThaiMoi);
			psUpdateStatus.setString(2, maPhieu);

			if (psUpdateStatus.executeUpdate() > 0) {
				con.commit();
				return 0;
			} else {
				con.rollback();
				return 2;
			}

		} catch (Exception e) {
			e.printStackTrace();
			try {
				if (con != null)
					con.rollback();
			} catch (SQLException ex) {
			}
			return 2;
		} finally {
			try {
				if (con != null) {
					con.setAutoCommit(true);
					con.close();
				}
			} catch (SQLException e) {
			}
		}
	}

	public PhieuDatHang timTheoMa(String maPDH) {
		PhieuDatHang pdh = null;

		String sql = """
				    SELECT pdh.maPDH,
				           pdh.ngayDat,
				           pdh.ngayHen,
				           pdh.diaChiHT,
				           pdh.tenHT,
				           pdh.ghiChu,
				           pdh.hotline,
				           pdh.trangThai,
				           kh.maKH,
				           kh.tenKH,
				           kh.sdt,
				           kh.tuoi,
				           nv.maNV,
				           nv.tenNV
				    FROM PhieuDatHang pdh
				    JOIN KhachHang kh ON pdh.maKH = kh.maKH
				    JOIN NhanVien nv ON pdh.maNV = nv.maNV
				    WHERE pdh.maPDH = ?
				""";

		try (Connection con = KetNoiDatabase.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, maPDH);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {

				KhachHang kh = new KhachHang(rs.getString("maKH"), rs.getString("tenKH"), rs.getInt("tuoi"),
						rs.getString("sdt"));

				NhanVien nv = new NhanVien(rs.getString("maNV"), rs.getString("tenNV"));

				pdh = new PhieuDatHang(rs.getString("maPDH"), kh, nv, rs.getDate("ngayDat").toLocalDate(),
						rs.getDate("ngayHen") != null ? rs.getDate("ngayHen").toLocalDate() : null,
						rs.getString("diaChiHT"), rs.getString("tenHT"), rs.getString("ghiChu"),
						rs.getString("hotline"), rs.getString("trangThai"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return pdh;
	}

	public List<Object[]> layChiTietPhieuDatHang(String maPDH) {
		List<Object[]> ds = new ArrayList<>();
		String sql = """
				    SELECT t.tenThuoc,
				           qg.tenQuocGia,
				           ct.soLuong,
				           dvt.tenDVT,
				           ct.donGia,
				           (ct.soLuong * ct.donGia) AS thanhTien
				    FROM CT_PhieuDatHang ct
				    JOIN Thuoc t ON ct.maThuoc = t.maThuoc
				    JOIN QuocGia qg ON t.maQuocGia = qg.maQuocGia
				    JOIN DonViTinh dvt ON ct.maDVT = dvt.maDVT
				    WHERE ct.maPDH = ?
				""";

		try (Connection con = KetNoiDatabase.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, maPDH);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Object[] row = new Object[] { rs.getString("tenThuoc"), // [0]
						rs.getString("tenQuocGia"), // [1]
						rs.getInt("soLuong"), // [2]
						rs.getString("tenDVT"), // [3]
						rs.getDouble("donGia"), // [4]
						rs.getDouble("thanhTien") // [5]
				};
				ds.add(row);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return ds;
	}

}
