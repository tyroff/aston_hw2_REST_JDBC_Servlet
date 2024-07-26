package dao;

import java.sql.SQLException;
import java.util.List;

public interface IDao <T,K>{
    void save(T c) throws SQLException;
    T getById(K id);
    List<T> getAll();
    void update(T c);
    boolean deleteById(K id);
}
