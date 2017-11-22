package com.newlecture.webapp.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.newlecture.webapp.entity.Notice;
import com.newlecture.webapp.entity.NoticeView;

public interface NoticeDao {
	List<NoticeView> getList(@Param("page") int page, String field, String query); //mybatis�� ���ӽ��ѵ� �ɶ��� �������̽��� �ٲ㵵 �ȴ�.
	int getCount();
	
	NoticeView get(String id); //mybatis�� ���ӽ��ѵ� �ɶ��� �������̽��� �ٲ㵵 �ȴ�... ������ �������̽��� �ٸ������� ��밡���ϵ��� �Ȱǵ帮�°�,...
	
	int update(String id, String title, String content);
	
	NoticeView getPrev(String id);
	NoticeView getNext(String id);
	
	int insert(String title, String content, String writerId);
	int insert(Notice notice);
	String getNextId();

}