package com.ideabobo.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpStatus;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import com.ideabobo.util.*;
import com.ideabobo.util.wxlogin.WxLoginUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.ideabobo.model.Dbservice;
import com.ideabobo.model.Dbtablemapping;
import com.ideabobo.service.DatabaseService;

@CrossOrigin(origins="*")
@Controller
@RequestMapping(value = "/database")
public class DatabaseController {
    //private static final Logger logger = Logger.getLogger(DatabaseController.class);
    @Autowired
	private DatabaseService databaseService;
    
    @RequestMapping(value = "/list", produces = "application/json; charset=utf-8", method = { RequestMethod.GET,
			RequestMethod.POST })
    @ResponseBody
    public List<Map<String,Object>> list(HttpServletRequest req) {
    	Dbservice dbm = new Dbservice(databaseService);
    	String table = Dbservice.getTableName(req.getParameter("table"));
    	Object tableObj = Dbtablemapping.getModelByTable(table);
    	if (tableObj==null){
    		return null;
		}
    	Object objectObj = Common.getByRequest(tableObj, req, false);
    	//Robj robj = new Robj();
    	List<Map<String,Object>> list = null;
    	try {
			String sql = dbm.list(table,objectObj,null);
			list = databaseService.find(sql);
		} catch (Exception e) {
			
			e.printStackTrace();
		}  	
        return list;
    }
    
    @RequestMapping(value = "/listJ", produces = "text/plain; charset=utf-8", method = { RequestMethod.GET,
			RequestMethod.POST })
    @ResponseBody
    public String listJ(HttpServletRequest req) {
    	Dbservice dbm = new Dbservice(databaseService);
    	String table = Dbservice.getTableName(req.getParameter("table"));
    	Object objectObj = Common.getByRequest(Dbtablemapping.getModelByTable(table), req, false);
    	//Robj robj = new Robj();
    	List<Map<String,Object>> list = null;
    	try {
    		String ordersql = null;
    		String sort = req.getParameter("sort");
    		String order = req.getParameter("order");
    		if (StringUtil.isNotNullOrEmpty(order)&&StringUtil.isNotNullOrEmpty(sort)){
    			ordersql = " order by "+sort+" "+order;
			}
			String sql = dbm.list(table,objectObj,ordersql);
			list = databaseService.find(sql);
		} catch (Exception e) {
			
			e.printStackTrace();
		}  	
        return renderJsonp(list, req);
    }
    
