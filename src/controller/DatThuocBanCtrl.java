package controller;

import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

import dao.ThuocDAO;
import entity.DonViTinh;
import entity.Thuoc;
import gui.DatThuoc_GUI;
import gui.PhieuDatThuoc_GUI;

public class DatThuocBanCtrl {
	
	private DatThuoc_GUI dtGUI;
	private PhieuDatThuoc_GUI pdtGUI;
	private ThuocDAO thDAO = new ThuocDAO();
	private ToolCtrl tool = new ToolCtrl();
	
	public DatThuocBanCtrl(DatThuoc_GUI datThuoc_GUI) {
		this.dtGUI = datThuoc_GUI;
	}

	public DatThuocBanCtrl(PhieuDatThuoc_GUI phieuDatThuoc_GUI) {
		this.pdtGUI = phieuDatThuoc_GUI;
	}
	
	public DatThuocBanCtrl(DatThuoc_GUI datThuoc_GUI, PhieuDatThuoc_GUI phieuDatThuoc_GUI) {
	    this.dtGUI = datThuoc_GUI;
	    this.pdtGUI = phieuDatThuoc_GUI;
	}

	//Đổ dữ liệu cho combobox
	public void setDataChoComboBox() {
		ArrayList<Thuoc> listThuoc = thDAO.layListThuocHoanChinh();
		for(Thuoc t : listThuoc) {
			dtGUI.cmbThuoc.addItem(t.getTenThuoc());
		}
	}
	
	//Kiểm tra thuốc đã tồn tại trong table
	public int kiemTraTonTai(String maThuoc) {
		DefaultTableModel model = (DefaultTableModel) dtGUI.tblDatThuoc.getModel();
		
		for(int i = 0; i < model.getRowCount(); i++) {
			String maThuocTrongBang = (String) model.getValueAt(i, 1);
			if(maThuocTrongBang.equalsIgnoreCase(maThuoc)) {
				return i;
			}
		}
		
		return -1;
		
	}
	
	
	//Thêm thuốc vào bảng
	public void addThuocVaoTable() {
		String tenThuoc = (String) dtGUI.cmbThuoc.getSelectedItem();
		ArrayList<Thuoc> listThuoc = thDAO.layListThuocHoanChinh();
		Thuoc thuoc = null;
		
		//Tìm thuốc theo lựa chọn trong combobox
		for(Thuoc t : listThuoc) {
			if(tenThuoc.equalsIgnoreCase(t.getTenThuoc())) {
				thuoc = t;
				break;
			}
		}
		
		//Đưa dữ liệu lên bảng
		Thuoc thDat = thDAO.layThuocDeDat(thuoc);
		String txtSoLuong = dtGUI.txtSoLuongDat.getText();
		int soLuong = Integer.parseInt(txtSoLuong);
		int stt = 1;
		DefaultTableModel model = (DefaultTableModel) dtGUI.tblDatThuoc.getModel();
		
		int hangTrungLap = kiemTraTonTai(thDat.getMaThuoc());
		if(hangTrungLap != -1) {
			int soLuongCu = Integer.parseInt(model.getValueAt(hangTrungLap, 4).toString());
			model.setValueAt(soLuongCu + soLuong, hangTrungLap, 4);
		} else {
			model.addRow(new Object[] {
					stt++,
					thDat.getMaThuoc(),
					thDat.getTenThuoc(),
					thDat.getDvt().getTenDVT(),
					txtSoLuong
			});
		}
		dtGUI.txtSoLuongDat.setText("1");
	}
	
	//Làm mới giao diện
	public void lamMoi() {
		dtGUI.txtSoLuongDat.setText("1");
		dtGUI.model.setRowCount(0);
	}
	
	//Lấy danh sách thuốc đang được thêm trên table
	public ArrayList<Thuoc> layDanhSachThuocTuTable() {
	    ArrayList<Thuoc> listThuoc = new ArrayList<>();
	    DefaultTableModel model = (DefaultTableModel) dtGUI.tblDatThuoc.getModel();

	    for (int i = 0; i < model.getRowCount(); i++) {
	        String maThuoc = model.getValueAt(i, 1).toString();
	        String tenThuoc = model.getValueAt(i, 2).toString();
	        String tenDVT = model.getValueAt(i, 3).toString();
	        int soLuong = Integer.parseInt(model.getValueAt(i, 4).toString());

	        // Tạo đối tượng đơn vị tính (nếu có lớp DVT)
	        DonViTinh dvt = new DonViTinh();
	        dvt.setTenDVT(tenDVT);

	        // Tạo đối tượng Thuoc
	        Thuoc thuoc = new Thuoc();
	        thuoc.setMaThuoc(maThuoc);
	        thuoc.setTenThuoc(tenThuoc);
	        thuoc.setDvt(dvt);
	        thuoc.setSoLuongDat(soLuong);

	        listThuoc.add(thuoc);
	    }

	    return listThuoc;
	}


	
	//Đố dữ liệu cho bảng phiếu đặt thuốc
	public void setDataChoTablePDT(ArrayList<Thuoc> listThuocDat) {
		DefaultTableModel model = (DefaultTableModel) pdtGUI.tblPhieuDat.getModel();
		int stt = 1;
		
		for(Thuoc t : listThuocDat ) {
			model.addRow(new Object[] {
					stt++,
					t.getTenThuoc(),
					t.getDvt().getTenDVT(),
					t.getSoLuongDat()
			});
		}
	}
	
	
	//Xoá dòng chọn ra khỏi bảng
	public void xoaThuocKhoiBang() {
		int selectedRow = dtGUI.tblDatThuoc.getSelectedRow();
		
		if (selectedRow != -1) { 
	        DefaultTableModel model = (DefaultTableModel) dtGUI.tblDatThuoc.getModel();
	        boolean xacNhan = tool.hienThiXacNhan("Xác nhận", "Bạn có chắc muốn xoá thuốc này không?", null);
	        if(xacNhan) {
	        	model.removeRow(selectedRow);
	        }
	        capNhatSTT(); 
	    } else {
	        tool.hienThiThongBao("Lỗi", "Vui lòng chọn dòng muốn xoá", false);
	    }
	}
	
	// Cập nhật lại cột STT trong bảng
	private void capNhatSTT() {
	    DefaultTableModel model = (DefaultTableModel) dtGUI.tblDatThuoc.getModel();
	    for (int i = 0; i < model.getRowCount(); i++) {
	        model.setValueAt(i + 1, i, 0); 
	    }
	}
	
}
