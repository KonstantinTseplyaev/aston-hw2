package com.aston.hw2.servlet;

import com.aston.hw2.model.dto.EventCreationDto;
import com.aston.hw2.model.dto.EventDto;
import com.aston.hw2.model.dto.EventForUpdate;
import com.aston.hw2.model.dto.EventFullDto;
import com.aston.hw2.model.dto.LocationForUpdate;
import com.aston.hw2.service.EventService;
import com.aston.hw2.util.JsonParserUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import static com.aston.hw2.util.ResponseAnswers.DEL_EVENT_ERROR;
import static com.aston.hw2.util.ResponseAnswers.DEL_EVENT_SUCCESS;
import static com.aston.hw2.util.ResponseAnswers.EVENT_ADDED;
import static com.aston.hw2.util.ResponseAnswers.EVENT_NOT_FOUND;
import static com.aston.hw2.util.ResponseAnswers.UPDATE_ERROR;

@Slf4j
@WebServlet("/events/*")
public class EventServlet extends HttpServlet {
    private final ObjectMapper jsonMapper;
    private EventService eventService;

    public EventServlet() {
        jsonMapper = new ObjectMapper();
        jsonMapper.registerModule(new JavaTimeModule());
    }

    @Override
    public void init() {
        this.eventService = (EventService) getServletContext().getAttribute("eventService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String responseBody;

        String id = req.getParameter("id");
        if (id == null) {
            resp.setContentType("application/json; charset=UTF-8");
            resp.setStatus(200);
            log.debug("get all events");
            List<EventDto> events = eventService.getAllEvents();
            responseBody = jsonMapper.writeValueAsString(events);
        } else {
            log.debug("find event with id {}", id);
            long eventId = Integer.parseInt(id);
            EventFullDto event = eventService.getEventById(eventId);
            if (event == null) {
                resp.setStatus(404);
                resp.setContentType("text/HTML");
                responseBody = EVENT_NOT_FOUND;
            } else {
                resp.setContentType("application/json; charset=UTF-8");
                resp.setStatus(200);
                responseBody = jsonMapper.writeValueAsString(event);
            }
        }
        PrintWriter writer = resp.getWriter();
        writer.write(responseBody);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        long userId = (long) session.getAttribute("userId");
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/HTML");

        EventCreationDto event = JsonParserUtil.toEventCreationDtoFromJson(req);
        event.setInitiatorId(userId);

        String respMessage = eventService.addNewEvent(event);

        if (respMessage.equals(EVENT_ADDED)) {
            resp.setStatus(201);
        } else {
            resp.setStatus(400);
        }
        PrintWriter writer = resp.getWriter();
        writer.write(respMessage);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String responseBody;
        HttpSession session = req.getSession();
        long userId = (long) session.getAttribute("userId");
        req.setCharacterEncoding("UTF-8");

        JSONObject jsonEvent = JsonParserUtil.getUpdateEventJson(req);
        log.debug("update event: {}", jsonEvent);

        LocationForUpdate locForUp = JsonParserUtil.toLocationForUpdateFromJson(jsonEvent, jsonMapper);
        EventForUpdate eventForUpdate = JsonParserUtil.toEventForUpdateFromJson(jsonEvent, jsonMapper);

        boolean isEventUpdated = eventService.updateEvent(eventForUpdate, locForUp, userId);

        if (isEventUpdated) {
            resp.setContentType("application/json; charset=UTF-8");
            EventFullDto event = eventService.getEventById(eventForUpdate.getId());
            responseBody = jsonMapper.writeValueAsString(event);
            resp.setStatus(201);
        } else {
            resp.setContentType(UPDATE_ERROR);
            responseBody = "update failed";
            resp.setStatus(500);
        }
        PrintWriter writer = resp.getWriter();
        writer.write(responseBody);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String responseBody;
        resp.setContentType("text/HTML");
        String id = req.getParameter("id");
        log.debug("delete event with id {}", id);
        HttpSession session = req.getSession();
        long userId = (long) session.getAttribute("userId");
        long eventId = Integer.parseInt(id);

        boolean isDeleted = eventService.deleteEventById(userId, eventId);

        if (isDeleted) {
            responseBody = DEL_EVENT_SUCCESS;
        } else {
            responseBody = DEL_EVENT_ERROR;
        }
        PrintWriter writer = resp.getWriter();
        writer.write(responseBody);
    }
}
