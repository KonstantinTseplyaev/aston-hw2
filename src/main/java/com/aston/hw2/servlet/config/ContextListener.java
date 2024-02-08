package com.aston.hw2.servlet.config;

import com.aston.hw2.dao.EventDao;
import com.aston.hw2.dao.LocationDao;
import com.aston.hw2.dao.UserDao;
import com.aston.hw2.service.AuthService;
import com.aston.hw2.service.EventService;
import com.aston.hw2.service.UserService;
import com.aston.hw2.service.impl.AuthServiceImpl;
import com.aston.hw2.service.impl.EventServiceImpl;
import com.aston.hw2.service.impl.UserServiceImpl;
import org.apache.commons.validator.routines.EmailValidator;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ServletContext context = servletContextEvent.getServletContext();
        UserDao userDao = new UserDao();
        EventDao eventDao = new EventDao();
        LocationDao locationDao = new LocationDao();
        EmailValidator emailValidator = EmailValidator.getInstance();
        EventService eventService = new EventServiceImpl(eventDao, locationDao, userDao);
        AuthService authService = new AuthServiceImpl(userDao);
        UserService userService = new UserServiceImpl(userDao);

        context.setAttribute("userService", userService);
        context.setAttribute("eventService", eventService);
        context.setAttribute("authService", authService);
        context.setAttribute("emailValidator", emailValidator);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
