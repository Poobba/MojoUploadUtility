package com.redhat.techbase.service;


import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;


import com.redhat.techbase.dto.Contentdto;

public interface MojoService {

	String getResponse(String restContext, String url);
	
	String getAlfrescoTicket();

	String getResponse(String atturl);

	HashMap<String, String> checkParent(String asString, String type);

}
