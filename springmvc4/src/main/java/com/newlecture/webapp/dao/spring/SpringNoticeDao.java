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
	
	
	//Transaction처리방법1
	//TransactionManager를 직접 사용하는 방법
	/*@Autowired
	private PlatformTransactionManager transactionManager;*/
	
	//Transaction처리방법2
	@Autowired
	private TransactionTemplate transactionTemplate;
	
	
	@Override
	public List<NoticeView> getList(int page, String field, String query) {

		String sql = "select * from NoticeView where " + field + " like ? order by regDate desc limit ?,10";
		
		List<NoticeView> list = template.query(
				sql,
				new Object[] {"%"+query+"%" , (page-1)*10},  //첫번째 물음표, 두번째 물음표
				BeanPropertyRowMapper.newInstance(NoticeView.class));
		
		return list;
	}
	
	//Transaction처리방법3과  4번(@transactional)
	//AOP를 사용하는 방법
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public int insert(Notice notice) {

		String sql = "insert into Notice(id, title, content, writerId) values(?, ?, ?, ?)";
		
		//트랜잭션을 구현하기 위해 service계층으로 나누어 내는 작업
		String sql1 = "update Member set point=point+1 where id=?";
		
		int result = 0;
	
		result =template.update(sql
					, getNextId()		//서브쿼리를 이용하기 위한 메서드
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
		String sql = "select * from Notice where id=?"; //sql문에 정해지지않은 부분은... object배열의 인자를 넣어줌으로써 해결한다!
		
		/*spring의 di기능을 이용하여 코드량을 줄일수있다.*/
		/*DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		dataSource.setUrl("jdbc:mysql://211.238.142.247/newlecture?autoReconnect=true&useSSL=false&useUnicode=true&characterEncoding=utf8");
		dataSource.setUsername("sist");
		dataSource.setPassword("cclass");*/
		
		/*JdbcTemplate template = new JdbcTemplate();
		template.setDataSource(dataSource);*/ //database에 연결하기 위해 알려줘야할것!
		
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
		
		//직접 하려면? 거의 쓸일은 없다. 혹시나 위의 방식으로 못쓰는경우에..
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
	//Transaction처리방법3
	//AOP를 사용하는 방법
	@Override
	public int insert(Notice notice) {

		String sql = "insert into Notice(id, title, content, writerId) values(?, ?, ?, ?)";
		
		//트랜잭션을 구현하기 위해 service계층으로 나누어 내는 작업
		String sql1 = "update Member set point=point+1 where id=?";
		
		int result = 0;
	
		result =template.update(sql
					, getNextId()		//서브쿼리를 이용하기 위한 메서드
					, notice.getTitle()
					, notice.getContent()
					, notice.getWriterId());
			
			
		result += template.update(sql1
					, notice.getWriterId());
			

		return result;
	
	}
	*/

	//Transaction처리방법2
	//TransactionTemplate 사용하는 방법
	/*@Override
	public int insert(Notice notice) {

		String sql = "insert into Notice(id, title, content, writerId) values(?, ?, ?, ?)";
		
		//트랜잭션을 구현하기 위해 service계층으로 나누어 내는 작업
		String sql1 = "update Member set point=point+1 where id=?";
		
		int result = 0;
		
		result = (int) transactionTemplate.execute(new TransactionCallbackWithoutResult() {
			
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus arg0) {

				template.update(sql
						, getNextId()		//서브쿼리를 이용하기 위한 메서드
						, notice.getTitle()
						, notice.getContent()
						, notice.getWriterId());
				
				
				template.update(sql1
						, notice.getWriterId());
				
			}
		});

			return result;
	
	}*/
	
	//Transaction처리방법1
	//TransactionManager를 직접 사용하는 방법
	/*@Override
	public int insert(Notice notice) {

		String sql = "insert into Notice(id, title, content, writerId) values(?, ?, ?, ?)";
		
		//트랜잭션을 구현하기 위해 service계층으로 나누어 내는 작업
		String sql1 = "update Member set point=point+1 where id=?";
		
		
		//수작업으로 해준 트랜잭션 처리(spring의 기능 사용 안한상태)
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus state = transactionManager.getTransaction(def);
		
		try {
			int result = template.update(sql
					, getNextId()		//서브쿼리를 이용하기 위한 메서드
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
