package golden.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.SimpleFormatter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.util.URLEncoder;
import org.apache.commons.io.IOUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

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
import golden.token.token_1;

import org.springframework.mock.web.MockMultipartFile;
//import org.apache.http.entity.ContentType;



@Controller
@RequestMapping({"/api"})
public class usercontroller {
    @Autowired
	userservice user_service;
    
    @RequestMapping({"/login"})
    @ResponseBody
    public JSONObject login(String account,String password,HttpServletRequest head, HttpServletResponse response) throws NoSuchAlgorithmException {
    	
    	String msg=null,code=null;
    	JSONObject result=new JSONObject();
    	if(account==null||account=="") {
    		msg="account missing or format error";
        	code="-1";
        	result.put("msg", msg);
        	result.put("code",code);
        	return result;
    	}

    	if(password==null||password=="") {
    		msg="password missing";
        	code="-2";
        	result.put("msg", msg);
        	result.put("code",code);
        	return result;
    	}
    	UserInfo user_info =user_service.selectby_account(account);
    	
    	if(user_info==null) {
    		msg="account doesn't exit";
        	code="-3";
        	result.put("msg", msg);
        	result.put("code",code);
        	return result;
    	}
    	
//        MessageDigest md = MessageDigest.getInstance("MD5");
//        md.update(password.getBytes());
//        byte[] b = md.digest();
//        StringBuffer buf = new StringBuffer("");
//        for (int offset = 0; offset < b.length; offset++) {
//          int i = b[offset];
//          if (i < 0)
//            i += 256; 
//          if (i < 16)
//            buf.append("0"); 
//          buf.append(Integer.toHexString(i));
//        } 
        
        if(user_info.getPassword().equals(password)) {
	        	msg="success";
	        	code="200";
        	String name=user_info.getName();
        	token_1 token_2 = new token_1();
            String token = token_2.getToken(true, account, name);
            result.put("data", token);
        	result.put("msg", msg);
        	result.put("code",code);
        	return result;
        }
        else {
        	msg="password error";
        	code="-4";
        	result.put("msg", msg);
        	result.put("code",code);
        	return result;
        }
        	
    }
    
    
    
    @RequestMapping({"/register"})
    @ResponseBody
    public JSONObject register(@RequestBody String json) throws NoSuchAlgorithmException {
    	String msg=null,code=null;
    	JSONObject result=new JSONObject();
    	JSONObject info=new JSONObject();
    	try{
    		info=JSON.parseObject(json);
    	}
    	catch (JSONException e){
    		msg="format error";
    		code="-8";
    		result.put("msg",msg);
    		result.put("code",code);
    		return result;
    	}
    	UserInfo user_info=new UserInfo();
//    	if(info.get("userid")==null) {
//    		msg="userid missing";
//    		code="-1";
//    		result.put("msg",msg);
//    		result.put("code",code);
//    		return result;
//    	}
    	if(info.get("account")==null) {
    		msg="account missing";
    		code="-2";
    		result.put("msg",msg);
    		result.put("code",code);
    		return result;
    	}
    	if(info.get("password")==null) {
    		msg="password missing";
    		code="-3";
    		result.put("msg",msg);
    		result.put("code",code);
    		return result;
    	}
    	if(info.get("name")==null) {
    		msg="name missing";
    		code="-4";
    		result.put("msg",msg);
    		result.put("code",code);
    		return result;
    	}
    	if(info.get("phone_number")==null) {
    		msg="phone_number missing";
    		code="-5";
    		result.put("msg",msg);
    		result.put("code",code);
    		return result;
    	}
    	if(user_service.selectby_account(info.getString("account"))!=null) {
    		msg="account exit";
    		code="-6";
    		result.put("msg",msg);
    		result.put("code",code);
    		return result;
    	}
    	
    	user_info.setAccount(info.getString("account"));
    	
    	String password=info.getString("password");
    	   MessageDigest md = MessageDigest.getInstance("MD5");
           md.update(password.getBytes());
           byte[] b = md.digest();
           StringBuffer buf = new StringBuffer("");
           for (int offset = 0; offset < b.length; offset++) {
             int i = b[offset];
             if (i < 0)
               i += 256; 
             if (i < 16)
               buf.append("0"); 
             buf.append(Integer.toHexString(i));
           } 
    	user_info.setPassword(buf.toString());
    	
    	user_info.setName(info.getString("name"));
    	user_info.setPhoneNumber(info.getString("phone_number"));
    	int num=user_service.insertuser(user_info);
    	if(num==0) {
    		msg="database error";
    		code="-7";
    		result.put("msg",msg);
    		result.put("code",code);
    		return result;
    	}
    	msg="success";
		code="200";
		result.put("msg",msg);
		result.put("code",code);
		return result;
    }
    
    
    
