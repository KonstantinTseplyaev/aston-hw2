package com.aston.hw2.servlet;

import com.aston.hw2.model.User;
import com.aston.hw2.model.dto.UserLoginDto;
import com.aston.hw2.service.AuthService;
import com.aston.hw2.util.JsonParserUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.EmailValidator;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static com.aston.hw2.util.ResponseAnswers.INCORRECT_EMAIL;
import static com.aston.hw2.util.ResponseAnswers.LOG_ERROR;
import static com.aston.hw2.util.ResponseAnswers.LOG_SUCCESS;

@Slf4j
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private AuthService authService;
    private EmailValidator emailValidator;

    @Override
    public void init() {
        this.authService = (AuthService) getServletContext().getAttribute("authService");
        this.emailValidator = (EmailValidator) getServletContext().getAttribute("emailValidator");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/HTML");

        UserLoginDto userLoginDto = JsonParserUtil.toUserLoginDtoFromJson(req);

        if (!emailValidator.isValid(userLoginDto.getEmail())) {
            resp.setStatus(400);
            resp.getWriter().write(INCORRECT_EMAIL);
            return;
        }

        User authUser = authService.getAuthenticatedUser(userLoginDto);

        if (authUser != null) {
            HttpSession userSession = req.getSession();
            userSession.setAttribute("userId", authUser.getId());
            resp.getWriter().write(LOG_SUCCESS);
        } else {
            resp.setStatus(400);
            resp.getWriter().write(LOG_ERROR);
        }
    }
}
