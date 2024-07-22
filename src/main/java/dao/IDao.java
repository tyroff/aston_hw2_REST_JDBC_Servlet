package dao;

import model.Patient;

import java.sql.SQLException;
import java.util.List;

public interface IDao <T,I>{
    void save(T c) throws SQLException;
    Patient getById(I id);
    List<T> getAll();
    void update(T c);
    boolean deleteById(I id);
}
