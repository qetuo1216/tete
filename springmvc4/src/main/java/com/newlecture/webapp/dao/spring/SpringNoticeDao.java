package com.newlecture.webapp.dao.spring;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DaoSupport;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import com.newlecture.webapp.dao.NoticeDao;
import com.newlecture.webapp.entity.Notice;
import com.newlecture.webapp.entity.NoticeView;

public class SpringNoticeDao implements NoticeDao {
	
	@Autowired
	private JdbcTemplate template;
	
	/*@Autowired
	public void setTemplate(JdbcTemplate template) {
		this.template = template;
	}*/
	
	
	//Transactionó�����1
	//TransactionManager�� ���� ����ϴ� ���
	/*@Autowired
	private PlatformTransactionManager transactionManager;*/
	
	//Transactionó�����2
	@Autowired
	private TransactionTemplate transactionTemplate;
	
	
	@Override
	public List<NoticeView> getList(int page, String field, String query) {

		String sql = "select * from NoticeView where " + field + " like ? order by regDate desc limit ?,10";
		
		List<NoticeView> list = template.query(
				sql,
				new Object[] {"%"+query+"%" , (page-1)*10},  //ù��° ����ǥ, �ι�° ����ǥ
				BeanPropertyRowMapper.newInstance(NoticeView.class));
		
		return list;
	}
	
	//Transactionó�����3��  4��(@transactional)
	//AOP�� ����ϴ� ���
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public int insert(Notice notice) {

		String sql = "insert into Notice(id, title, content, writerId) values(?, ?, ?, ?)";
		
		//Ʈ������� �����ϱ� ���� service�������� ������ ���� �۾�
		String sql1 = "update Member set point=point+1 where id=?";
		
		int result = 0;
	
		result =template.update(sql
					, getNextId()		//���������� �̿��ϱ� ���� �޼���
					, notice.getTitle()
					, notice.getContent()
					, notice.getWriterId());
			
			
		result += template.update(sql1
					, notice.getWriterId());
			

		return result;
	
	}
	

	@Override
	public int getCount() {

		String sql = "select count(id) `count` from Notice";
		
		int count = template.queryForObject(
				sql,
				Integer.class);
		
		return count;
	}

	@Override
	public NoticeView get(String id) {
		String sql = "select * from Notice where id=?"; //sql���� ������������ �κ���... object�迭�� ���ڸ� �־������ν� �ذ��Ѵ�!
		
		/*spring�� di����� �̿��Ͽ� �ڵ差�� ���ϼ��ִ�.*/
		/*DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		dataSource.setUrl("jdbc:mysql://211.238.142.247/newlecture?autoReconnect=true&useSSL=false&useUnicode=true&characterEncoding=utf8");
		dataSource.setUsername("sist");
		dataSource.setPassword("cclass");*/
		
		/*JdbcTemplate template = new JdbcTemplate();
		template.setDataSource(dataSource);*/ //database�� �����ϱ� ���� �˷�����Ұ�!
		
		NoticeView notice = template.queryForObject(
				sql, 
				new Object[] {id},
				BeanPropertyRowMapper.newInstance(NoticeView.class));
		
		/*NoticeView notice = template.queryForObject(
				sql, 
				new Object[] {id},
				new RowMapper<NoticeView>() {

					@Override
					public NoticeView mapRow(ResultSet rs, int rowNum) throws SQLException {
							
						NoticeView notice = new NoticeView();
						notice.setId(rs.getString("id"));
						notice.setTitle(rs.getString("title"));
						notice.setWriterId(rs.getString("writerId"));
						notice.setContent(rs.getString("content"));
						notice.setHit(0);
						
						return notice;
					}
					
				});*/
		
		return notice;
	}