    @RequestMapping({"/insertother"})
    @ResponseBody
    public JSONObject insertother(String type,String title,String text,HttpServletRequest request) throws IOException,MultipartException, ParseException
    {

    	JSONObject result =new JSONObject();
    	String msg=null,code=null;
    	if(text==null||text=="") {
    		msg="text missing or format error";
    		code="-1";
    		result.put("msg",msg);
    		result.put("code",code);
    		return result;
    	}
    	if(title==null||title=="") {
    		msg="title missing";
    		code="-2";
    		result.put("msg",msg);
    		result.put("code",code);
    		return result;
    	}
    	if(type==null||type=="") {
    		msg="type missing";
    		code="-3";
    		result.put("msg",msg);
    		result.put("code",code);
    		return result;
    	}
    	if((!type.equals("project"))&&(!type.equals("findings_project"))&&(!type.equals("findings_essay"))&&(!type.equals("findings_property_right"))&&(!type.equals("findings_award"))) {
    		msg="type error";
    		code="-4";
    		result.put("msg",msg);
    		result.put("code",code);
    		return result;
    	}   	
    	Findings other=new Findings();    	
       	other.setTitle(title);
    	Date d = new Date();
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    	String date =sdf.format(d);
    	other.setDate(date);
        other.setContent(text);
        int flag;
    	other.setType(type);
        flag=user_service.insertfindings(other);
    	if(flag==0) {
    		msg="database error";
    		code="-5";
    		result.put("msg",msg);
    		result.put("code",code);
    		return result;
    	}
 
        msg="success";
		code="200";
		result.put("msg",msg);
		result.put("code",code);
        return result;
    }
    
    
    
    
    
    @RequestMapping({"/insertnews"})
    @ResponseBody
    public JSONObject insertnews(String text,String title,String username,String photosource,String cover,HttpServletRequest request) throws IOException,MultipartException, ParseException
    {

    	JSONObject result =new JSONObject();
    	String msg=null,code=null;
    	if(text==null||text=="") {
    		msg="text missing or format error";
    		code="-1";
    		result.put("msg",msg);
    		result.put("code",code);
    		return result;
    	}
    	
    	if(title==null||title=="") {
    		msg="title missing";
    		code="-2";
    		result.put("msg",msg);
    		result.put("code",code);
    		return result;
    	}
    	
    	if(username==null||username=="") {
    		msg="username missing";
    		code="-3";
    		result.put("msg",msg);
    		result.put("code",code);
    		return result;
    	}
    	if(photosource==null||photosource=="") {
    		msg="photosource missing";
    		code="-4";
    		result.put("msg",msg);
    		result.put("code",code);
    		return result;
    	}
    	if(cover==null||cover=="") {
    		msg="cover missing";
    		code="-5";
    		result.put("msg",msg);
    		result.put("code",code);
    		return result;
    	}
    	
    	Article article=new Article();
    	article.setUsername(username);
    	article.setCoverImage(cover);
    	article.setPhotosource(photosource);
       	article.setTitle(title);
    	Date d = new Date();
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    	String date =sdf.format(d);
    	article.setDate(date);
        article.setContent(text);
       	int  flag=user_service.insertarticle(article);
    	if(flag==0) {
    		msg="database error";
    		code="-6";
    		result.put("msg",msg);
    		result.put("code",code);
    		return result;
    	}
 
        msg="success";
		code="200";
		result.put("msg",msg);
		result.put("code",code);
        return result;
    }
    
    
    
    @RequestMapping({"/articledetail"})
    @ResponseBody
    public JSONObject getdetail(Integer id,HttpServletResponse response,String type,HttpServletRequest request) throws IOException {
    	String msg=null,code=null;
    	JSONObject result= new JSONObject();
    	if(request.getParameter("id")==null) {
    		msg="id missing or format error";
     		code="-1";
     		result.put("msg",msg);
     		result.put("code",code);
            return result; 
    	}
    	
    	if(request.getParameter("type")==null) {
    		msg="type missing";
     		code="-2";
     		result.put("msg",msg);
     		result.put("code",code);
            return result; 
    	}
    	
    	if(!(request.getParameter("type").equals("news")||request.getParameter("type").equals("project")||request.getParameter("type").equals("findings_project")||request.getParameter("type").equals("researcher"))) {
    		msg="type error";
     		code="-3";
     		result.put("msg",msg);
     		result.put("code",code);
            return result;
    	}
    	Article article=new Article();
    	Findings other = new Findings();
    	Researcher researcher = new Researcher();
    	if(type.equals("project")||type.equals("findings_project")) { 
    		other = user_service.selectfindings_by_id(id,type);  
    		if(other==null) {
    	    		msg="article doesn't exit";
    	      		code="-4";
    	      		result.put("msg",msg);
    	      		result.put("code",code);
    	            return result; 
    	    	}
    		result.put("data",other);
    		}
    	else if(type.equals("researcher")) {
    		researcher = user_service.select_researcher(id);
    		if(researcher==null) {
	    		msg="article doesn't exit";
	      		code="-4";
	      		result.put("msg",msg);
	      		result.put("code",code);
	            return result; 
	    	}
		result.put("data",researcher);
    	}
    	else { 
            article=user_service.selectdetail_by_id(id);
    	    if(article==null) {
    		msg="article doesn't exit";
      		code="-4";
      		result.put("msg",msg);
      		result.put("code",code);
            return result; 
    	}
    	    result.put("data",article);
    	}
   		msg="success";
        code="200";
    	result.put("msg",msg);
    	result.put("code",code);
        return result; 
        }


    
    
