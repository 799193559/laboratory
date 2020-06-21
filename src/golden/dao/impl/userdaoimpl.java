package golden.dao.impl;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import golden.tempmodel.title_list_1;
import golden.dao.userdao;
import golden.model.Article;
import golden.model.Findings;
import golden.model.Image;
import golden.model.News;
import golden.model.Project;
import golden.model.Recommend;
import golden.model.Researcher;
import golden.model.UserInfo;
import golden.tempmodel.researcher_list;
import golden.tempmodel.title_list;

@Repository
public class userdaoimpl extends SqlSessionDaoSupport implements userdao{
	 @Autowired
	  public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
	    super.setSqlSessionFactory(sqlSessionFactory);
	  }
	
	String ns_1="golden.mapper.UserInfoMapper.";
	String ns_2="golden.mapper.ArticleMapper.";
//	String ns_3="golden.mapper.FindingsMappe.";
	String ns_4="golden.mapper.ProjectMapper.";
	String ns_5="golden.mapper.ImageMapper.";
	String ns_6="golden.mapper.RecommendMapper.";
	String ns_7="golden.mapper.FindingsMapper.";
	String ns_8="golden.mapper.ResearcherMapper.";
	@Override
	public UserInfo selectby_account(String account) {
		// TODO Auto-generated method stub
		return (UserInfo)getSqlSession().selectOne(String.valueOf(ns_1)+"selectByaccount", account);
	}
	@Override
	public int insertuser(UserInfo user_info) {
		// TODO Auto-generated method stub
		return getSqlSession().insert(String.valueOf(ns_1)+"insert", user_info);
	}
	@Override
	public int insertnews(News news) {
		// TODO Auto-generated method stub
		return getSqlSession().insert(String.valueOf(ns_2)+"insert",news);
	}
	/**
	 *
	 */
	@Override
	public Article selectdetail_by_id(Integer article_id) {
		// TODO Auto-generated method stub
		    return getSqlSession().selectOne(String.valueOf(ns_2)+"selectByPrimaryKey",article_id);

		
	}
//	@Override
//	public Findings selectfindings_by_id(Integer article_id) {
//		// TODO Auto-generated method stub
//           return getSqlSession().selectOne(String.valueOf(ns_3)+"selectByPrimaryKey",article_id);
//	}
//	@Override
//	public Project selectproject_by_id(Integer article_id) {
//		// TODO Auto-generated method stub
//		return getSqlSession().selectOne(String.valueOf(ns_4)+"selectByPrimaryKey",article_id);
//	}
	@Override
	public int insertarticle(Article article) {
		// TODO Auto-generated method stub
		return getSqlSession().insert(String.valueOf(ns_2)+"insert",article);
	}
	@Override
	public List<title_list> select_title_list() {
		// TODO Auto-generated method stub
	   return getSqlSession().selectList(String.valueOf(ns_2)+"select_title_list");
	}
	@Override
	public int delete_article(Integer article_id) {
		// TODO Auto-generated method stub
	   return  getSqlSession().delete(String.valueOf(ns_2)+"deleteByPrimaryKey",article_id);

	}
	@Override
	public int insertimage(Image imageinfo) {
		// TODO Auto-generated method stub
		return getSqlSession().insert(String.valueOf(ns_5)+"insert",imageinfo);
	}
	
	@Override
	public int insertrecommend(Recommend recommend) {
		// TODO Auto-generated method stub
		return getSqlSession().insert(String.valueOf(ns_6)+"insert",recommend);
	}
	@Override
	public int deleterecommend(Integer recommend_id) {
		// TODO Auto-generated method stub
		return getSqlSession().delete(String.valueOf(ns_6)+"deleteByPrimaryKey",recommend_id);
	}
	@Override
	public int updaterecommend(Recommend recommend) {
		// TODO Auto-generated method stub
		return getSqlSession().update(String.valueOf(ns_6)+"updateByPrimaryKey",recommend);
	}
	@Override
	public Recommend selectrecommend(Integer recommend_id) {
		// TODO Auto-generated method stub
		return getSqlSession().selectOne(String.valueOf(ns_6)+"selectByPrimaryKey",recommend_id);
	}
	

	@Override
	public int delete_findings(Integer article_id) {
		// TODO Auto-generated method stub
		return getSqlSession().delete(String.valueOf(ns_7)+"deleteByPrimaryKey",article_id);
	}
	@Override
	public Findings selectfindings_by_id(Integer article_id,String type) {
		// TODO Auto-generated method stub
		Findings findings = new Findings();
		findings.setType(type);
		findings.setId(article_id);
      return getSqlSession().selectOne(String.valueOf(ns_7)+"selectByPrimaryKey",findings);

	}
	@Override
	public int insertfindings(Findings other) {
		// TODO Auto-generated method stub
		return getSqlSession().insert(String.valueOf(ns_7)+"insert",other);
	}
	@Override
	public List<title_list_1> select_findings_1(String type) {
		// TODO Auto-generated method stub
		return getSqlSession().selectList(String.valueOf(ns_7)+"select_title_list_1",type);
	}
	@Override
	public int insertresearcher(Researcher researcher) {
		// TODO Auto-generated method stub
		return getSqlSession().insert(String.valueOf(ns_8)+"insert",researcher);
	}
	@Override
	public int delete_researcher(Integer id) {
		// TODO Auto-generated method stub
		return getSqlSession().delete(String.valueOf(ns_8)+"deleteByPrimaryKey",id);
	}
	@Override
	public List<researcher_list> select_researcher_list() {
		// TODO Auto-generated method stub
		return getSqlSession().selectList(String.valueOf(ns_8)+"select_researcher_list");
	}
	@Override
	public Researcher select_researcher(Integer id) {
		// TODO Auto-generated method stub
		return getSqlSession().selectOne(String.valueOf(ns_8)+"selectByPrimaryKey",id);
	}
	@Override
	public int updatearticle(Article article) {
		// TODO Auto-generated method stub
		return getSqlSession().update(String.valueOf(ns_2)+"updateByPrimaryKey",article);
	}
	@Override
	public int updateresearcher(Researcher researcher) {
		// TODO Auto-generated method stub
		return getSqlSession().update(String.valueOf(ns_8)+"updateByPrimaryKey",researcher);
	}
	@Override
	public int updatefindings(Findings other) {
		// TODO Auto-generated method stub
		return getSqlSession().update(String.valueOf(ns_7)+"updateByPrimaryKey",other);
	}
}