	@Override
	public int update(String id, String title, String content) {
	
		String sql = "update Notice set title=?, content=? where id=?";
		
		int result = template.update(sql
				, title
				, content
				, id);
		
		//���� �Ϸ���? ���� ������ ����. Ȥ�ó� ���� ������� �����°�쿡..
		/*int result = template.update(sql, new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement st) throws SQLException {
				st.setString(1, title);
				st.setString(2, content);
				st.setString(3, id);
				
			}
			
		});*/
				
		return result;
	}

	@Override
	public NoticeView getPrev(String id) {

		String sql = "select * from NoticeView where id < CAST(? AS UNSIGNED) order by regDate desc limit 1";
		
		NoticeView notice = template.queryForObject(
				sql, 
				new Object[] {id},
				BeanPropertyRowMapper.newInstance(NoticeView.class));
		
		return notice;

	}

	@Override
	public NoticeView getNext(String id) {

		String sql = "select * from NoticeView where id > CAST(? AS UNSIGNED) order by regDate asc limit 1";
		
		NoticeView notice = template.queryForObject(
				sql, 
				new Object[] {id},
				BeanPropertyRowMapper.newInstance(NoticeView.class));
		
		return notice;

	}

	@Override
	public int insert(String title, String content, String writerId) {
		
		return insert(new Notice(title, content, writerId));

	}
/*
	//Transactionó�����3
	//AOP�� ����ϴ� ���
	@Override
	public int insert(Notice notice) {

		String sql = "insert into Notice(id, title, content, writerId) values(?, ?, ?, ?)";
		
		//Ʈ������� �����ϱ� ���� service�������� ������ ���� �۾�
		String sql1 = "update Member set point=point+1 where id=?";
		
		int result = 0;
	
		result =template.update(sql
					, getNextId()		//���������� �̿��ϱ� ���� �޼���
					, notice.getTitle()
					, notice.getContent()
					, notice.getWriterId());
			
			
		result += template.update(sql1
					, notice.getWriterId());
			

		return result;
	
	}
	*/

	//Transactionó�����2
	//TransactionTemplate ����ϴ� ���
	/*@Override
	public int insert(Notice notice) {

		String sql = "insert into Notice(id, title, content, writerId) values(?, ?, ?, ?)";
		
		//Ʈ������� �����ϱ� ���� service�������� ������ ���� �۾�
		String sql1 = "update Member set point=point+1 where id=?";
		
		int result = 0;
		
		result = (int) transactionTemplate.execute(new TransactionCallbackWithoutResult() {
			
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus arg0) {

				template.update(sql
						, getNextId()		//���������� �̿��ϱ� ���� �޼���
						, notice.getTitle()
						, notice.getContent()
						, notice.getWriterId());
				
				
				template.update(sql1
						, notice.getWriterId());
				
			}
		});

			return result;
	
	}*/
	
	//Transactionó�����1
	//TransactionManager�� ���� ����ϴ� ���
	/*@Override
	public int insert(Notice notice) {

		String sql = "insert into Notice(id, title, content, writerId) values(?, ?, ?, ?)";
		
		//Ʈ������� �����ϱ� ���� service�������� ������ ���� �۾�
		String sql1 = "update Member set point=point+1 where id=?";
		
		
		//���۾����� ���� Ʈ����� ó��(spring�� ��� ��� ���ѻ���)
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus state = transactionManager.getTransaction(def);
		
		try {
			int result = template.update(sql
					, getNextId()		//���������� �̿��ϱ� ���� �޼���
					, notice.getTitle()
					, notice.getContent()
					, notice.getWriterId());
			
			
			result += template.update(sql1
					, notice.getWriterId());
			
			transactionManager.commit(state);
			
			return result;
		}
		catch (Exception e) {
			transactionManager.rollback(state);
			
			throw e;
		}
		
	}*/

	@Override
	public String getNextId() {

		String sql = "select ifnull(max(cast(id as unsigned)),0) + 1 from Notice";
		
		String result = template.queryForObject(
				sql,
				String.class);
		
		return result;
	}

}
