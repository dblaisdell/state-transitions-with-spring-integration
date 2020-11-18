package rnd.statemachine.order;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * 
 * @author nallas
 *
 */
@Service
public class OrderDbService {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public List<OrderData> getOrders() {
		return jdbcTemplate.query("select uuid,state from ORDER_STATE",(rs, rowNum) -> {
			OrderData d = new OrderData();
			d.setOrderId(UUID.fromString(rs.getString("uuid")));
			d.setMessage(rs.getString("state"));
			return d;
		});
	}

    public String getOrderState(UUID uuid) {
    	String sql = "select state from ORDER_STATE where uuid='"+uuid+"'";
    	String res;
    	try {
    	 res = jdbcTemplate.queryForObject(sql, String.class);
    	}catch(Exception e) {
    		res="";
    	}
    	return res;
    }

    @Transactional
    public void saveState(UUID uuid, String state) {
    	String sql;
    	if(!this.getOrderState(uuid).isEmpty()) {
    		sql = "update ORDER_STATE set state='"+state+"' where uuid='"+uuid+"'";
    	} else {
    		sql = "insert into ORDER_STATE(uuid,state) values('"+uuid+"','"+state+"')";
    	} 
    	jdbcTemplate.execute(sql);
    }
}
