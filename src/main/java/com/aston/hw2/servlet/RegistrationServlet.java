package com.aston.hw2.servlet;

import com.aston.hw2.model.dto.UserRegistrationDto;
import com.aston.hw2.service.UserService;
import com.aston.hw2.util.JsonParserUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.EmailValidator;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.aston.hw2.util.ResponseAnswers.EMAIL_USED;
import static com.aston.hw2.util.ResponseAnswers.INCORRECT_EMAIL;
import static com.aston.hw2.util.ResponseAnswers.REG_SUCCESS;

@Slf4j
@WebServlet("/registration")
public class RegistrationServlet extends HttpServlet {
    private UserService userService;
    private EmailValidator emailValidator;

    @Override
    public void init() {
        this.userService = (UserService) getServletContext().getAttribute("userService");
        this.emailValidator = (EmailValidator) getServletContext().getAttribute("emailValidator");
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/HTML");
        String respMessage;

        UserRegistrationDto user = JsonParserUtil.toUserRegistrationDtoFromJson(req);
        log.debug("registration user with data: {}", user);

        if (!emailValidator.isValid(user.getEmail())) {
            respMessage = INCORRECT_EMAIL;
            resp.setStatus(400);
            resp.getWriter().write(respMessage);
            return;
        }

        boolean isRegistered = userService.addUser(user);

        if (isRegistered) {
            respMessage = REG_SUCCESS;
            resp.setStatus(201);
        } else {
            respMessage = EMAIL_USED;
            resp.setStatus(400);
        }
        resp.getWriter().write(respMessage);
    }
}
