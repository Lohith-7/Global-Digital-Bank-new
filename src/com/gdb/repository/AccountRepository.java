package com.gdb.repository;

import com.gdb.domain.IAccount;
import java.util.Collection;

public interface AccountRepository {
    void save(IAccount account);
    IAccount findById(int accountNumber);
    Collection<IAccount> findAll();
    void update(IAccount account);
    void delete(int accountNumber);
    boolean exists(int accountNumber);
    int nextAccountNumber();
}
