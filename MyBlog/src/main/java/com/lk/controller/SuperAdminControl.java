package com.lk.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lk.constant.CodeType;
import com.lk.model.FriendLink;
import com.lk.service.ArticleLikesRecordService;
import com.lk.service.ArticleService;
import com.lk.service.CategoryService;
import com.lk.service.FeedBackService;
import com.lk.service.FriendLinkService;
import com.lk.service.PrivateWordService;
import com.lk.service.UserService;
import com.lk.service.VisitorService;
import com.lk.utils.DataMap;
import com.lk.utils.JsonResult;

import net.sf.json.JSONObject;

/**
 * @author: linkui
 * @Date:2020/11/25 16:14
 * Describe: 超级管理页面
 */
@RestController
public class SuperAdminControl {

    @Autowired
    PrivateWordService privateWordService;
    @Autowired
    FeedBackService feedBackService;
    @Autowired
    VisitorService visitorService;
    @Autowired
    UserService userService;
    @Autowired
    ArticleService articleService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    ArticleLikesRecordService articleLikesRecordService;
    
    @Autowired
    FriendLinkService friendLinkService;
    /**
     * 获得所有悄悄话
     * @return
     */
    @PostMapping("/getAllPrivateWord")
    @PreAuthorize("hasAuthority('ROLE_SUPERADMIN')")
    public JSONObject getAllPrivateWord(){
        return privateWordService.getAllPrivateWord();
    }

    /**
     * 回复悄悄话
     * @return
     */
    @PostMapping("/replyPrivateWord")
    public JSONObject replyPrivateWord(@AuthenticationPrincipal Principal principal,
                                       @RequestParam("replyContent") String replyContent,
                                       @RequestParam("replyId") String id){
        String username;
        JSONObject jsonObject;
        try {
            username = principal.getName();
        } catch (NullPointerException e){
            jsonObject = new JSONObject();
            jsonObject.put("status",403);
            return jsonObject;
        }

        return privateWordService.replyPrivateWord(replyContent, username, Integer.parseInt(id));
    }

    /**
     * 分页获得所有反馈信息
     * @param rows 一页大小
     * @param pageNum 当前页
     */
    @GetMapping("/getAllFeedback")
    public JSONObject getAllFeedback(@RequestParam("rows") String rows,
                                     @RequestParam("pageNum") String pageNum){
        return feedBackService.getAllFeedback(Integer.parseInt(rows),Integer.parseInt(pageNum));
    }

    /**
     * 获得统计信息
     * @return
     */
    @GetMapping("/getStatisticsInfo")
    public JSONObject getStatisticsInfo(){
        JSONObject returnJson = new JSONObject();
        long num = visitorService.getAllVisitor();
        returnJson.put("allVisitor", num);
        returnJson.put("allUser", userService.countUserNum());
        returnJson.put("yesterdayVisitor", num);
        returnJson.put("articleNum", articleService.countArticle());
        return returnJson;
    }

    /**
     * 获得文章管理
     * @return
     */
    @GetMapping("/getArticleManagement")
    public JSONObject getArticleManagement(@AuthenticationPrincipal Principal principal,
                                           @RequestParam("rows") String rows,
                                           @RequestParam("pageNum") String pageNum){
        String username = null;
        JSONObject returnJson = new JSONObject();
        try {
            username = principal.getName();
        } catch (NullPointerException e){
            returnJson.put("status",403);
            return  returnJson;
        }
        return articleService.getArticleManagement(Integer.parseInt(rows), Integer.parseInt(pageNum));
    }

    
    
    /**
     * 删除文章
     * @param id 文章id
     * @return 1--删除成功   0--删除失败
     */
    @GetMapping("/deleteArticle")
    @PreAuthorize("hasRole('ROLE_SUPERADMIN')")
    public int deleteArticle(@RequestParam("id") String id){
        if("".equals(id) || id == null){
            return 0;
        }
        return articleService.deleteArticle(Long.parseLong(id));
    }
    /**
     * 获得所有分类
     */
 @GetMapping("/getArticleCategories")
  public String getArticleCategories( ){
	  try {
          DataMap<?> data = categoryService.findAllCategories();
          return JsonResult.build(data).toJSON();
      } catch (Exception e){
         System.out.println("Get article categories exception"+ e);
      }
      return JsonResult.fail(CodeType.SERVER_EXCEPTION).toJSON();
 }
 
