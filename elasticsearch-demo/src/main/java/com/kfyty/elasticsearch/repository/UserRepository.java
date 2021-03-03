package com.kfyty.elasticsearch.repository;

import com.kfyty.elasticsearch.entity.User;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * 描述:
 *
 * @author kfyty725
 * @date 2021/3/2 14:19
 * @email kfyty725@hotmail.com
 */
@Repository
public interface UserRepository extends ElasticsearchRepository<User, Integer> {
    User findByNameEquals(String name);
}
