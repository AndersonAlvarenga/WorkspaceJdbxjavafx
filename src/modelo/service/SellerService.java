package modelo.service;

import java.util.List;

import modelo.dao.DaoFactory;
import modelo.dao.SellerDao;
import modelo.entidades.Seller;

public class SellerService {
	private SellerDao sellerService = DaoFactory.createSellerDao();

	public List<Seller> findAll() {
		return sellerService.findAll();
	}

	public void insertSeller(Seller seller) {
		sellerService.insert(seller);
	}

	public void saveNewDepartmento(Seller obj) {
		if (obj.getId() == null) {
			sellerService.insert(obj);
		} else {
			sellerService.update(obj);
		}
	}

	public void removeDepart(Seller seller) {
		sellerService.deleteById(seller.getId());
	}

}
