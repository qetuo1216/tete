package com.newlecture.webapp.controller.admin;




import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.newlecture.webapp.dao.MemberDao;
import com.newlecture.webapp.dao.NoticeDao;
import com.newlecture.webapp.dao.NoticeFileDao;
import com.newlecture.webapp.entity.Notice;
import com.newlecture.webapp.entity.NoticeFile;

@Controller
@RequestMapping("/admin/board/*")
public class BoardController {

	//��ü�� ioc�����̳ʿ� ����!
	@Autowired
	private NoticeDao noticeDao;
	
	@Autowired
	private NoticeFileDao noticeFileDao;
	
	@Autowired
	private MemberDao memberDao;
	
	@RequestMapping("notice")
	public String notice(
			@RequestParam(value="p", defaultValue="1") Integer page,
			@RequestParam(value="f", defaultValue="title") String field, //title�� �⺻������ �˻��ϰڴ�
			@RequestParam(value="q", defaultValue="") String query,
			Model model) {
		
		model.addAttribute("list", noticeDao.getList(page, field, query));
		
		return "admin.board.notice.list";
	}
	
	@RequestMapping("notice/{id}")
	public String noticeDetail(@PathVariable("id") String id, Model model) {
		
		model.addAttribute("n", noticeDao.get(id));
		model.addAttribute("prev", noticeDao.getPrev(id));
		model.addAttribute("next", noticeDao.getNext(id));
		
		//return "customer/notice-detail";
		return "admin.board.notice.detail";
	}
	
	@RequestMapping(value="notice/reg", method=RequestMethod.GET)
	public String noticeReg() {
		
		return "admin.board.notice.reg";
	}
	
	@RequestMapping(value="notice/reg", method=RequestMethod.POST)
	/*public String noticeReg(
			String title,
			String content) throws UnsupportedEncodingException {*/
	public String noticeReg(
			Notice notice,
			MultipartFile file, //name="file"�� �༮�� 2�� �̻��� �� ���... []�� �̿��Ѵ�.
			HttpServletRequest request,
			Principal principal) throws IOException { //principal : ���� ������� ������ �������ش�.
		
		//file��ü�� ������ �Ǿ����� ����÷�θ� ���ؼ� ���� null�ϰ���� ó��(����ó��������)
		//file.isEmpty();
		
		
		//���糯¥��� - ���丮������ �⵵���� �ϱ� ����
		// ��¥��¹��1
		//Date curDate = new Date();
		//curDate.getYear();
		
		// ��¥��¹��2
		Calendar cal = Calendar.getInstance();
		//Date curDate2 = cal.getTime();
		int year = cal.get(Calendar.YEAR);
		
		// ��¥��¹��3
		//SimpleDateFormat fmt = new SimpleDateFormat("yyyy");
		//String year = fmt.format(curDate)

		
		//Ʈ��������� ��������Ѵ�. ���� ���� �ø��� �ִµ� �ٸ������ �÷������� ���� �޾ƿ� Ű���� �޶����� �ϱ⶧����
		//���ع����� �ȵǹǷ� �ϳ��� ������ ��������Ѵ�.
		//���� �Խù��� �۹�ȣ�� �˾Ƴ´� ���������� �Լ��� ���� �̿��ؾ��Ѵ�. 
		String nextId = noticeDao.getNextId();
				
		ServletContext ctx = request.getServletContext();
		/*String path = ctx.getRealPath("/resource/customer/notice/" + year + "/" + nextId); //year�� �������� ����ȯ ( ������ -> ������ )*/
		String path = ctx.getRealPath(String.format("/resource/customer/notice/%s/%s", year, nextId));		
		System.out.println(path);
		
		File f = new File(path); //������ ���������� ���� ���ϰ�ü ����
		
		if(!f.exists()) {
			if(!f.mkdirs()) //mkdirs() -> ������ ������
				System.out.println("���丮�� ������ ���� �����ϴ�.");
		}

		// ��θ� ���ϸ� �߰����ֱ�
		// ��θ� Ȯ���غ��� ����� ������� �����ڰ� \���� �Ʒ��� /�� �־��־���...
		// ������ �����찡 �ƴ϶� ����������� �� �ٸ��� ������ �����ڸ� �������� �ν��Ҽ� �ִ� ������ ������Ѵ�.. �����ڸ� �������ش�. => File.separator
		path += File.separator + file.getOriginalFilename();
		File f2 = new File(path); //���Ⱑ ����Ǹ� ������ 0Ű�ι���Ʈ¥���� ���������
		
		
		InputStream fis = file.getInputStream();
		OutputStream fos = new FileOutputStream(f2);
		
		byte[] buf = new byte[1024];
		
		int size = 0;
		while((size = fis.read(buf)) > 0)
			fos.write(buf, 0, size);
		
		
		fis.close();
		fos.close();
		
		
		
		
		//file.getInputStream();
		String fileName = file.getOriginalFilename(); //db�����ϱ����� ������ �Ѿ������ Ȯ���ؾ��Ѵ�.
		System.out.println(fileName);
		
		//�ѱ� ������
		//title = new String(title.getBytes("ISO-8859-1"),"UTF-8");
		
		System.out.println(notice.getTitle());
		
		String writerId = "newlec";
		notice.setWriterId("newlec");
		//int row = noticeDao.insert(new Notice(title, content, writerId));

		//������ �ʿ�
		int row = noticeDao.insert(notice); //�Խñ� ����ϰ�,,, ������� �̸��� �������°��� �ϳ��� Ʈ��������� ����� �Ѵ�!!
		//memberDao.pointUp(principal.getName()); //������� �̸��� �����Դ�! => Ʈ����� ó�� ����
		
		noticeFileDao.insert(new NoticeFile(null, fileName, nextId)); //id, src, noticeId
		
		return "redirect:../notice";
	}
	
	/*@RequestMapping(value="notice/edit", method=RequestMethod.GET)
	public String noticeEdit(
			String id,
			String title,
			String content,
			MultipartFile file, //name="file"�� �༮�� 2�� �̻��� �� ���... []�� �̿��Ѵ�.
			HttpServletRequest request,
			Principal principal) {
		
		System.out.println(id);
		System.out.println(title);
		System.out.println(content);
		
		int result = noticeDao.update(notice);
		
		return "admin.board.notice.edit";
	}*/
	
	/*@RequestMapping(value="notice/edit", method=RequestMethod.GET)
	public String noticeEdit() {
		
		return "admin.board.notice.edit";
	}
	
	@RequestMapping(value="notice-edit", method=RequestMethod.POST)
	public String noticeEdit2(String id ){
		
		
		
		return "admin.board.notice.detail";
	}*/
	
	
}





















