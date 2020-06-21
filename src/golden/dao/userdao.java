package golden.dao;

import java.util.List;

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
import golden.tempmodel.title_list_1;

public interface userdao {
    public UserInfo selectby_account(String account);

	public int insertuser(UserInfo user_info);

	public int insertnews(News news);

	public Article selectdetail_by_id(Integer article_id);

//	public Findings selectfindings_by_id(Integer article_id);
//
//	public Project selectproject_by_id(Integer article_id);

	public int insertarticle(Article article);

	public List<title_list> select_title_list();

	public int delete_article(Integer article_id);

	public int insertimage(Image imageinfo);

	public int insertrecommend(Recommend recommend);

	public int deleterecommend(Integer recommend_id);

	public int updaterecommend(Recommend recommend);

	public Recommend selectrecommend(Integer recommend_id);

	public int delete_findings(Integer article_id);

	public Findings selectfindings_by_id(Integer article_id, String type);

	public int insertfindings(Findings other);

	public List<title_list_1> select_findings_1(String type);

	public int insertresearcher(Researcher researcher);

	public int delete_researcher(Integer id);

	public List<researcher_list> select_researcher_list();

	public Researcher select_researcher(Integer id);

	public int updatearticle(Article article);

	public int updateresearcher(Researcher researcher);

	public int updatefindings(Findings other);
}