    @RequestMapping({"/getotherlist"})
    @ResponseBody
    public JSON get_other_list(String type,Integer offset,Integer offsetsize) {
    	String code=null;
    	String msg=null;
    	JSONObject result = new JSONObject();
    	
    	if(type==null||type=="") {
    		msg="type missing or format error";
     		code="-1";
    		result.put("code",code);
    		result.put("msg",msg);
    		return result;
    	}
     	if(offset==null) {
    		msg="offset missing";
     		code="-2";
    		result.put("code",code);
    		result.put("msg",msg);
    		return result;
    	}
     	if(offsetsize==null) {
    		msg="offsetsize missing";
     		code="-3";
    		result.put("code",code);
    		result.put("msg",msg);
    		return result;
    	}
    	if((!type.equals("project"))&&(!type.equals("findings_project"))&&(!type.equals("findings_essay"))&&(!type.equals("findings_property_right"))&&(!type.equals("findings_award"))) {
    		msg="type error";
     		code="-4";
    		result.put("code",code);
    		result.put("msg",msg);
    		return result;
    	}
    	List<title_list_1>  list_1 = new ArrayList<title_list_1>();  
        List<title_list_1>  list = new ArrayList<title_list_1>();  
        int total;
        list = user_service.select_findings_1(type);
     
        total = list.size();
        
        for(int k = total-1; k >= 0; k--) {
        	list_1.add(list.get(k));
        }
           
        List<title_list_1> data = new ArrayList<title_list_1>();

                          
        int j ;

        if(list_1.size()<offsetsize) {//��ҳ��
        	j = list_1.size();
        }
        else {
            j = offset*offsetsize;
        }
        for(int i= offset*offsetsize-offsetsize,k=0;i<j;i++,k++) {
        	try {
        	data.add(k, list_1.get(i));
        	}
        	catch(IndexOutOfBoundsException e) {
        		msg="success";
         		code="200";
        		result.put("code",code);
        		result.put("msg",msg);
        		result.put("list",data);
           		result.put("total",total);
        		return result;
        	}
        }
        
        msg="success";
 		code="200";
 		result.put("msg",msg);
		result.put("code",code);
		result.put("list",data);
   		result.put("total",total);
		return result;
    }



    
    
