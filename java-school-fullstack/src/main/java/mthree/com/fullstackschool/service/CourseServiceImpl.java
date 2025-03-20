package mthree.com.fullstackschool.service;

import mthree.com.fullstackschool.dao.CourseDao;
import mthree.com.fullstackschool.model.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.nio.channels.ScatteringByteChannel;
import java.util.List;

@Service
public class CourseServiceImpl implements CourseServiceInterface {

    //YOUR CODE STARTS HERE

    private CourseDao courseDao;

    public CourseServiceImpl(CourseDao courseDao){
        this.courseDao = courseDao;
    }

    //YOUR CODE ENDS HERE

    public List<Course> getAllCourses() {
        //YOUR CODE STARTS HERE

        return courseDao.getAllCourses();

        //YOUR CODE ENDS HERE
    }

    public Course getCourseById(int id) {
        //YOUR CODE STARTS HERE

        try{
            return courseDao.findCourseById(id);
        } catch (DataAccessException e) {
            Course courseNotFound = new Course();
            courseNotFound.setCourseName("Course Not Found");
            courseNotFound.setCourseDesc("Course Not Found");
            return courseNotFound;
        }

        //YOUR CODE ENDS HERE
    }

    public Course addNewCourse(Course course) {
        //YOUR CODE STARTS HERE

        boolean isInvalid = false;
        if (course.getCourseName().isBlank()){
            course.setCourseName("Name blank, course NOT added");
            isInvalid = true;
        }
        if (course.getCourseDesc().isBlank()){
            course.setCourseDesc("Description blank, course NOT added");
            isInvalid = true;
        }

        return  isInvalid ? course : courseDao.createNewCourse(course);

        //YOUR CODE ENDS HERE
    }

    public Course updateCourseData(int id, Course course) {
        //YOUR CODE STARTS HERE

        if (id != course.getCourseId()){
            course.setCourseName("IDs do not match, course not updated");
            course.setCourseDesc("IDs do not match, course not updated");
        } else {
            courseDao.updateCourse(course);
        }

        return course;
        //YOUR CODE ENDS HERE
    }

    public void deleteCourseById(int id) {
        //YOUR CODE STARTS HERE

        //first delete the entries in the course_student table
        courseDao.deleteAllStudentsFromCourse(id);

        //then delete the course itself
        courseDao.deleteCourse(id);

        System.out.println( "Course ID: " + id + " deleted");

        //YOUR CODE ENDS HERE
    }
}
