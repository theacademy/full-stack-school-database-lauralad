package mthree.com.fullstackschool.dao;

import mthree.com.fullstackschool.dao.mappers.CourseMapper;
import mthree.com.fullstackschool.model.Course;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class CourseDaoImpl implements CourseDao {

    private final JdbcTemplate jdbcTemplate;

    public CourseDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Course createNewCourse(Course course) {
        //YOUR CODE STARTS HERE

        //course id is auto-incremented, don't add it
        final String INSERT_COURSE =
                """
                INSERT INTO course(courseCode, courseDesc, teacherId)
                VALUES (?, ?, ?)
                """;
        jdbcTemplate.update(
                INSERT_COURSE,
                course.getCourseName(),
                course.getCourseDesc(),
                course.getTeacherId()
        );

        //retrieve the cid of the recently added course and update the object
        final String SELECT_RECENT_CID = """
                SELECT MAX(cid)
                FROM course
                """;
        int cid = jdbcTemplate.queryForObject(
                SELECT_RECENT_CID, Integer.class
        );
        course.setCourseId(cid);

        return course;

        //YOUR CODE ENDS HERE
    }

    @Override
    public List<Course> getAllCourses() {
        //YOUR CODE STARTS HERE

        final String SELECT_ALL_COURSES = "SELECT * FROM course";
        List<Course> courses = jdbcTemplate.query(SELECT_ALL_COURSES, new CourseMapper());

        return courses;

        //YOUR CODE ENDS HERE
    }

    @Override
    public Course findCourseById(int id) {
        //YOUR CODE STARTS HERE

        final String SELECT_COURSE_ID =
                """
                SELECT * 
                FROM course
                WHERE cid = ?
                """;

        Course course = jdbcTemplate.queryForObject(
                SELECT_COURSE_ID,
                new CourseMapper(),
                id
        );

        return course;

        //YOUR CODE ENDS HERE
    }

    @Override
    public void updateCourse(Course course) {
        //YOUR CODE STARTS HERE

        final String UPDATE_COURSE = """
                UPDATE course SET
                courseCode = ?,
                courseDesc = ?,
                teacherId = ?
                WHERE cid = ?
                """;
        jdbcTemplate.update(
                UPDATE_COURSE,
                course.getCourseName(),
                course.getCourseDesc(),
                course.getTeacherId(),
                course.getCourseId()
        );

        //YOUR CODE ENDS HERE
    }

    @Override
    public void deleteCourse(int id) {
        //YOUR CODE STARTS HERE

        final String DELETE_COURSE_ID = """
                DELETE FROM course 
                WHERE cid = ?
                """;

        jdbcTemplate.update(DELETE_COURSE_ID, id);
        //YOUR CODE ENDS HERE
    }

    @Override
    public void deleteAllStudentsFromCourse(int courseId) {
        //YOUR CODE STARTS HERE

        final String DELETE_COURSE_STUDENT = """
                DELETE FROM course_student
                WHERE course_id = ?
                """;
        jdbcTemplate.update(DELETE_COURSE_STUDENT, courseId);

        //YOUR CODE ENDS HERE
    }
}
