package com.newlecture.webapp.controller;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.newlecture.webapp.dao.NoticeDao;
import com.newlecture.webapp.entity.NoticeView;

@Controller
@RequestMapping("/customer/*")
public class CustomerController {
	
	@Autowired
	private NoticeDao noticeDao; // �ٿ� �̿��ϰڴ�!
	
	@RequestMapping("notice")
	public String notice(
		@RequestParam(value="p", defaultValue="1") Integer page,
		@RequestParam(value="f", defaultValue="title") String field, //title�� �⺻������ �˻��ϰڴ�
		@RequestParam(value="q", defaultValue="") String query,
		Model model) {

		model.addAttribute("list", noticeDao.getList(page, field, query));
		model.addAttribute("count", noticeDao.getCount());
		
		/*
		String output = String.format("p:%s, q:%s\n", page, query);
		output += String.format("title : %s\n", list.get(0).getTitle());*/
		
		//return "customer/notice"; //servlet-context.xml���� ������ resolve ���̺귯���� �̿��ؼ� �����Ͽ��⶧���� �ٿ������ִ�!
		return "customer.notice.list"; //tiles�� �������ؼ� tiles �������Ͽ� ������ ��� �ٲ��ش�. ��� tiles resolver�� ��ġ����� �Ѵ�.
	}

	@RequestMapping("notice-ajax")
	@ResponseBody
	public String noticeA(
		@RequestParam(value="p", defaultValue="1") Integer page,
		@RequestParam(value="f", defaultValue="title") String field, //title�� �⺻������ �˻��ϰڴ�
		@RequestParam(value="q", defaultValue="") String query,
		Model model) {

		List<NoticeView> list = noticeDao.getList(page, field, query);
		
		String json="";
		
		Gson gson = new Gson();
		json = gson.toJson(list);
		
		/*StringBuilder builder = new StringBuilder();
		
		builder.append("[");
		builder.append("{id:\"1\", title:\"hihihi~\", writerId:\"newlec\"},");
		builder.append("{id:\"2\", title:\"hohoho~~\", writerId:\"lecnew\"}");
		builder.append("]");

		json = builder.toString();*/
		
		/*try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		return json;
	}


	@RequestMapping("notice/{id}")
	public String noticeDetail(@PathVariable("id") String id, Model model) {
		
		model.addAttribute("n", noticeDao.get(id));
		//model.addAttribute("prev", noticeDao.getPrev(id));
		//model.addAttribute("next", noticeDao.getNext(id));
		
		//return "customer/notice-detail";
		return "customer.notice.detail";
	}
}
