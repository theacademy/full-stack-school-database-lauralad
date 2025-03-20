package mthree.com.fullstackschool.dao;

import mthree.com.fullstackschool.dao.mappers.StudentMapper;
import mthree.com.fullstackschool.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import java.sql.*;
import java.util.List;
import java.util.Objects;

@Repository
public class StudentDaoImpl implements StudentDao {

    @Autowired
    private final JdbcTemplate jdbcTemplate;

    public StudentDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional
    public Student createNewStudent(Student student) {
        //YOUR CODE STARTS HERE

        final String INSERT_STUDENT = """
                INSERT INTO student(fName, lName)
                VALUES (?, ?)
                """;
        jdbcTemplate.update(
                INSERT_STUDENT,
                student.getStudentFirstName(),
                student.getStudentLastName()
        );

        //retrieve the created id and set it to the student obj
        final String SELECT_RECENT_SID = """
                SELECT MAX(sid)
                FROM student
                """;
        int sid = jdbcTemplate.queryForObject(
                SELECT_RECENT_SID, Integer.class
        );
        student.setStudentId(sid);

        return student;


        //YOUR CODE ENDS HERE
    }

    @Override
    public List<Student> getAllStudents() {
        //YOUR CODE STARTS HERE

        final String SELECT_ALL_STUDENTS = "SELECT * FROM student";
        List<Student> students = jdbcTemplate.query(SELECT_ALL_STUDENTS, new StudentMapper());

        return students;

        //YOUR CODE ENDS HERE
    }

    @Override
    public Student findStudentById(int id) {
        //YOUR CODE STARTS HERE

        final String SELECT_STUDENT_ID = "SELECT * FROM student WHERE sid = ?";
        Student student = jdbcTemplate.queryForObject(
                SELECT_STUDENT_ID,
                new StudentMapper(),
                id
        );

        return student;

        //YOUR CODE ENDS HERE
    }

    @Override
    public void updateStudent(Student student) {
        //YOUR CODE STARTS HERE

        final String UPDATE_STUDENT = """
                UPDATE student SET
                fName = ?,
                lName = ?
                WHERE sid = ?
                """;
        jdbcTemplate.update(
                UPDATE_STUDENT,
                student.getStudentFirstName(),
                student.getStudentLastName(),
                student.getStudentId()
        );


        //YOUR CODE ENDS HERE
    }

    @Override
    public void deleteStudent(int id) {
        //YOUR CODE STARTS HERE

        //delete any entries if they exist in the course_student table
        //for the student id (if they are enrolled in courses, they will be
        //deleted, else nothing happens)
        final String DELETE_COURSES_FOR_STUDENT = """
                DELETE FROM course_student
                WHERE student_id = ?
                """;
        jdbcTemplate.update(DELETE_COURSES_FOR_STUDENT, id);

        //finally delete the student
        final String DELETE_STUDENT_ID = "DELETE FROM student WHERE sid = ?";
        jdbcTemplate.update(DELETE_STUDENT_ID, id);

        //YOUR CODE ENDS HERE
    }

    @Override
    public void addStudentToCourse(int studentId, int courseId) {
        //YOUR CODE STARTS HERE

        final String INSERT_COURSE_STUDENT = """
                INSERT INTO course_student(student_id, course_id)
                VALUES (?, ?)
                """;
        jdbcTemplate.update(
                INSERT_COURSE_STUDENT,
                studentId,
                courseId
        );

        //YOUR CODE ENDS HERE
    }

    @Override
    public void deleteStudentFromCourse(int studentId, int courseId) {
        //YOUR CODE STARTS HERE

        final String DELETE_COURSE_STUDENT = """
                DELETE FROM course_student
                WHERE student_id = ? AND course_id = ?
                """;
        jdbcTemplate.update(
                DELETE_COURSE_STUDENT,
                studentId,
                courseId
        );

        //YOUR CODE ENDS HERE
    }
}
