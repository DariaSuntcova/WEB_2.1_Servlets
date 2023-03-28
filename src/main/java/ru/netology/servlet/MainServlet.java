package ru.netology.servlet;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import ru.netology.controller.PostController;
import ru.netology.repository.PostRepository;
import ru.netology.service.PostService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MainServlet extends HttpServlet {
    private PostController controller;

    private static final String GET = "GET";
    private static final String POST = "POST";
    private static final String DELETE = "DELETE";
    private static final String PATH_API_POST = "/api/posts";
    private static final String SLASH = "/";

    @Override
    public void init() {
        final var factory = new DefaultListableBeanFactory();
        final var reader = new XmlBeanDefinitionReader(factory);
        reader.loadBeanDefinitions("beans.xml");

        // получаем по имени бина
        controller = (PostController) factory.getBean("postController");
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        // если деплоились в root context, то достаточно этого
        try {
            final var path = req.getRequestURI();
            final var method = req.getMethod();

            // primitive routing
            if (method.equals(GET) && path.equals(PATH_API_POST)) {
                controller.all(resp);
                return;
            }
            if (method.equals(GET) && path.matches(PATH_API_POST + "\\d+")) {
                // easy way
                controller.getById(parseID(path), resp);
                return;
            }
            if (method.equals(POST) && path.equals(PATH_API_POST)) {
                controller.save(req.getReader(), resp);
                return;
            }
            if (method.equals(DELETE) && path.matches(PATH_API_POST + "\\d+")) {
                // easy way
                controller.removeById(parseID(path), resp);
                return;
            }
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private long parseID(String path) {
        return Long.parseLong(path.substring(path.lastIndexOf(SLASH)));
    }
}

