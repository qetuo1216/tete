package com.newlecture.webapp.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.newlecture.webapp.entity.Notice;
import com.newlecture.webapp.entity.NoticeView;

public interface NoticeDao {
	List<NoticeView> getList(@Param("page") int page, String field, String query); //mybatis에 종속시켜도 될때는 인터페이스를 바꿔도 된다.
	int getCount();
	
	NoticeView get(String id); //mybatis에 종속시켜도 될때는 인터페이스를 바꿔도 된다... 하지만 인터페이스는 다른데서도 사용가능하도록 안건드리는게,...
	
	int update(String id, String title, String content);
	
	NoticeView getPrev(String id);
	NoticeView getNext(String id);
	
	int insert(String title, String content, String writerId);
	int insert(Notice notice);
	String getNextId();

}