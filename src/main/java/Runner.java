import entity.Comment;
import entity.User;
import entity.Video;
import lombok.Cleanup;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;

import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class Runner {
    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put("hibernate.connection.url", "jdbc:postgresql://localhost:5432/youtube");
        properties.put("hibernate.connection.username", "postgres");
        properties.put("hibernate.connection.password", "123");
        properties.put("hibernate.connection.driver_class", "org.postgresql.Driver");
        properties.put("hibernate.hbm2ddl.auto", "create");
        properties.put(Environment.SHOW_SQL, true);
        properties.put(Environment.FORMAT_SQL, true);

        @Cleanup SessionFactory sessionFactory = new Configuration().addProperties(properties)
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(Video.class)
                .addAnnotatedClass(Comment.class)
                .buildSessionFactory();

        @Cleanup Session session = sessionFactory.openSession();
        createUser(session, "john");
        createUser(session, "rick");
        addVideo(session, "Мое первое интервью", "бла-бла-бла", "john");
        addVideo(session, "Мое второе интервью", "без бла-бла", "john");
        addComment(session, "Мой коммент :-)", 1, "rick");

        session.clear();

        User john = findUserByName(session, "john");
        String videoName = "Мое первое интервью";
        List<Comment> comments = john.getVideoList().stream()
                .filter(e -> videoName.equals(e.getName()))
                .limit(1)
                .flatMap(e -> e.getComments().stream())
                .collect(Collectors.toList());
        System.out.println(comments);
    }

    private static User findUserByName(Session session, String name) {

        User user = session.createQuery("from User where nickname = :paramName", User.class)
                .setParameter("paramName", name)
                .setMaxResults(1)
                .getSingleResultOrNull();
        if (user == null) {
            throw new RuntimeException("Пользователь с таким ником не обнаружен");
        }
        return user;
    }

    private static void createUser(Session session, String nickName) {
        session.beginTransaction();
        User user = new User(nickName);
        session.persist(user);
        session.getTransaction().commit();
    }

    private static void addVideo(Session session, String name, String description, String userName) {
        User owner = findUserByName(session, userName);
        session.beginTransaction();
        Video video = new Video(name, description, owner);
        session.persist(video);
        session.getTransaction().commit();
    }

    private static void addComment(Session session, String commentText, Integer videoId, String userName) {
        User user = findUserByName(session, userName);
        Video video = session.find(Video.class, videoId);
        if (video == null) {
            throw new RuntimeException("Видео отсутствует или удалено!");
        }
        session.beginTransaction();
        Comment comment = new Comment(commentText, video, user);
        session.persist(comment);
        session.getTransaction().commit();
    }
}
