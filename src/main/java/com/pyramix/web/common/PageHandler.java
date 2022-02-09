package com.pyramix.web.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.zkoss.util.media.AMedia;
import org.zkoss.zk.ui.WebApp;
import org.zkoss.zk.ui.WebApps;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

public class PageHandler implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4848436333196419561L;

	public static JasperPrint print;
	
	private static final Logger log = Logger.getLogger(PageHandler.class);
	
	@SuppressWarnings("rawtypes")
	public void generateReport(HashMap<String, Object> dataField,
			HashMap<String, Object> dataList, String fromPath) {
		
		String key = "";
		Collection<?> value = null;
		
		// String path = Sessions.getCurrent().getWebApp().getRealPath(fromPath);

		Collection<Map<String, ?>> datasource = new ArrayList<Map<String, ?>>();

		HashMap<String, Object> map = new HashMap<String, Object>();

		JRBeanCollectionDataSource list = null;

		if (dataList != null) {
			Set dataListSet = dataList.keySet();
			for (Iterator i = dataListSet.iterator(); i.hasNext();) {
				key = (String) i.next();
				value = (Collection<?>) dataList.get(key);
				list = new JRBeanCollectionDataSource(value);
				dataField.put(key, list);
			}
		}

		datasource.add(dataField);

		JRMapCollectionDataSource ds = new JRMapCollectionDataSource(datasource);

		try {
//			final Execution exec = Executions.getCurrent();
//			is = exec.getDesktop().getWebApp()
//					.getResourceAsStream(exec.toAbsoluteURI(path, false));

			InputStream is = null;
			
			final WebApp webapp = WebApps.getCurrent();
			
			is = webapp.getResourceAsStream(fromPath);
			
			if (is == null) {
				is = Thread.currentThread().getContextClassLoader()
						.getResourceAsStream(fromPath);
				if (is == null) {
					File fl = new File(fromPath);
					if (!fl.exists())
						throw new RuntimeException("resource for " + fromPath
								+ " not found.");

					is = new FileInputStream(fl);
				}
			}
			print = JasperFillManager.fillReport(is, map, ds);	

			//Show JasperViewer
			JasperViewer.viewReport(print, false);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("rawtypes")
	public AMedia generateReportAMedia(HashMap<String, Object> dataField,
			HashMap<String, Object> dataList, String fromPath, String fileName) throws IOException {
		
		log.info("PageHandler: "+fromPath);
		
		String key = "";
		Collection<?> value = null;
		
		// String path = Sessions.getCurrent().getWebApp().getRealPath(fromPath);
		
		Collection<Map<String, ?>> datasource = new ArrayList<Map<String, ?>>();

		HashMap<String, Object> map = new HashMap<String, Object>();

		JRBeanCollectionDataSource list = null;

		if (dataList != null) {
			Set dataListSet = dataList.keySet();
			for (Iterator i = dataListSet.iterator(); i.hasNext();) {
				key = (String) i.next();
				value = (Collection<?>) dataList.get(key);
				list = new JRBeanCollectionDataSource(value);
				dataField.put(key, list);
			}
		}

		datasource.add(dataField);

		JRMapCollectionDataSource ds = new JRMapCollectionDataSource(datasource);

		try {
			// final Execution exec = Executions.getCurrent();
			// is = exec.getDesktop().getWebApp()
			//		.getResourceAsStream(exec.toAbsoluteURI(path, false));

			InputStream is = null;
			
			final WebApp webapp = WebApps.getCurrent();
			
			is = webapp.getResourceAsStream(fromPath);
			
			if (is == null) {
				is = Thread.currentThread().getContextClassLoader()
						.getResourceAsStream(fromPath);
				if (is == null) {
					File fl = new File(fromPath);
					if (!fl.exists())
						throw new RuntimeException("resource for " + fromPath
								+ " not found.");

					is = new FileInputStream(fl);
				}
			}
			print = JasperFillManager.fillReport(is, map, ds);
			AMedia am = new AMedia(fileName+".pdf", null, null,
					JasperExportManager.exportReportToPdf(print));
			return am;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}		
}