    @RequestMapping({"/getnewslist"})
    @ResponseBody
    public JSON get_news_list(Integer offset,Integer offsetsize,String username,String title) {
    
    	
    	String code=null;
    	String msg=null;
    	JSONObject result = new JSONObject();
    	List<title_list> list  = new ArrayList<title_list>();
    	List<title_list> list_1  = new ArrayList<title_list>();
    	List<title_list> data  = new ArrayList<title_list>();
    	List<title_list> data_1  = new ArrayList<title_list>();
  
    	int total;
     	if(offset==null) {
    		msg="offset missing";
     		code="-1";
    		result.put("code",code);
    		result.put("msg",msg);
    		return result;
    	}
     	if(offsetsize==null) {
    		msg="offsetsize missing";
     		code="-2";
    		result.put("code",code);
    		result.put("msg",msg);
    		return result;
    	}	
     	
    	list_1 = user_service.select_title_list();
    	total = list_1.size();
    	for(int k = total-1; k >=0; k--) {
    		list.add(list_1.get(k));
    	}

    	
        if(title!=""&&username!=""&&title!=null&&username!=null) {
    	for(int i=0,j=0;i<list.size();i++) {
    		if(list.get(i).getUsername().contains(username)&&list.get(i).getTitle().contains(title)) {
    			data.add(j, list.get(i));
    			j++;
    		}
    	}
    	int total_1=data.size(); 
    	   int z;
    	   if(data.size()<offsetsize) {//����һҳ��������һҳ����
           	z = data.size();
           }
           else {
               z = offset*offsetsize;
           }
           for(int i= offset*offsetsize-offsetsize,k=0;i<z;i++,k++) {
           	try {
           	data_1.add(k, data.get(i));
           	}
           	catch(IndexOutOfBoundsException e) {//���һҳ
           		msg="success";
            	code="200";
           		result.put("code",code);
           		result.put("msg",msg);
           		result.put("list",data_1);
           		result.put("total",total_1);
           		return result;
           	}
           }
    	
    	
        msg="success";
 		code="200";
 		result.put("msg",msg);
		result.put("code",code);
		result.put("list",data_1);
   		result.put("total",total_1);
		return result;
    }
    
    
    
    
    if(title!=""&&title!=null) {
    	for(int i=0,j=0;i<list.size();i++) {
    		if(list.get(i).getTitle().contains(title)) {
    			data.add(j, list.get(i));
    			j++;
    		}
    	}
        int total_1=data.size();

 	   int z;
 	   if(data.size()<offsetsize) {//����һҳ��������һҳ����
        	z = data.size();
        }
        else {
            z = offset*offsetsize;
        }
        for(int i= offset*offsetsize-offsetsize,k=0;i<z;i++,k++) {
        	try {
        	data_1.add(k, data.get(i));
        	}
        	catch(IndexOutOfBoundsException e) {//���һҳ
        		msg="success";
         	    code="200";
        		result.put("code",code);
        		result.put("msg",msg);
        		result.put("list",data_1);
           		result.put("total",total_1);
        		return result;
        	}
        }
 	
 	
    msg="success";
		code="200";
		result.put("msg",msg);
		result.put("code",code);
		result.put("list",data_1);
   		result.put("total",total_1);
		return result;    
    }
    
    
    
    
    if(username!=""&&username!=null) {
    	for(int i=0,j=0;i<list.size();i++) {
    		if(list.get(i).getUsername().contains(username)) {
    			data.add(j, list.get(i));
    			j++;
    		}
    	}
    	
       int total_1=data.size();

 	   int z;
 	   if(data.size()<offsetsize) {//����һҳ��������һҳ����
        	z = data.size();
        }
        else {
            z = offset*offsetsize;
        }
        for(int i= offset*offsetsize-offsetsize,k=0;i<z;i++,k++) {
        	try {
        	data_1.add(k, data.get(i));
        	}
        	catch(IndexOutOfBoundsException e) {//���һҳ
        		msg="success";
         	    code="200";
        		result.put("code",code);
        		result.put("msg",msg);
        		result.put("list",data_1);
           		result.put("total",total_1);
        		return result;
        	}
        }
 	
 	
     msg="success";
		code="200";
		result.put("msg",msg);
		result.put("code",code);
		result.put("list",data_1);
   		result.put("total",total_1);
		return result;
    }
    
    total = list.size();

    int j;
    if(list.size()<offsetsize) {//��ҳ��
    	j = list.size();
    }
    else {
        j = offset*offsetsize;
    }
    for(int i= offset*offsetsize-offsetsize,k=0;i<j;i++,k++) {
    	try {
    	data.add(k, list.get(i));
    	}
    	catch(IndexOutOfBoundsException e) {
    		msg="success";
     		code="200";
    		result.put("code",code);
    		result.put("msg",msg);
    		result.put("list",data);
       		result.put("total",total);
    		return result;
    	}
    }
    
    msg="success";
	code="200";
    result.put("msg",msg);
	result.put("code",code);
	result.put("list",data);
	result.put("total",total);
	return result;
}
    
       
    
    
    
    
    @RequestMapping({"/delete"})
    @ResponseBody
    public JSONObject deletearticle(Integer id,String type) {
    	String msg = null;
    	String code = null;
    	JSONObject result=new JSONObject();
    	if(id==null) {
    		msg = "id missing or format error";
        	code = "-1";
        	result.put("msg",msg);
        	result.put("code",code);
        	return result;
        	}
        
    	if(type==null) {
    		msg = "type missing";
        	code = "-2";
        	result.put("msg",msg);
        	result.put("code",code);
        	return result;
        	}
    	if(!(type.equals("news")||type.equals("researcher")||type.equals("project")||type.equals("findings_project")||type.equals("findings_essay")||type.equals("findings_property_right")||type.equals("findings_award"))) {
    		msg = "type error";
        	code = "-4";
        	result.put("msg",msg);
        	result.put("code",code);
        	return result;
    	}
    	int flag=0;
    	
    	if(type.equals("news")) 
    		flag = user_service.delete_article(id);
    	else if(type.equals("researcher"))
    	    flag = user_service.delete_researcher(id);
    	else
    		flag = user_service.delete_findings(id);
    	System.out.println(flag);
        if(flag==0) {
        	msg="delete error";
        	code="-3";
        	result.put("msg",msg);
        	result.put("code",code);
        	return result;
        }
        msg = "success";
    	code = "200";
    	result.put("msg",msg);
    	result.put("code",code);
    	return result;
    }
    
    
    
    
    
