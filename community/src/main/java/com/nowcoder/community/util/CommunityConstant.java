package com.nowcoder.community.util;

public interface CommunityConstant {

    /**
     * Active successfully
     */
    int ACTIVATION_SUCCESS = 0;

    /**
     * Repeat active
     */
    int ACTIVATION_REPEAT = 1;

    /**
     * Active fail
     */
    int ACTIVATION_FAILURE = 2;

    /**
     * 默认状态的登录凭证的超时时间
     */
    int DEFAULT_EXPIRED_SECONDS = 3600 * 12; // 12小时

    /**
     * 记住状态的登录凭证超时时间
     */
    int REMEMBER_EXPIRED_SECONDS = 3600 * 24 * 100;

    /**
     * 实体类型：帖子
     */
    int ENTITY_TYPE_POST = 1;

    /**
     * 实体类型：评论
     */
    int ENTITY_TYPE_COMMENT = 1;
}
