package com.lynas.data;

import com.lynas.data.Contact;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ContactDAO extends CrudRepository<Contact, Long> {

}
