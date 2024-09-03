package com.appliances.recyle.repository;

import com.appliances.recyle.domain.FileImage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileImageRepository extends MongoRepository<FileImage, String> {

}
