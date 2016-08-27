package com.catorv.test.gallop.database;

import com.catorv.gallop.cfg.ConfigurationModule;
import com.catorv.gallop.database.DatabaseModule;
import com.catorv.gallop.lifecycle.LifecycleModule;
import com.catorv.gallop.test.junit.GuiceModule;
import com.catorv.gallop.test.junit.GuiceTestRunner;
import com.google.inject.Inject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * DataSource Test
 * Created by cator on 8/11/16.
 */
@RunWith(GuiceTestRunner.class)
@GuiceModule({
		ConfigurationModule.class,
		LifecycleModule.class,
		DatabaseModule.class
})
public class DataSourceTest {


	@Inject
	private DataSource ds;

	@Test
	public void testUtf8mb4() throws SQLException {
		String s = new String(new byte[]{(byte) 0xf0, (byte) 0x9f, (byte) 0x92,
				(byte) 0x83});

		Connection connection = ds.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			pstmt = connection.prepareStatement("drop table if exists t_utf8mb4");
			pstmt.execute();
			pstmt.close();

			pstmt = connection.prepareStatement("create table t_utf8mb4 (s varchar(20))");
			pstmt.execute();
			pstmt.close();

			pstmt = connection.prepareStatement("insert into t_utf8mb4 (s) values (?)");
			pstmt.setString(1, s);
			pstmt.execute();
			pstmt.close();

			pstmt = connection.prepareStatement("select s from t_utf8mb4");
			rs = pstmt.executeQuery();
			if (rs.next()) {
				Assert.assertEquals(s, rs.getString(1));
			}
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
			if (connection != null) {
				connection.close();
			}
		}
	}

}