    @RequestMapping(value = "/find", produces = "application/json; charset=utf-8", method = { RequestMethod.GET,
			RequestMethod.POST })
    @ResponseBody
    public Map<String,Object> find(HttpServletRequest req) {
    	Dbservice dbm = new Dbservice(databaseService);
    	String table = Dbservice.getTableName(req.getParameter("table"));
    	Object objectObj = Common.getByRequest(Dbtablemapping.getModelByTable(table), req, false);
    	//Robj robj = new Robj();
    	List<Map<String,Object>> list = null;
    	try {
			String sql = dbm.list(table,objectObj,true);
			list = databaseService.find(sql);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
    	if (list!=null && list.size()>0) {
    		return list.get(0);
		}else{
			return null;
		}
        
    }
    
    @RequestMapping(value = "/findJ", produces = "text/plain; charset=utf-8", method = { RequestMethod.GET,
			RequestMethod.POST })
    @ResponseBody
    public String findJ(HttpServletRequest req) {
    	Dbservice dbm = new Dbservice(databaseService);
    	String table = Dbservice.getTableName(req.getParameter("table"));
    	Object objectObj = Common.getByRequest(Dbtablemapping.getModelByTable(table), req, false);
    	//Robj robj = new Robj();
    	List<Map<String,Object>> list = null;
    	try {
			String sql = dbm.list(table,objectObj,true);
			list = databaseService.find(sql);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
    	if (list!=null && list.size()>0) {
    		return renderJsonp(list.get(0), req);
		}else{
			return renderJsonp(null, req);
		}
        
    }
    

    
    
    @RequestMapping(value = "/listApp", produces = "application/json; charset=utf-8", method = { RequestMethod.GET,
			RequestMethod.POST })
    @ResponseBody
    public List<Map<String,Object>> listApp(HttpServletRequest req) {
    	Dbservice dbm = new Dbservice(databaseService);
    	String table = Dbservice.getTableName(req.getParameter("table"));
    	//Robj robj = new Robj();
    	List<Map<String,Object>> list = null;
    	try {
    		String object = req.getParameter("object");
    		//if(StringUtil.isNotNullOrEmpty(object)){
    			Object objectObj = Dbtablemapping.parseStringModel(object, table);
    			String sql = dbm.list(table,objectObj,null);
    			list = databaseService.find(sql);
    	    	//robj.setData(databaseService.find(sql));
    		//}
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}  	
        return list;
    }
    
    @RequestMapping(value = "/listC", produces = "text/plain; charset=utf-8", method = { RequestMethod.GET,
			RequestMethod.POST })
    @ResponseBody
    public String listC(HttpServletRequest req) {
    	Dbservice dbm = new Dbservice(databaseService);
    	String table = Dbservice.getTableName(req.getParameter("table"));
    	//Robj robj = new Robj();
    	List<Map<String,Object>> list = null;
    	try {
    		String object = req.getParameter("object");
    		//if(StringUtil.isNotNullOrEmpty(object)){
    			Object objectObj = Dbtablemapping.parseStringModel(object, table);
    			String sql = dbm.list(table,objectObj,null);
    			list = databaseService.find(sql);
    	    	//robj.setData(databaseService.find(sql));
    		//}
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}  	
        return renderJsonp(list, req);
    }
    
    @RequestMapping(value = "/findApp", produces = "application/json; charset=utf-8", method = { RequestMethod.GET,
			RequestMethod.POST })
    @ResponseBody
    public Map<String,Object> findApp(HttpServletRequest req) {
    	Dbservice dbm = new Dbservice(databaseService);
    	String table = Dbservice.getTableName(req.getParameter("table"));
    	//Robj robj = new Robj();
    	List<Map<String,Object>> list = null;
    	try {
    		String object = req.getParameter("object");
    		//if(StringUtil.isNotNullOrEmpty(object)){
    			Object objectObj = Dbtablemapping.parseStringModel(object, table);
    			String sql = dbm.list(table,objectObj,null);
    			list = databaseService.find(sql);
    	    	//robj.setData(databaseService.find(sql));
    		//}
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}  	
    	if (list!=null && list.size()>0) {
    		return list.get(0);
		}else{
			return null;
		}
    }
    
    @RequestMapping(value = "/findC", produces = "text/plain; charset=utf-8", method = { RequestMethod.GET,
			RequestMethod.POST })
    @ResponseBody
    public String findC(HttpServletRequest req) {
    	Dbservice dbm = new Dbservice(databaseService);
    	String table = Dbservice.getTableName(req.getParameter("table"));
    	//Robj robj = new Robj();
    	List<Map<String,Object>> list = null;
    	try {
    		String object = req.getParameter("object");
    		//if(StringUtil.isNotNullOrEmpty(object)){
    			Object objectObj = Dbtablemapping.parseStringModel(object, table);
    			String sql = dbm.list(table,objectObj,null);
    			list = databaseService.find(sql);
    	    	//robj.setData(databaseService.find(sql));
    		//}
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}  	
    	if (list!=null && list.size()>0) {
    		return renderJsonp(list.get(0), req);
		}else{
			return renderJsonp(null, req);
		}
    }
    
    @RequestMapping(value = "/listPage", produces = "application/json; charset=utf-8", method = { RequestMethod.GET,
			RequestMethod.POST })
    @ResponseBody
    public Page listPage(HttpServletRequest req) {
    	Dbservice dbm = new Dbservice(databaseService);
    	String table = Dbservice.getTableName(req.getParameter("table"));
    	Page page = new Page();
    	//Robj robj = new Robj();
    	try {
    		Object model = Common.getByRequest(Dbtablemapping.getModelByTable(table), req, false);
    		page.model = model;
    		String order = req.getParameter("order");
			String sort = req.getParameter("sort");
			String pageNo = req.getParameter("page");
			String pageSize = req.getParameter("rows");
			if (pageSize==null || pageSize.equals("")){
				pageSize = req.getParameter("limit");
			}
			page = dbm.getByPage(page, table,sort,order,pageNo,pageSize);
	    	//robj.setData(page);
		} catch (Exception e) {
			
			e.printStackTrace();
		}  	
        return page;
    }
    
    @RequestMapping(value = "/listPageJ", produces = "text/plain; charset=utf-8", method = { RequestMethod.GET,
			RequestMethod.POST })
    @ResponseBody
    public String listPageJ(HttpServletRequest req) {
    	Dbservice dbm = new Dbservice(databaseService);
    	String table = Dbservice.getTableName(req.getParameter("table"));
    	Page page = new Page();
    	//Robj robj = new Robj();
    	try {
    		Object model = Common.getByRequest(Dbtablemapping.getModelByTable(table), req, false);
    		page.model = model;
			page = dbm.getByPage(page, table,null,null,null,null);
	    	//robj.setData(page);
		} catch (Exception e) {
			
			e.printStackTrace();
		}  	
        return renderJsonp(page, req);
    }
    
    @RequestMapping(value = "/listPageApp", produces = "application/json; charset=utf-8", method = { RequestMethod.GET,
			RequestMethod.POST })
    @ResponseBody
    public Page listPageApp(HttpServletRequest req) {
    	Dbservice dbm = new Dbservice(databaseService);
    	String table = Dbservice.getTableName(req.getParameter("table"));
    	Page page = new Page();
    	//Robj robj = new Robj();
    	try {
    		String object = req.getParameter("object");
    		if(StringUtil.isNotNullOrEmpty(object)){
    			page.model = Dbtablemapping.parseStringModel(object, table);
    		}
			page = dbm.getByPage(page, table,null,null,null,null);
	    	//robj.setData(page);
		} catch (Exception e) {
			
			e.printStackTrace();
		}  	
        return page;
    }
    
    @RequestMapping(value = "/listPageC", produces = "text/plain; charset=utf-8", method = { RequestMethod.GET,
			RequestMethod.POST })
    @ResponseBody
    public String listPageC(HttpServletRequest req) {
    	Dbservice dbm = new Dbservice(databaseService);
    	String table = Dbservice.getTableName(req.getParameter("table"));
    	Page page = new Page();
    	//Robj robj = new Robj();
    	try {
    		String object = req.getParameter("object");
    		if(StringUtil.isNotNullOrEmpty(object)){
    			page.model = Dbtablemapping.parseStringModel(object, table);
    		}
			page = dbm.getByPage(page, table,null,null,null,null);
	    	//robj.setData(page);
		} catch (Exception e) {
			
			e.printStackTrace();
		}  	
        return renderJsonp(page, req);
    }
    
    
    @RequestMapping(value = "/addApp", produces = "text/plain; charset=utf-8", method = { RequestMethod.GET,
			RequestMethod.POST })
    @ResponseBody
    public String addApp(HttpServletRequest req) {
    	Dbservice dbm = new Dbservice(databaseService);
    	//Robj robj = new Robj();
    	String table = Dbservice.getTableName(req.getParameter("table"));
    	String object = req.getParameter("object");
    	Object obj = Dbtablemapping.parseStringModel(object, table);
    	try {
    		String sql = dbm.add(obj, table);
    		databaseService.executeAction(sql);
		} catch (Exception e) {
			
			return "操作失败"+e.getMessage();
		}
    	
    	return "success!";
        
    }
    
    @RequestMapping(value = "/add", produces = "text/plain; charset=utf-8", method = { RequestMethod.GET,
			RequestMethod.POST })
    @ResponseBody
    public String add(HttpServletRequest req) {
    	Dbservice dbm = new Dbservice(databaseService);
    	//Robj robj = new Robj();
    	String tableReq = req.getParameter("table");
    	String table = Dbservice.getTableName(tableReq);
    	Object model = Common.getByRequest(Dbtablemapping.getModelByTable(table), req, false);
    	try {
    		String sql = dbm.add(model, table);
    		databaseService.executeAction(sql);
		} catch (Exception e) {
			
			return "操作失败"+e.getMessage();
		}
    	
    	return "success!";
        
    }
    
    @RequestMapping(value = "/saveWithFile", produces = "text/plain; charset=utf-8", method = { RequestMethod.GET,
			RequestMethod.POST })
    @ResponseBody
    public String saveWithFile(@RequestParam(value = "file", required = false) MultipartFile[] files,HttpServletRequest req) {
    	Dbservice dbm = new Dbservice(databaseService);
		String rstr = "0";
    	try {
    		String fileNames=null;
    		if(files!=null && files.length>0){
    			long fn = files[0].getSize();
    			if(fn>1){
    				fileNames = Common.copyFile2Upload(files);
    			}
    			
    		}
    		String tableReq = req.getParameter("table");
    		String fileField = req.getParameter("fileName");
        	String table = Dbservice.getTableName(tableReq);
        	Object model = Common.getByRequestSetfile(Dbtablemapping.getModelByTable(table),fileField,fileNames, req, false);
    		String sql = dbm.save(model, table);
    		databaseService.executeAction(sql);
			/**
			 * 判断是否插入操作,如果是返回插入的id
			 */
			String id = req.getParameter("id");

			if(id==null || id.equals("")){
				List<Map<String, Object>> rlist = databaseService.find("select LAST_INSERT_ID() as lastId");
				if(rlist!=null){
					rstr = rlist.get(0).get("lastId")+"";
				}
			}else{
				rstr = id;
			}
		} catch (Exception e) {
			
			return "操作失败"+e.getMessage();
		}
    	
    	return rstr;
        
    }
    
    @RequestMapping(value = "/upload", produces = "text/plain; charset=utf-8", method = { RequestMethod.GET,
			RequestMethod.POST })
    @ResponseBody
    public String upload(@RequestParam(value = "file", required = false) MultipartFile[] files,HttpServletRequest req) {
    	String fileNames=null;
    	try {
    		if(files!=null && files.length>0){
    			fileNames = Common.copyFile2Upload(files);
    		}
    		
		} catch (Exception e) {
		}
    	return fileNames;
        
    }

	@RequestMapping(value = "/editorUploadVideo", produces = "text/plain; charset=utf-8", method = { RequestMethod.GET,
			RequestMethod.POST })
	@ResponseBody
	public String editorUploadVideo(@RequestParam(value = "file", required = false) MultipartFile[] files,HttpServletRequest req) {
		String fileNames=null;
		String jsonobj = null;
		try {
			if(files!=null && files.length>0){
				fileNames = Common.copyFile2Upload(files);
				ArrayList<String> nl = new ArrayList<String>();
				String[] fns = fileNames.split(",");
				String url = "upload/"+fns[0];
				jsonobj = "{\"errno\":0,\"data\":{\"url\":\""+url+"\"}}";
			}

		} catch (Exception e) {
		}
		return jsonobj;

	}

	@RequestMapping(value = "/editorUploadVideoJ", produces = "text/plain; charset=utf-8", method = { RequestMethod.GET,
			RequestMethod.POST })
	@ResponseBody
	public String editorUploadVideoJ(@RequestParam(value = "file", required = false) MultipartFile[] files,HttpServletRequest req) {
		String fileNames=null;
		String jsonobj = null;
		try {
			if(files!=null && files.length>0){
				fileNames = Common.copyFile2Upload(files);
				ArrayList<String> nl = new ArrayList<String>();
				String[] fns = fileNames.split(",");
				String url = fns[0];
				jsonobj = "{\"errno\":0,\"data\":{\"url\":\""+url+"\"}}";
			}

		} catch (Exception e) {
		}
		return jsonobj;

	}

	@RequestMapping(value = "/editorUpload", produces = "text/plain; charset=utf-8", method = { RequestMethod.GET,
			RequestMethod.POST })
	@ResponseBody
	public String editorUpload(@RequestParam(value = "file", required = false) MultipartFile[] files,HttpServletRequest req) {
		String fileNames=null;
		EditorUpload eu = new EditorUpload();
		eu.setErrno(0);
		try {
			if(files!=null && files.length>0){
				fileNames = Common.copyFile2Upload(files);
				ArrayList<String> nl = new ArrayList<String>();
				String[] fns = fileNames.split(",");
				for (int i = 0; i < fns.length; i++) {
					nl.add("upload/"+fns[i]);
				}
				eu.setData(nl);
			}

		} catch (Exception e) {
		}
		return JSON.toJSONString(eu);

	}

	@RequestMapping(value = "/editorUploadJ", produces = "text/plain; charset=utf-8", method = { RequestMethod.GET,
			RequestMethod.POST })
	@ResponseBody
	public String editorUploadJ(@RequestParam(value = "file", required = false) MultipartFile[] files,HttpServletRequest req) {
		String fileNames=null;
		EditorUpload eu = new EditorUpload();
		eu.setErrno(0);
		try {
			if(files!=null && files.length>0){
				fileNames = Common.copyFile2Upload(files);
				ArrayList<String> nl = new ArrayList<String>();
				String[] fns = fileNames.split(",");
				for (int i = 0; i < fns.length; i++) {
					nl.add(fns[i]);
				}
				eu.setData(nl);
			}

		} catch (Exception e) {
		}
		return JSON.toJSONString(eu);

	}
    
    @RequestMapping(value = "/createQrcode", produces = "text/plain; charset=utf-8", method = { RequestMethod.GET,
			RequestMethod.POST })
    @ResponseBody
    public String createQrcode(HttpServletRequest req) {
    	String fileNames="";
    	try {
    		String path = ResourceUtils.getURL("classpath:").getPath();
            String destPath = path+File.separator+"static"+File.separator+"upload"+File.separator;
            String content = req.getParameter("code");
        	fileNames=QRCodeUtil.encode(content, null, destPath, true);
		} catch (Exception e) {
			
		}
    	return fileNames;
        
    }
    
    @RequestMapping(value = "/createQrcodeJ", produces = "text/plain; charset=utf-8", method = { RequestMethod.GET,
			RequestMethod.POST })
    @ResponseBody
    public String createQrcodeJ(HttpServletRequest req) {
    	String fileNames="";
    	try {
    		String path = ResourceUtils.getURL("classpath:").getPath();
            String destPath = path+File.separator+"static"+File.separator+"upload"+File.separator;
            String content = req.getParameter("code");
        	fileNames=QRCodeUtil.encode(content, null, destPath, true);
		} catch (Exception e) {
			
		}
    	return renderJsonpString(fileNames, req);
    }
    
    private String renderJsonpString(String str,HttpServletRequest req){
    	Map<String,String> obj = new HashMap<>();
		obj.put("info", str);
		String callbackFunctionName = req.getParameter("callback");	
		String json = JSON.toJSONString(obj);
		String r = callbackFunctionName+"("+json+")";
		return r;
    }
    
    private String renderJsonp(Object obj,HttpServletRequest req){
		String callbackFunctionName = req.getParameter("callback");	
		String json = JSON.toJSONString(obj);
		String r = callbackFunctionName+"("+json+")";
		return r;
    }
    
    @RequestMapping(value = "/updateApp", produces = "text/plain; charset=utf-8", method = { RequestMethod.GET,
			RequestMethod.POST })
    @ResponseBody
    public String updateApp(HttpServletRequest req) {
    	Dbservice dbm = new Dbservice(databaseService);
    	//Robj robj = new Robj();
    	String table = Dbservice.getTableName(req.getParameter("table"));
    	String object = req.getParameter("object");
    	Object obj = Dbtablemapping.parseStringModel(object, table);
    	try {
    		String sql = dbm.update(obj, table);
    		databaseService.executeAction(sql);
		} catch (Exception e) {
			
			return "操作失败"+e.getMessage();
		}
    	
    	return "success!";
        
    }
    
    
    @RequestMapping(value = "/update", produces = "text/plain; charset=utf-8", method = { RequestMethod.GET,
			RequestMethod.POST })
    @ResponseBody
    public String update(HttpServletRequest req) {
    	Dbservice dbm = new Dbservice(databaseService);
    	//Robj robj = new Robj();
    	String tableReq = req.getParameter("table");
    	String table = Dbservice.getTableName(tableReq);
    	Object model = Common.getByRequest(Dbtablemapping.getModelByTable(table), req, false);
    	try {
    		String sql = dbm.update(model, table);
    		databaseService.executeAction(sql);
		} catch (Exception e) {
			
			return "操作失败"+e.getMessage();
		}
    	
    	return "success!";
        
    }
    
    @RequestMapping(value = "/updateSqlJ", produces = "text/plain; charset=utf-8", method = { RequestMethod.GET,
			RequestMethod.POST })
    @ResponseBody
    public String updateSqlJ(HttpServletRequest req) {
    	String sql = req.getParameter("sql");
    	try {
    		databaseService.executeAction(sql);
		} catch (Exception e) {
			
			return renderJsonpString("0", req);
		}
    	
    	return renderJsonpString("1", req);
        
    }
    
    @RequestMapping(value = "/listSqlJ", produces = "text/plain; charset=utf-8", method = { RequestMethod.GET,
			RequestMethod.POST })
    @ResponseBody
    public String listSqlJ(HttpServletRequest req) {
    	String sql = req.getParameter("sql");
    	List<Map<String,Object>> list = null;
    	try {
			list = databaseService.find(sql);
		} catch (Exception e) {
			
			e.printStackTrace();
		}  	
        return renderJsonp(list, req);
    }
    
    
    @RequestMapping(value = "/updateSql", produces = "text/plain; charset=utf-8", method = { RequestMethod.GET,
			RequestMethod.POST })
    @ResponseBody
    public String updateSql(HttpServletRequest req) {
    	String sql = req.getParameter("sql");
    	try {
    		databaseService.executeAction(sql);
		} catch (Exception e) {
			
			return "操作失败"+e.getMessage();
		}
    	
    	return "success!";
        
    }

	@RequestMapping(value = "/listSql", produces = "application/json; charset=utf-8", method = { RequestMethod.GET,
			RequestMethod.POST })
	@ResponseBody
	public List<Map<String,Object>> listSql(HttpServletRequest req) {
		String sql = req.getParameter("sql");
		List<Map<String,Object>> list = null;
		try {
			list = databaseService.find(sql);
		} catch (Exception e) {

			e.printStackTrace();
		}
		return list;
	}
    
    @RequestMapping(value = "/save", produces = "text/plain; charset=utf-8", method = { RequestMethod.GET,
			RequestMethod.POST })
    @ResponseBody
    public String save(HttpServletRequest req) {
    	Dbservice dbm = new Dbservice(databaseService);
    	//Robj robj = new Robj();
    	String tableReq = req.getParameter("table");
    	String table = Dbservice.getTableName(tableReq);
    	Object model = Common.getByRequest(Dbtablemapping.getModelByTable(table), req, false);
    	String rstr = "";
    	try {
    		String sql = dbm.save(model, table);
    		databaseService.executeAction(sql);
			/**
			 * 判断是否插入操作,如果是返回插入的id
			 */
			String id = req.getParameter("id");

			if(id==null || id.equals("")){
				List<Map<String, Object>> rlist = databaseService.find("select LAST_INSERT_ID() as lastId");
				if(rlist!=null){
					rstr = rlist.get(0).get("lastId")+"";
				}
			}else{
				rstr = id;
			}
		} catch (Exception e) {
			
			return "操作失败"+e.getMessage();
		}
    	
    	return rstr;
        
    }
    
    @RequestMapping(value = "/saveJ", produces = "text/plain; charset=utf-8", method = { RequestMethod.GET,
			RequestMethod.POST })
    @ResponseBody
    public String saveJ(HttpServletRequest req) {
    	Dbservice dbm = new Dbservice(databaseService);
    	//Robj robj = new Robj();
    	String tableReq = req.getParameter("table");
    	String table = Dbservice.getTableName(tableReq);
    	Object model = Common.getByRequest(Dbtablemapping.getModelByTable(table), req, false);
    	String rstr = "0";
    	try {
    		String sql = dbm.save(model, table);
    		databaseService.executeAction(sql);
    		/**
    		 * 判断是否插入操作,如果是返回插入的id
    		 */
    		String id = req.getParameter("id");
    		rstr = "1";
    		if(id==null || id.equals("")){
    			List<Map<String, Object>> rlist = databaseService.find("select LAST_INSERT_ID() as lastId");
    			if(rlist!=null){
    				rstr = rlist.get(0).get("lastId")+"";
    			}
    		}
		} catch (Exception e) {
			
			return renderJsonpString(rstr, req);
		}
    	
    	return renderJsonpString(rstr, req);
        
    }
    
    @RequestMapping(value = "/saveApp", produces = "text/plain; charset=utf-8", method = { RequestMethod.GET,
			RequestMethod.POST })
    @ResponseBody
    public String saveApp(HttpServletRequest req) {
    	Dbservice dbm = new Dbservice(databaseService);
    	//Robj robj = new Robj();
    	String table = Dbservice.getTableName(req.getParameter("table"));
    	String object = req.getParameter("object");
    	Object obj = Dbtablemapping.parseStringModel(object, table);
    	try {
    		String sql = dbm.save(obj, table);
    		databaseService.executeAction(sql);
		} catch (Exception e) {
			
			return "操作失败"+e.getMessage();
		}
    	
    	return "success!";
        
    }
    
    @RequestMapping(value = "/saveC", produces = "text/plain; charset=utf-8", method = { RequestMethod.GET,
			RequestMethod.POST })
    @ResponseBody
    public String saveC(HttpServletRequest req) {
    	Dbservice dbm = new Dbservice(databaseService);
    	//Robj robj = new Robj();
    	String table = Dbservice.getTableName(req.getParameter("table"));
    	String object = req.getParameter("object");
    	Object obj = Dbtablemapping.parseStringModel(object, table);
    	try {
    		String sql = dbm.save(obj, table);
    		databaseService.executeAction(sql);
		} catch (Exception e) {
			return renderJsonpString("操作失败"+e.getMessage(), req);
		}
    	
    	return renderJsonpString("1", req);
        
    }
    
   
    @RequestMapping(value = "/deleteApp", produces = "text/plain; charset=utf-8", method = { RequestMethod.GET,
			RequestMethod.POST })
    @ResponseBody
    public String deleteApp(HttpServletRequest req) {
    	Dbservice dbm = new Dbservice(databaseService);
    	//Robj robj = new Robj();
    	String table = Dbservice.getTableName(req.getParameter("table"));
    	String object = req.getParameter("object");
    	
    	if(StringUtil.isNotNullOrEmpty(object)){
    		try {
    			Object paramsObj = Dbtablemapping.parseStringModel(object, table);
        		String sql = dbm.delete(table,paramsObj);
        		databaseService.executeAction(sql);
    		} catch (Exception e) {
    			return "操作失败"+e.getMessage();
    		}
    	}
    	return "success!";
    }
    
    @RequestMapping(value = "/deleteC", produces = "text/plain; charset=utf-8", method = { RequestMethod.GET,
			RequestMethod.POST })
    @ResponseBody
    public String deleteC(HttpServletRequest req) {
    	Dbservice dbm = new Dbservice(databaseService);
    	//Robj robj = new Robj();
    	String table = Dbservice.getTableName(req.getParameter("table"));
    	String object = req.getParameter("object");
    	
    	if(StringUtil.isNotNullOrEmpty(object)){
    		try {
    			Object paramsObj = Dbtablemapping.parseStringModel(object, table);
        		String sql = dbm.delete(table,paramsObj);
        		databaseService.executeAction(sql);
    		} catch (Exception e) {
    			return renderJsonpString("操作失败"+e.getMessage(), req);
    		}
    	}
    	return renderJsonpString("1", req);
    }
    
    @RequestMapping(value = "/delete", produces = "text/plain; charset=utf-8", method = { RequestMethod.GET,
			RequestMethod.POST })
    @ResponseBody
    public String delete(HttpServletRequest req) {
    	Dbservice dbm = new Dbservice(databaseService);
    	//Robj robj = new Robj();
    	String table = Dbservice.getTableName(req.getParameter("table"));
    	Object model = Common.getByRequest(Dbtablemapping.getModelByTable(table), req, false);
		try {
    		String sql = dbm.delete(table,model);
    		databaseService.executeAction(sql);
		} catch (Exception e) {
			return "操作失败"+e.getMessage();
		}
    	
    	return "success!";
    }

    @RequestMapping(value = "/deleteJ", produces = "text/plain; charset=utf-8", method = { RequestMethod.GET,
			RequestMethod.POST })
    @ResponseBody
    public String deleteJ(HttpServletRequest req) {
    	Dbservice dbm = new Dbservice(databaseService);
    	//Robj robj = new Robj();
    	String table = Dbservice.getTableName(req.getParameter("table"));
    	Object model = Common.getByRequest(Dbtablemapping.getModelByTable(table), req, false);
		try {
    		String sql = dbm.delete(table,model);
    		databaseService.executeAction(sql);
		} catch (Exception e) {
			return renderJsonpString("操作失败"+e.getMessage(), req);
		}
    	
    	return renderJsonpString("1", req);
    }

	@RequestMapping(value = "/getWxPhoneNumber", produces = "text/plain; charset=utf-8", method = { RequestMethod.GET,
			RequestMethod.POST })
	@ResponseBody
	public String getWxPhoneNumber(HttpServletRequest req) {
		// 1.请求微信接口服务，获取accessToken
		JSONObject accessTokenJson = WxLoginUtil.getAccessToken(Common.getProperty("app_id"), Common.getProperty("app_secret"));
		String accessToken = accessTokenJson.get("access_token",String.class);
		// 2.请求微信接口服务，获取用户手机号信息
		String code = req.getParameter("code");
		JSONObject phoneNumberJson = WxLoginUtil.getPhoneNumber(code, accessToken);
		String json = JSON.toJSONString(phoneNumberJson);
		return json;
	}

	@RequestMapping(value = "/wxlogin", produces = "text/plain; charset=utf-8", method = { RequestMethod.GET,
			RequestMethod.POST })
	@ResponseBody
	public String wxlogin(HttpServletRequest req) {
		String appid = Common.getProperty("app_id");
		String app_secret = Common.getProperty("app_secret");
		// 2.请求微信接口服务，获取用户手机号信息
		String code = req.getParameter("code");
		String baseUrl = "https://api.weixin.qq.com/sns/jscode2session?appid="+appid+"&secret="+app_secret+"&js_code="+code+"&grant_type=authorization_code";
		String body = HttpUtil.createGet(baseUrl).execute().body();
		return body;
	}


	@RequestMapping(value = "/getImgLocal", produces = "text/plain; charset=utf-8", method = { RequestMethod.GET,
			RequestMethod.POST })
	@ResponseBody
	public String saveWxUser(HttpServletRequest req) {
		String fileName = DownloadImg.downloadImg2path(req.getParameter("url"));
		return fileName;

	}
}