    @RequestMapping({"/insertimage"})
    @ResponseBody
    public JSON insertimage(@RequestParam("image")MultipartFile image,HttpServletRequest request) throws IllegalStateException, IOException {
    	String msg=null,code=null;
    	JSONObject result=new JSONObject();
    	if(image==null||image.isEmpty()) {
    		msg="image missing";
    		code="-1";
    		result.put("code",code);
    		result.put("msg",msg);
    		return result;
    	}
    	 String originalFilename = image.getOriginalFilename();
         //2,��ȡԴ�ļ����ļ���ǰ׺,������׺
//         String fileNamePrefix = URLEncoder.encode(originalFilename.substring(0,originalFilename.lastIndexOf(".")),Constants.CHARSET_UTF8);
         //3,�ӹ������ļ�����ԭ�ļ�����ʱ���         
         String newFileNamePrefix  = "image" + System.currentTimeMillis();
         //4,�õ����ļ���
         String fileName = newFileNamePrefix + originalFilename.substring(originalFilename.lastIndexOf("."));
//         String path="/home/laboratory_image/";
         
         
//         String targetPath = "image/";
         String path = request.getSession().getServletContext().getRealPath("/")+"image/";
         String fullpath=path+fileName;
         Image imageinfo=new  Image();
         imageinfo.setLocation(fullpath);  
         System.out.println(path);
         File file = new File(path+fileName);
//         File file = new File(path + fileName);
         /**
          * �ж�·���Ƿ���ڣ���������ھʹ���һ��
          */
         if (!file.getParentFile().exists()) {           
             file.getParentFile().mkdirs();
         }
         image.transferTo(new File(path + File.separator + fileName));
         String indexPath = request.getContextPath();
         String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+indexPath;
         String resultmap = basePath + File.separator + "image" + File.separator + fileName;
         imageinfo.setPath(resultmap);
         user_service.insertimage(imageinfo); 
       // ����ʵ�ʴ洢����ʵ�ļ���
   
         msg="success";
 		 code="200";
 		 result.put("code",code);
 		 result.put("msg",msg);
 		 result.put("imagepath",resultmap);
 		 return result;
    }   
    
   
    
    
    
    
    @SuppressWarnings("unused")
	@RequestMapping({"/updaterecommend"})
    @ResponseBody
    public JSON updaterecommend(@RequestBody String content)  {
    	String msg=null,code=null;
    	JSONObject result=new JSONObject();
    	
     	if(content==null||content=="") {
    		msg="content missing";
    		code="-2";
    		result.put("code",code);
    		result.put("msg",msg);
    		return result;
    	}
    	
     	try {
    	JSONObject content_1=JSON.parseObject(content);
     	}
     	catch (JSONException e){
     		msg="json format error";
    		code="-3";
    		result.put("code",code);
    		result.put("msg",msg);
    		return result;
     	}
     	
    	
//    	if(recommend_id==null) {
//    		msg="recommend_id missing";
//    		code="-1";
//    		result.put("code",code);
//    		result.put("msg",msg);
//    		return result;
//    	}

    	Recommend recommend = new Recommend();
    	recommend.setContent(content);
    	recommend.setRecommendId(1);
        int flag = user_service.updaterecommend(recommend);
        if(flag==0) {
    	   msg="database error";
   		   code="-3";
   		   result.put("code",code);
   		   result.put("msg",msg);
   		   return result;
       }
           msg="success";
		   code="200";
		   result.put("code",code);
		   result.put("msg",msg);
		   return result;
    }
    
    @RequestMapping(value="/selectrecommend", method=RequestMethod.GET)
    @ResponseBody
    public JSON selectrecommend(Integer recommend_id)  {
    	String msg=null,code=null;
    	JSONObject result=new JSONObject();
//    	if(recommend_id==null) {
//    		msg="recommend_id missing";
//    		code="-1";
//    		result.put("code",code);
//    		result.put("msg",msg);
//    		return result;
//    	}
       Recommend recommend=user_service.selectrecommend(1);
       if(recommend==null) {
    	   msg="recommend_id doesn't exit";
   		   code="-2";
   		   result.put("code",code);
   		   result.put("msg",msg);
   		   return result;
       }
           msg="success";
		   code="200";
		   result.put("code",code);
		   result.put("msg",msg);
		   JSONObject content = JSON.parseObject(recommend.getContent());
		   result.put("data",content);
		   return result;
    }
    
