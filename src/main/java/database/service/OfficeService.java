package database.service;

import database.dao.AddressDAO;
import database.dao.ContactDAO;
import database.dao.ProvinceDAO;
import database.dao.TradeDAO;
import database.entity.Address;
import database.entity.Contact;
import database.entity.Province;
import database.entity.Trade;
import exception.DataTooLongViolationException;
import exception.NameUniqueViolationException;
import org.hibernate.SessionFactory;

import java.util.List;

public class OfficeService {
    private AddressDAO addressDAO;
    private ContactDAO contactDAO;
    private ProvinceDAO provinceDAO;
    private TradeDAO tradeDAO;

    public OfficeService(SessionFactory sessionFactory) {
        addressDAO = new AddressDAO(sessionFactory);
        contactDAO = new ContactDAO(sessionFactory);
        provinceDAO = new ProvinceDAO(sessionFactory);
        tradeDAO = new TradeDAO(sessionFactory);
    }

    public List<Address> getAddresses() {
        return addressDAO.getEntities();
    }

    public void saveAddress(Address address) throws DataTooLongViolationException {
        addressDAO.saveEntity(address);
    }

    public Address getAddress(int id) {
        return addressDAO.getEntity(id);
    }

    public void deleteAddress(int id) {
        addressDAO.deleteEntity(id);
    }

    public List<Contact> getContacts() {
        return contactDAO.getEntities();
    }

    public void saveAddress(Contact contact) throws DataTooLongViolationException {
        contactDAO.saveEntity(contact);
    }

    public Contact getContact(int id) {
        return contactDAO.getEntity(id);
    }

    public void deleteContact(int id) {
        contactDAO.deleteEntity(id);
    }

    public List<Province> getProvinces() {
        return provinceDAO.getEntities();
    }

    public void saveProvince(Province province) throws NameUniqueViolationException, DataTooLongViolationException {
        provinceDAO.saveEntity(province);
    }

    public Province getProvince(int id) {
        return provinceDAO.getEntity(id);
    }

    public void deleteProvince(int id) {
        provinceDAO.deleteEntity(id);
    }

    public List<Trade> getTrades() {
        return tradeDAO.getEntities();
    }

    public void saveTrade(Trade trade) throws NameUniqueViolationException, DataTooLongViolationException {
        tradeDAO.saveEntity(trade);
    }

    public Trade getTrade(int id) {
        return tradeDAO.getEntity(id);
    }

    public void deleteTrade(int id) {
        tradeDAO.deleteEntity(id);
    }
}
