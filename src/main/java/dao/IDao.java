package dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface IDao <T,I>{
    void save(T c) throws SQLException;
    Optional<T> getById(I id);
    List<T> getAll();
    void update(T c);
    boolean deleteById(I id);
}
