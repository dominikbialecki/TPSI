package model;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity
public class LastId {

    public LastId(){}
    public LastId(int studentIndex, int gradeId) {
        this.studentIndex = studentIndex;
        this.gradeId = gradeId;
    }

    @Id
    private ObjectId id;

    public ObjectId getId() {
        return id;
    }
    public void setId(ObjectId id) {
        this.id = id;
    }

    private Integer studentIndex;
    public Integer getStudentIndex() {
        return studentIndex;
    }
    public void setStudentIndex(Integer studentIndex) {
        this.studentIndex = studentIndex;
    }

    private Integer gradeId;
    public Integer getGradeId() {
        return gradeId;
    }
    public void setGradeId(Integer gradeId) {
        this.gradeId = gradeId;
    }
}
