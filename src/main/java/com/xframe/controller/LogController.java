package com.xframe.controller;

import java.text.ParseException;

import com.jfinal.core.ActionKey;
import com.jfinal.log.Log;
import com.xframe.common.model.News;
import com.xframe.utils.DateHelper;

public class LogController extends BaseController<News>{
	
    private final static Log LOG = Log.getLog(LogController.class);
 
    @ActionKey("/api/log")
    public void index() throws ParseException {
        LOG.info(DateHelper.Now());
        renderText(DateHelper.Now());
    }
}
