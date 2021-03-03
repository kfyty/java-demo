package com.kfyty.elasticsearch;

import com.kfyty.elasticsearch.entity.User;
import com.kfyty.elasticsearch.entity.base.Entity;
import com.kfyty.elasticsearch.repository.UserRepository;
import com.kfyty.elasticsearch.utils.builder.ElasticsearchQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@SpringBootApplication
public class ElasticsearchApplication implements CommandLineRunner {
    @Autowired
    private ElasticsearchRestTemplate elasticsearchTemplate;

    @Autowired
    private UserRepository userRepository;

    public static void main(String[] args) {
        SpringApplication.run(ElasticsearchApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        
    }

    public void testQuery1() {
        // id in(1, 2) and name like '%hello%' or objectId = 12
        ElasticsearchQueryBuilder<User> es = ElasticsearchQueryBuilder.or(User.class)
                .or(ElasticsearchQueryBuilder.and(User.class).in(User::getId, "1", "2").contains(User::getName, "hello"))
                .or(ElasticsearchQueryBuilder.and(User.class).eq(Entity::getObjectId, 12));
        List<User> users = elasticsearchTemplate.queryForList(es.build(), User.class);
        System.out.println(users);
    }

    public void testQuery2() {
        // objectId = 11 and (name = 'qwe' or name = 'zxc')
        ElasticsearchQueryBuilder<User> es = ElasticsearchQueryBuilder.and(User.class)
                .and(ElasticsearchQueryBuilder.and(User.class).eq(User::getObjectId, 11))
                .and(ElasticsearchQueryBuilder.or(User.class).eq(User::getName, "qwe").eq(User::getName, "zxc"));
        List<User> users = elasticsearchTemplate.queryForList(es.build(), User.class);
        System.out.println(users);
    }

    public void saveTest(Integer id, Integer objectId, String name) {
        User user = new User();
        user.setId(id);
        user.setObjectId(objectId);
        user.setName(name);
        user.setCreateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        userRepository.save(user);
    }
}