 /**
  * 添加或删除分类
  */
 @PostMapping(value = "/updateCategory")
  public String updateCategory(@RequestParam("categoryName") String  categoryName,
                           @RequestParam("type") int type){
     try {
         DataMap data = categoryService.updateCategory(categoryName, type);
         return JsonResult.build(data).toJSON();
     } catch (Exception e){
         System.out.println ("Update type [{}] article categories [{}] exception"+ type+ categoryName+ e);
     }
     return JsonResult.fail(CodeType.SERVER_EXCEPTION).toJSON();
 }
 
 /**
  * 获得文章点赞信息
  */
 @PostMapping(value = "/getArticleThumbsUp")
  public String getArticleThumbsUp(@RequestParam("rows") int rows,
                                      @RequestParam("pageNum") int pageNum){
     try {
         DataMap data = articleLikesRecordService.getArticleThumbsUp(rows, pageNum);
         return JsonResult.build(data).toJSON();
     } catch (Exception e){
         System.out.println("Get article thumbsUp exception"+ e);
     }
     return JsonResult.fail(CodeType.SERVER_EXCEPTION).toJSON();
 }
 /**
  * 已读所有点赞信息
  */
 @GetMapping(value = "/readAllThumbsUp")
  public String readAllThumbsUp(){
     try {
         DataMap data = articleLikesRecordService.readAllThumbsUp();
         return JsonResult.build(data).toJSON();
     } catch (Exception e){
         System.out.println("Read all thumbsUp exception"+ e);
     }
     return JsonResult.fail(CodeType.SERVER_EXCEPTION).toJSON();
 }
 /**
  * 已读一条点赞信息
  */
 @GetMapping(value = "/readThisThumbsUp")
  public String readThisThumbsUp(@RequestParam("id") int id){
     try {
         DataMap data = articleLikesRecordService.readThisThumbsUp(id);
         return JsonResult.build(data).toJSON();
     } catch (Exception e){
    	 System.out.println(("Read one thumbsUp [{}] exception"+ id+ e));
     }
     return JsonResult.fail(CodeType.SERVER_EXCEPTION).toJSON();
 }

 /**
  * 获得友链
  */
 @GetMapping(value = "/getFriendLink")
  public String getFriendLink(){
     try {
         DataMap data = friendLinkService.getAllFriendLink();
         return JsonResult.build(data).toJSON();
     } catch (Exception e){
    	 System.out.println("Get friendLink exception"+ e);
     }
     return JsonResult.fail(CodeType.SERVER_EXCEPTION).toJSON();
 }
 
 /**
  * 添加或编辑友链
  */
 @PostMapping(value = "/updateFriendLink")
 
 public String addFriendLink(@RequestParam("id") String id,
                             @RequestParam("blogger") String blogger,
                             @RequestParam("url") String url){
     try {
         FriendLink friendLink = new FriendLink(blogger, url);
         DataMap data;
         if("".equals(id)||id==null){
             data = friendLinkService.addFriendLink(friendLink);
         } else {
             data = friendLinkService.updateFriendLink(friendLink, Integer.parseInt(id));
         }
         return JsonResult.build(data).toJSON();
     } catch (Exception e){
    	 System.out.println("Update friendLink [{}] exception"+ blogger+ e);
     }
     return JsonResult.fail(CodeType.SERVER_EXCEPTION).toJSON();
 }

 /**
  * 删除友链
  */
 @PostMapping(value = "/deleteFriendLink")
  public String deleteFriendLink(@RequestParam("id") int id){
     try {
         DataMap data = friendLinkService.deleteFriendLink(id);
         return JsonResult.build(data).toJSON();
     } catch (Exception e){
    	 System.out.println("Delete friendLink [{}] exception"+ id+ e);
     }
     return JsonResult.fail(CodeType.SERVER_EXCEPTION).toJSON();
 }

}