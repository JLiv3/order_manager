package org.jliv3.customer.order_manager.repository;

import org.jliv3.customer.order_manager.entity.FileImg;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileImgRepository extends CrudRepository<FileImg, Integer> {
}