    @RequestMapping({"/insertresearcher"})
    @ResponseBody
    public JSON insertresearcher(Researcher researcher)  {
    	String msg=null,code=null;
    	JSONObject result = new JSONObject();
        if(researcher.getName()==null||researcher.getName()=="") {
        	msg="name missing";
        	code="-1";
        	result.put("msg",msg);
        	result.put("code",code);
            return result;
        }
        if(researcher.getTitles()==null||researcher.getTitles()=="") {
        	msg="titles missing";
        	code="-2";
        	result.put("msg",msg);
        	result.put("code",code);
            return result;
        }
        if(researcher.getImagePath()==null||researcher.getImagePath()=="") {
        	msg="imgePath missing";
        	code="-3";
        	result.put("msg",msg);
        	result.put("code",code);
            return result;
        }
        if(researcher.getContent()==null||researcher.getContent()=="") {
        	msg="content missing";
        	code="-4";
        	result.put("msg",msg);
        	result.put("code",code);
            return result;
        }
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(d);
        researcher.setDate(date);
        int flag;
        flag = user_service.insertresearcher(researcher);
        if(flag==0) {
        	msg="database error";
        	code="-5";
        	result.put("msg",msg);
        	result.put("code",code);
            return result;
        }
        msg="success";
        code="200";
        result.put("msg",msg);
    	result.put("code",code);
        return result;
    }
    
    @RequestMapping({"/getresearcherlist"})
    @ResponseBody
    public JSON getresearcherlist(Integer offset,Integer offsetsize) {
    	String code=null;
    	String msg=null;
    	JSONObject result = new JSONObject();
    	
     	if(offset==null) {
    		msg="offset missing";
     		code="-1";
    		result.put("code",code);
    		result.put("msg",msg);
    		return result;
    	}
     	if(offsetsize==null) {
    		msg="offsetsize missing";
     		code="-2";
    		result.put("code",code);
    		result.put("msg",msg);
    		return result;
    	}
    	List<researcher_list>  list_1 = new ArrayList<researcher_list>();  
        int total;
        list_1 = user_service.select_researcher_list();     
        total = list_1.size();
        
     
           
        List<researcher_list> data = new ArrayList<researcher_list>();

                          
        int j ;

        if(list_1.size()<offsetsize) {//��ҳ��
        	j = list_1.size();
        }
        else {
            j = offset*offsetsize;
        }
        for(int i= offset*offsetsize-offsetsize,k=0;i<j;i++,k++) {
        	try {
        	data.add(k, list_1.get(i));
        	}
        	catch(IndexOutOfBoundsException e) {
        		msg="success";
         		code="200";
        		result.put("code",code);
        		result.put("msg",msg);
        		result.put("list",data);
           		result.put("total",total);
        		return result;
        	}
        }
        
        msg="success";
 		code="200";
 		result.put("msg",msg);
		result.put("code",code);
		result.put("list",data);
   		result.put("total",total);
		return result;
    	
    }
    
    
    
    @RequestMapping({"/updatenews"})
    @ResponseBody
    public JSON updatenews(Integer id,String text,String title,String username,String photosource,String cover,HttpServletRequest request)  {
    	String msg=null,code=null;
    	JSONObject result=new JSONObject();
    	if(text==null||text=="") {
    		msg="text missing or format error";
    		code="-1";
    		result.put("msg",msg);
    		result.put("code",code);
    		return result;
    	}
   	
    	if(title==null||title=="") {
    		msg="title missing";
    		code="-2";
    		result.put("msg",msg);
    		result.put("code",code);
    		return result;
    	}
    	
    	if(username==null||username=="") {
    		msg="username missing";
    		code="-3";
    		result.put("msg",msg);
    		result.put("code",code);
    		return result;
    	}
    	
    	if(photosource==null||photosource=="") {
    		msg="photosource missing";
    		code="-4";
    		result.put("msg",msg);
    		result.put("code",code);
    		return result;
    	}
    	
    	if(cover==null||cover=="") {
    		msg="cover missing";
    		code="-5";
    		result.put("msg",msg);
    		result.put("code",code);
    		return result;
    	}
    	if(id==null) {
    		msg="id missing";
    		code="-7";
    		result.put("msg",msg);
    		result.put("code",code);
    		return result;
    	}
    	Article article=new Article();
    	article.setArticleId(id);
    	article.setUsername(username);
    	article.setCoverImage(cover);
    	article.setPhotosource(photosource);
       	article.setTitle(title);
    	Date d = new Date();
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    	String date =sdf.format(d);
    	article.setDate(date);
        article.setContent(text);
       	int  flag=user_service.updatearticle(article);
    	if(flag==0) {
    		msg="article doesn't exit or database error";
    		code="-6";
    		result.put("msg",msg);
    		result.put("code",code);
    		return result;
    	}
 
        msg="success";
		code="200";
		result.put("msg",msg);
		result.put("code",code);
        return result;
    }
    
