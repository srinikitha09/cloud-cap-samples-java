package my.bookshop.handlers;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.sap.cds.services.EventContext;
import com.sap.cds.services.changeset.ChangeSetListener;
import com.sap.cds.services.handler.EventHandler;
import com.sap.cds.services.handler.annotations.Before;
import com.sap.cds.services.handler.annotations.ServiceName;

import cds.gen.extendedservice.ExtendedService_;
import cds.gen.extendedservice.SessionContext_;

@Component
@ServiceName(ExtendedService_.CDS_NAME)
public class ExtendedService implements EventHandler {

	final static Logger LOG = LoggerFactory.getLogger(ExtendedService.class);

	final static String MY_VARIABLE = "myVariable";

	@Autowired
	DataSource datasource;

	@SuppressWarnings("resource")
	@Before(event = "*", entity = SessionContext_.CDS_NAME)
	@Transactional
	public void setSessionContextVariables(EventContext ctx) {
		ctx.getChangeSetContext().register(new ChangeSetListener() {

			@Override
			public void beforeClose() {
				try {
					Connection connection = new TransactionAwareDataSourceProxy(ExtendedService.this.datasource)
							.getConnection();
					connection.setClientInfo(MY_VARIABLE, null);
				} catch (SQLException e) {
					LOG.error(e.getMessage(), e);
				}
			}
		});

		try {
			Connection connection = new TransactionAwareDataSourceProxy(ExtendedService.this.datasource)
					.getConnection();
			connection.setClientInfo(MY_VARIABLE, "myValue");
		} catch (SQLException e) {
			LOG.error(e.getMessage(), e);
		}
	}
}
