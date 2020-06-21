package golden.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import golden.dao.userdao;
import golden.model.Article;
import golden.model.Findings;
import golden.model.Image;
import golden.model.News;
import golden.model.Project;
import golden.model.Recommend;
import golden.model.Researcher;
import golden.model.UserInfo;
import golden.service.userservice;
import golden.tempmodel.researcher_list;
import golden.tempmodel.title_list;
import golden.tempmodel.title_list_1;

@Service("userservice")
public class userserviceimpl implements userservice{
    
	@Autowired
	userdao user_dao;
	@Override
	public UserInfo selectby_account(String account) {
		// TODO Auto-generated method stub
		return user_dao.selectby_account(account);
	}
	@Override
	public int insertuser(UserInfo user_info) {
		// TODO Auto-generated method stub
		return user_dao.insertuser(user_info);
	}
	@Override
	public int insertnews(News news) {
		// TODO Auto-generated method stub
		return user_dao.insertnews(news);
	}
	@Override
	public Article selectdetail_by_id(Integer article_id) {
		// TODO Auto-generated method stub
		return user_dao.selectdetail_by_id(article_id);
	}
//	@Override
//	public Findings selectfindings_by_id(Integer article_id) {
//		// TODO Auto-generated method stub
//		return user_dao.selectfindings_by_id(article_id);
//	}
//	@Override
//	public Project selectproject_by_id(Integer article_id) {
//		// TODO Auto-generated method stub
//		return user_dao.selectproject_by_id(article_id);
//	}
	@Override
	public int insertarticle(Article article) {
		// TODO Auto-generated method stub
		return user_dao.insertarticle(article);
	}
	@Override
	public List<title_list> select_title_list() {
		// TODO Auto-generated method stub
		return user_dao.select_title_list();
	}
	@Override
	public int delete_article(Integer article_id) {
		// TODO Auto-generated method stub
		return user_dao.delete_article(article_id);
	}
	@Override
	public int insertimage(Image imageinfo) {
		// TODO Auto-generated method stub
		return user_dao.insertimage(imageinfo);
	}
	@Override
	public int insertrecommend(Recommend recommend) {
		// TODO Auto-generated method stub
		return user_dao.insertrecommend(recommend);
	}
	@Override
	public int deleterecommend(Integer recommend_id) {
		// TODO Auto-generated method stub
		return user_dao.deleterecommend(recommend_id);
	}
	@Override
	public int updaterecommend(Recommend recommend) {
		// TODO Auto-generated method stub
		return user_dao.updaterecommend(recommend);
	}
	@Override
	public Recommend selectrecommend(Integer recommend_id) {
		// TODO Auto-generated method stub
		return user_dao.selectrecommend(recommend_id);
	}

	@Override
	public int delete_findings(Integer article_id) {
		// TODO Auto-generated method stub
		return user_dao.delete_findings(article_id);
	}
	@Override
	public Findings selectfindings_by_id(Integer article_id,String type) {
		// TODO Auto-generated method stub
		return user_dao.selectfindings_by_id(article_id,type);
	}
	@Override
	public int insertfindings(Findings other) {
		// TODO Auto-generated method stub
		return user_dao.insertfindings(other);
	}
	@Override
	public List<title_list_1> select_findings_1(String type) {
		// TODO Auto-generated method stub
		return user_dao.select_findings_1(type);
	}
	@Override
	public int insertresearcher(Researcher researcher) {
		// TODO Auto-generated method stub
		return user_dao.insertresearcher(researcher);
	}
	@Override
	public int delete_researcher(Integer id) {
		// TODO Auto-generated method stub
		return user_dao.delete_researcher(id);
	}
	@Override
	public List<researcher_list> select_researcher_list() {
		// TODO Auto-generated method stub
		return user_dao.select_researcher_list();
	}
	@Override
	public Researcher select_researcher(Integer id) {
		// TODO Auto-generated method stub
		return user_dao.select_researcher(id);
	}
	@Override
	public int updatearticle(Article article) {
		// TODO Auto-generated method stub
		return user_dao.updatearticle(article);
	}
	@Override
	public int updateresearcher(Researcher researcher) {
		// TODO Auto-generated method stub
		return user_dao.updateresearcher(researcher);
	}
	@Override
	public int updatefindings(Findings other) {
		// TODO Auto-generated method stub
		return user_dao.updatefindings(other);
	}


}
