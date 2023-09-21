package com.nowcoder.community.service;

import com.nowcoder.community.dao.AlphaDao;
import com.nowcoder.community.dao.DiscussPostMapper;
import com.nowcoder.community.dao.UserMapper;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.util.CommunityUtil;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Date;

@Service
@Scope("prototype")
public class AlphaService {

    private static final Logger logger = LoggerFactory.getLogger(AlphaService.class);

    @Autowired
    private AlphaDao alphaDao;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private TransactionTemplate transactionTemplate;
    public AlphaService() {
        System.out.println("Instantiate AlphaService");
    }

    @PostConstruct
    public void init() {
        System.out.println("Initialize AlphaService");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("Destroy AlphaService");
    }

    public String find() {
        return alphaDao.select();
    }

    // REQUIRED: 支持当前事务(外部事务),如果不存在则创建新事务.
    // REQUIRES_NEW: 创建一个新事务,并且暂停当前事务(外部事务).
    // NESTED: 如果当前存在事务(外部事务),则嵌套在该事务中执行(独立的提交和回滚),否则就会REQUIRED一样.
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public Object save1() {
        //新建用户
        User user = new User();
        user.setUsername("aplha");
        user.setSalt(CommunityUtil.generateUUID().substring(0, 5));
        user.setPassword(CommunityUtil.md5("123" + user.getSalt()));
        user.setEmail("Alpha@qq.com");
        user.setHeaderUrl("http://image/nowcoder.com/head/99t.png");
        user.setCreateTime(new Date());
        userMapper.insertUser(user);
        //新建帖子
        DiscussPost post = new DiscussPost();
        post.setUserId(user.getId());
        post.setTitle("New user's post");
        post.setContent("Newcomers report, nice to meet you!");
        post.setCreateTime(new Date());
        discussPostMapper.insertDiscussPost(post);
        //报错前会把数据插入吗？
        Integer.valueOf("abc");
        return "ok";
    }

    public Object save2() {
        transactionTemplate.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        return transactionTemplate.execute(new TransactionCallback<Object>() {

            @Override
            public Object doInTransaction(TransactionStatus transactionStatus) {
                //新建用户
                User user = new User();
                user.setUsername("beta");
                user.setSalt(CommunityUtil.generateUUID().substring(0, 5));
                user.setPassword(CommunityUtil.md5("123" + user.getSalt()));
                user.setEmail("beta@qq.com");
                user.setHeaderUrl("http://image/nowcoder.com/head/999.png");
                user.setCreateTime(new Date());
                userMapper.insertUser(user);
                //新建帖子
                DiscussPost post = new DiscussPost();
                post.setUserId(user.getId());
                post.setTitle("New post, my name is beta!");
                post.setContent("Newcomers report, nice to meet you!");
                post.setCreateTime(new Date());
                discussPostMapper.insertDiscussPost(post);
                //报错前会把数据插入吗？
                Integer.valueOf("abc");
                return "ok";
            }
        });
    }
}
