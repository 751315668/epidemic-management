package com.epidemicmanagement.service;

import com.epidemicmanagement.entity.Message;
import com.epidemicmanagement.entity.News;
import com.github.pagehelper.PageInfo;

/**
 * @author XUE
 */
public interface MessageService {

    PageInfo<Message> selectAllMsg(Integer pageNum, Integer pageSize);

    boolean insertMsg(Message message);

//    boolean replyMsg(Message message);

}
