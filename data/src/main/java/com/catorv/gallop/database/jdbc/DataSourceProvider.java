package com.catorv.gallop.database.jdbc;

import com.catorv.gallop.lifecycle.Destroy;
import com.google.inject.ImplementedBy;
import com.google.inject.Provider;

import javax.sql.DataSource;
import java.io.Closeable;
import java.io.IOException;

/**
 * Created by cator on 25/10/2016.
 */
@ImplementedBy(BasicDataSourceProvider.class)
public interface DataSourceProvider extends Closeable, Provider<DataSource> {
	DataSource getDataSource(String jndi);

	@Override
	@Destroy
	void close() throws IOException;

	@Override
	DataSource get();
}
