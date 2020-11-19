package com.lk.service;

import com.lk.model.ArticleLikesRecord;
import com.lk.utils.DataMap;

/**
 * @author: linkui
 * @Date:2020/11/7 15:48
 * Describe: 文章点赞记录业务操作
 */
public interface ArticleLikesRecordService {

    /**
     * 文章是否已经点过赞
     * @param articledId 文章id
     * @param username 点赞人
     * @return true--已经点过赞  false--没有点赞
     */
    boolean isLiked(long articleId, String username);

    /**
     * 保存文章中点赞的记录
     * @param articleLikesRecord
     */
    void insertArticleLikesRecord(ArticleLikesRecord articleLikesRecord);

    /**
     * 通过文章id删除文章点赞记录
     * @param articleId 文章id
     */
    void deleteArticleLikesRecordByArticleId(long articleId);
    /**
     * 获得文章点赞信息
     */
    DataMap getArticleThumbsUp(int rows, int pageNum);
}
