package com.redhat.techbase.util;

import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

public class PropertyFileReader extends Properties {

    private static final long serialVersionUID = -7182008117522643499L;
	private static PropertyFileReader propertyFileReader = null;
    private static Logger logger = Logger.getLogger(PropertyFileReader.class);

    public static String propertyFile = "config.properties";
    
    public static final String PROCESS_SHEET_NUM = "process.sheet.num";
	public static final String TOTAL_COLS = "total.cols";
	public static final String TOTAL_ROWS = "total.rows";
	public static final String PROCESS_FROMROW_NUM = "process.fromrow.num";

    public static final String CONTENT_PATH="contents/";
    public static final String PLACES_PATH="/places";
    
    public static final String ATTACH_PATH="attachments/";
    
    public static final String SPACE_TYPE="space";
    public static final String PROJECT_TYPE="project";
    public static final String GROUP_TYPE="group";
   
    public static final String PLACE="place.name";
    
    public static final String OUTPUT="file.name";
    public static final String MOJODOCTYPE="doctype";
    public static final String DIRECTORY="file.directory";
    
    //constants
    public static final String ROOT_URL="root.url";
    public static final String JOB_RUN_DATE="last.job.date";
    public static final String BASE_URL="rest.context";
    
    public static final String SPACE_NAME="space.name";
    public static final String SUB_SPACE_NAME="subSpace.name";
    public static final String ARCHIVE_SUB_SPACE_NAME="archived.space";
    
    public static final String GROUP_NAME="group.name";
    
    public static final String CONTENT_ID="content.id";
    
    public static final String TECHNOLOGY_CLASSIFICATION="technology";
    public static final String PRODUCT_CLASSIFICATION="product";
    public static final String CONSULTANT="consultant";
    public static final String ENABLEMENT="enablement";
    public static final String PRESALES="pre-sales";
    public static final String MANAGEMENT="management";
    public static final String BA_CLASSIFICATION="business.activity";
    
    
    public static final String ALFRESCO_SERVER_URL ="alfresco.url";
    public static final String ALFRESCO_USERNAME="alfresco.username";
    public static final String ALFRESCO_PASSWORD="alfresco.password";
    public static final String CONTENT_TYPE="contenttype";
    public static final String ACCOUNT_CONTENT_TYPE="contenttype2";
    public static final String DOWNLOAD_PATH="downloadpath";
    public static final String TIME_OUT="timeout";
    public static final String PATH="storepath";

  public PropertyFileReader() {
        super();
        try {
            loadProperties(Thread.currentThread().getContextClassLoader());
        } catch (Exception ex) { // unable to load the properties file for some reason
        	System.out.printf("BatchUploadProperty() -  Unable to load properties file... - ", ex);
        }
    }

    private void loadProperties(ClassLoader contextClassLoader) {
    	System.out.printf("loadProperties() -  Beginning to load Properties");
        InputStream in = null;
        try {
            in = contextClassLoader.getResourceAsStream(propertyFile);
            if (in != null)
                super.load(in); // can throw IOException
            else
            	System.out.printf("loadProperties() -  InputStream was null. Probably file does not exist. " + propertyFile);
        } catch (Exception e) {
        	System.out.printf("loadProperties() -  Some error loading property file - ", e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                	System.out.printf("loadProperties() -  Could not close input stream - ", e);
                }
            }
        }
        System.out.printf("loadProperties() -  Finished loading Properties");
    }

    public static PropertyFileReader getInstance() {
        if (propertyFileReader == null) {
            synchronized (PropertyFileReader.class) {
                if (propertyFileReader == null)
                    propertyFileReader = new PropertyFileReader();
            }
        }
        return propertyFileReader;
    }

    public static void reload() {
        logger.info("reload() - Reloading the properties file");
        propertyFileReader = null;
        propertyFileReader = new PropertyFileReader();
    }

}