    @RequestMapping({"/updateresearcher"})
    @ResponseBody
    public JSON updateresearcher(Researcher researcher)
    {
    	String msg=null,code=null;
    	JSONObject result = new JSONObject();
        if(researcher.getName()==null||researcher.getName()=="") {
        	msg="name missing";
        	code="-1";
        	result.put("msg",msg);
        	result.put("code",code);
            return result;
        }
        if(researcher.getTitles()==null||researcher.getTitles()=="") {
        	msg="titles missing";
        	code="-2";
        	result.put("msg",msg);
        	result.put("code",code);
            return result;
        }
        if(researcher.getImagePath()==null||researcher.getImagePath()=="") {
        	msg="imgePath missing";
        	code="-3";
        	result.put("msg",msg);
        	result.put("code",code);
            return result;
        }
        if(researcher.getContent()==null||researcher.getContent()=="") {
        	msg="content missing";
        	code="-4";
        	result.put("msg",msg);
        	result.put("code",code);
            return result;
        }
        if(researcher.getId()==null) {
        	msg="id missing";
        	code="-6";
        	result.put("msg",msg);
        	result.put("code",code);
            return result;
        }
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(d);
        researcher.setDate(date);
        int flag;
        flag = user_service.updateresearcher(researcher);
        if(flag==0) {
        	msg="researcher doesn't exit or database error";
        	code="-5";
        	result.put("msg",msg);
        	result.put("code",code);
            return result;
        }
        msg="success";
        code="200";
        result.put("msg",msg);
    	result.put("code",code);
        return result;
    }
  
   
    @RequestMapping({"/updateother"})
    @ResponseBody
    public JSON updateother(String type,String title,String text,Integer id,HttpServletRequest request){

    	JSONObject result =new JSONObject();
    	String msg=null,code=null;
    	if(text==null||text=="") {
    		msg="text missing or format error";
    		code="-1";
    		result.put("msg",msg);
    		result.put("code",code);
    		return result;
    	}
    	if(title==null||title=="") {
    		msg="title missing";
    		code="-2";
    		result.put("msg",msg);
    		result.put("code",code);
    		return result;
    	}
    	if(type==null||type=="") {
    		msg="type missing";
    		code="-3";
    		result.put("msg",msg);
    		result.put("code",code);
    		return result;
    	}
    	if((!type.equals("project"))&&(!type.equals("findings_project"))&&(!type.equals("findings_essay"))&&(!type.equals("findings_property_right"))&&(!type.equals("findings_award"))) {
    		msg="type error";
    		code="-4";
    		result.put("msg",msg);
    		result.put("code",code);
    		return result;
    	}   
    	if(id==null) {
    		msg="id missing";
    		code="-6";
    		result.put("msg",msg);
    		result.put("code",code);
    		return result;
    	}
    	Findings other=new Findings();   
    	other.setId(id);
       	other.setTitle(title);
    	Date d = new Date();
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    	String date =sdf.format(d);
    	other.setDate(date);
        other.setContent(text);
        int flag;
    	other.setType(type);
        flag=user_service.updatefindings(other);
    	if(flag==0) {
    		msg="it doesn't exit or database error";
    		code="-5";
    		result.put("msg",msg);
    		result.put("code",code);
    		return result;
    	}
 
        msg="success";
		code="200";
		result.put("msg",msg);
		result.put("code",code);
        return result;
    }
}

















//News news = new News();
//Findings findings=new Findings();
//Project project=new Project();
//String path=new String();
//if(type=="news") {
//    news=user_service.selectdetail_by_id(article_id);
//    if(news!=null)
//	path = news.getNews();
//}
//if(type=="findings") {
//    findings = user_service.selectfindings_by_id(article_id);
//    if(findings!=null)
//    path = findings.getFinding();
//}
//if(type=="project") {
//	project = user_service.selectproject_by_id(article_id);
//    if(project!=null)
//	path = project.getProject();
//}
//if(news==null&&findings==null&&project==null) {
//    msg="article doesn't exit";
//	code="-3";
//	result.put("msg",msg);
//	result.put("code",code);
//    return result; 
//}
//String path = request.getSession().getServletContext().getRealPath("/static/");

//String projectServerPath = request.getScheme() + "://"+request.getServerName()+":" +request.getServerPort() + request.getContextPath() + "/rtffile/";	

