package modelo.dao;

import java.util.List;

import modelo.entidades.Seller;

public interface SellerDao {
	void insert(Seller seller);
	void update(Seller seller);
	void deleteById(int id);
	Seller findById(int id);
	List<Seller> findAll();
	List<Seller>findAllDepartmento(int id);
}
