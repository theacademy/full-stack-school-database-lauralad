package mthree.com.fullstackschool.dao;

import mthree.com.fullstackschool.dao.mappers.StudentMapper;
import mthree.com.fullstackschool.dao.mappers.TeacherMapper;
import mthree.com.fullstackschool.model.Student;
import mthree.com.fullstackschool.model.Teacher;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class TeacherDaoImpl implements TeacherDao {

    private final JdbcTemplate jdbcTemplate;

    public TeacherDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Teacher createNewTeacher(Teacher teacher) {
        //YOUR CODE STARTS HERE

        final String INSERT_TEACHER = """
                INSERT INTO teacher(tFName, tLName, dept)
                VALUES (?, ?, ?)
                """;
        jdbcTemplate.update(
                INSERT_TEACHER,
                teacher.getTeacherFName(),
                teacher.getTeacherLName(),
                teacher.getDept()
        );

        //retrieve the created id and set it to the teacher obj
        final String SELECT_RECENT_TID = """
                SELECT MAX(tid)
                FROM teacher
                """;
        int tid = jdbcTemplate.queryForObject(
                SELECT_RECENT_TID, Integer.class
        );

        teacher.setTeacherId(tid);
        return teacher;

        //YOUR CODE ENDS HERE
    }

    @Override
    public List<Teacher> getAllTeachers() {
        //YOUR CODE STARTS HERE

        final String SELECT_ALL_TEACHERS = "SELECT * FROM teacher";
        List<Teacher> teachers = jdbcTemplate.query(SELECT_ALL_TEACHERS, new TeacherMapper());

        return teachers;


        //YOUR CODE ENDS HERE
    }

    @Override
    public Teacher findTeacherById(int id) {
        //YOUR CODE STARTS HERE

        final String SELECT_TEACHER_ID = """
                SELECT *
                FROM teacher
                WHERE tid = ?
                """;
        Teacher teacher = jdbcTemplate.queryForObject(
                SELECT_TEACHER_ID,
                new TeacherMapper(),
                id
        );
        return teacher;

        //YOUR CODE ENDS HERE
    }

    @Override
    public void updateTeacher(Teacher t) {
        //YOUR CODE STARTS HERE

        final String UPDATE_TEACHER = """
                UPDATE teacher SET
                tFName = ?,
                tLName = ?,
                dept = ?
                WHERE tid = ?
                """;
        jdbcTemplate.update(
                UPDATE_TEACHER,
                t.getTeacherFName(),
                t.getTeacherLName(),
                t.getDept(),
                t.getTeacherId()
        );

        //YOUR CODE ENDS HERE
    }

    @Override
    public void deleteTeacher(int id) {
        //YOUR CODE STARTS HERE

        //remove the teacherId from any courses that have teacherId=id
        final String UPDATE_COURSE_NO_TEACHER = """
                UPDATE course 
                SET teacherId = NULL
                WHERE teacherId = ?
                """;
        jdbcTemplate.update(UPDATE_COURSE_NO_TEACHER, id);

        //remove the teacher
        final String DELETE_TEACHER = "DELETE FROM teacher WHERE tid = ?";
        jdbcTemplate.update(DELETE_TEACHER, id);

        //YOUR CODE ENDS HERE
    }
}