//String fileNameExtension = fileName.substring(fileName.indexOf("."), fileName.length());
// ����ʵ�ʴ洢����ʵ�ļ���
//String realName = UUID.randomUUID().toString() + fileNameExtension;//��߰�ȫ��
//String resultmap=projectServerPath+realName;   
//@RequestMapping({"/newsdetail"})
//@ResponseBody
//public JSONObject getdetail(Integer news_id) {
////	File f = new File("a.txt");
////    FileOutputStream fop = new FileOutputStream(f);
////    // ����FileOutputStream����,�ļ������ڻ��Զ��½�
////
////    OutputStreamWriter writer = new OutputStreamWriter(fop, "UTF-8");
//	News news=user_service.getnewsdetail(news_id);
//	String temp=news.getNews();
//	byte [] detail=temp.getBytes();
//	OutputStream os = new FileOutputStream("test.rtf");
//	OutputStreamWriter writer = new OutputStreamWriter(os, "UTF-8");
//    writer.append(temp);
//    writer.close();
//    return (test.rtf);
//}
//@RequestMapping({"/insertrecommend"})
//@ResponseBody
//public JSON insertrecommend(String content)  {
//	String msg=null,code=null;
//	JSONObject result=new JSONObject();
//	if(content==null||content.isEmpty()) {
//		msg="content missing";
//		code="-1";
//		result.put("code",code);
//		result.put("msg",msg);
//		return result;
//	}
//   Recommend recommend = new Recommend();
//   
//   Date d=new Date();
//   SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//   String date = sdf.format(d);
//   
//   recommend.setDate(date);
//   
//   recommend.setContent(content);
//   int flag = user_service.insertrecommend(recommend);
//   if(flag==0) {
//	   msg="database error";
//		   code="-2";
//		   result.put("code",code);
//		   result.put("msg",msg);
//		   return result;
//   }
//   Integer recommend_id=recommend.getRecommendId();
//       msg="success";
//	   code="200";
//	   result.put("code",code);
//	   result.put("msg",msg);
//	   result.put("recommend_id",recommend_id);
//	   return result;
//}
//@RequestMapping({"/deleterecommend"})
//@ResponseBody
//public JSON deleterecommend(Integer recommend_id)  {
//	String msg=null,code=null;
//	JSONObject result=new JSONObject();
//	if(recommend_id==null) {
//		msg="recommend_id missing";
//		code="-1";
//		result.put("code",code);
//		result.put("msg",msg);
//		return result;
//	}
//   int flag = user_service.deleterecommend(recommend_id);
//   if(flag==0) {
//	   msg="database error";
//		   code="-2";
//		   result.put("code",code);
//		   result.put("msg",msg);
//		   return result;
//   }
//       msg="success";
//	   code="200";
//	   result.put("code",code);
//	   result.put("msg",msg);
//	   return result;
//}

	/**
 * ���ļ������в�����ֹ�ļ�����
 */
//1����ȡԭʼ�ļ���
//String originalFilename = cover.getOriginalFilename();
//2,��ȡԴ�ļ����ļ���ǰ׺,������׺
//String fileNamePrefix = originalFilename.substring(0,originalFilename.lastIndexOf("."));
//3,�ӹ������ļ�����ԭ�ļ�����ʱ���
//String newFileNamePrefix  = fileNamePrefix + System.currentTimeMillis();
//4,�õ����ļ���
//String fileName = newFileNamePrefix + originalFilename.substring(originalFilename.lastIndexOf("."));
//String path="/home/laboratory_image/";
//
//
//String indexPath = request.getContextPath();
//String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+indexPath;
//String resultmap = basePath + File.separator + "image" + File.separator + fileName;
//String path = request.getSession().getServletContext().getRealPath("/")+"image/";
//String fullpath=path+fileName;
//Image imageinfo=new  Image();
//imageinfo.setPath(resultmap);
//user_service.insertimage(imageinfo);   
//System.out.println(path);
//File file = new File(path+fileName);
//if (!file.getParentFile().exists()) {           
// file.getParentFile().mkdirs();
//}
//cover.transferTo(new File(path + File.separator + fileName));
//System.out.println("==========="+path+"====");
//File file = new File(path + fileName);
///**
//* �ж�·���Ƿ���ڣ���������ھʹ���һ��
//*/
//if (!file.getParentFile().exists()) {           
//  file.getParentFile().mkdirs();
//}
//newsfile.transferTo(new File(path + File.separator + fileName));
//String path="E:\\eclipse\\";
//String path="/home/laboratory_rtf/";//�����Ʒ������ǵ��޸�
//String fullpath=path+fileName;
//String path = article.getContent();
//File file=new File(path);
//String filename=file.getName();
//System.out.println("==========="+filename+"===========");
//response.setHeader("Content-Disposition", "attachment; filename=" + java.net.URLEncoder.encode(filename, "UTF-8"));
////�����ļ�ContentType���ͣ��������ã����Զ��ж������ļ�����
//response.setContentType("multipart/form-data");
//response.setCharacterEncoding("UTF-8");
//InputStream inputStream = new BufferedInputStream(new FileInputStream(new File(path)));
//BufferedOutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
////����������
//byte[] buffer = new byte[1024];
//int length;
//while ((length = inputStream.read(buffer)) != -1) {
//  outputStream.write(buffer, 0, length);
//  outputStream.flush();
//}
////�ر���
//inputStream.close();
//outputStream.close();