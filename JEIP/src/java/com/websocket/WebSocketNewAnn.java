/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.websocket;

import com.google.gson.Gson;
import com.model.userheader.UserHeader;
import java.io.IOException;
import java.util.HashMap;
import java.util.*;
import java.util.Map.Entry;
import javax.servlet.http.HttpSession;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 最新公告推播 WebSocket for tomcat server
 *
 * @author Vance
 */
@ServerEndpoint(value = "/NewAnn/echo", configurator = WebSocketNewAnnConfig.class)
public class WebSocketNewAnn {

    /**
     * 最新公告推播的log紀錄
     */
    protected final Log logger = LogFactory.getLog(this.getClass());

    //所有登入者的Session
    private static final Map<Integer, Session> userSessions = Collections.synchronizedMap(new HashMap<Integer, Session>());

    private HttpSession httpSession;

    /**
     * WebSocket開始執行時
     * 
     * @param userSession
     * @param config
     * @throws IOException
     * @throws Exception
     */
    @OnOpen
    public void onOpen(Session userSession, EndpointConfig config) throws IOException, Exception {
        this.httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        UserHeader loginInfo = (UserHeader) httpSession.getAttribute("LoginInfo");
        int userID = loginInfo.getUserID();

        //放入每位登入者的httpSession
        userSessions.put(userID, userSession);
    }

    /**
     * WebSocket發送訊息至client端
     * 
     * @param session
     * @param message
     */
    @OnMessage
    public void onMessage(Session session, String message) {
        //待完成...
        //預計功能：user未點開來看訊息時，則持續發送訊息給user
    }

    /**
     * WebSocket發送錯誤時
     * 
     * @param session
     * @param t
     */
    @OnError
    public void onError(Session session, Throwable t) {
    }

    /**
     * WebSocket關閉
     * 
     * @param session
     * @throws IOException
     */
    @OnClose
    public void OnClose(Session session) throws IOException {
        session.close();
    }

    /**
     * 發送給所有使用者最新公告的void()
     * 
     * @param sendMsg
     */
    public void sendNewAnnMsgToAll(String sendMsg) {
        int userID = -1;
        try {
            for (Entry<Integer, Session> entry : userSessions.entrySet()) {
                if (entry.getValue().isOpen()) {
                    userID = Integer.parseInt(entry.getKey().toString());
                    entry.getValue().getBasicRemote().sendText(sendMsg);
                }
            }
        } catch (IOException e) {
            logger.error("發送給UserID:" + userID + "之WebSocket訊息發生錯誤:" + e.getLocalizedMessage());
        }
    }
}
