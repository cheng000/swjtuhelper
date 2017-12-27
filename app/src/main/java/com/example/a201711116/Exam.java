package com.example.a201711116;

/**
 * Created by wangli on 17-12-20.
 */

public class Exam {

    private String ID, scheduleNumber, classID, className, classNum, credit, teacher;
    private String examTime, examPlace, examTeacher;
    private String englishName, chineseName;

    public Exam(String ID, String scheduleNumber, String classID, String className,
                String classNum, String credit, String teacher,
                String examTime, String examPlace, String examTeacher)
    {
        this.ID = ID; this.scheduleNumber = scheduleNumber;
        this.classID = classID; this.className = className; this.classNum = classNum;
        this.credit = credit; this.teacher = teacher;
        this.examTime = examTime; this.examPlace = examPlace;
        this.examTeacher = examTeacher;
        englishName = chineseName = "";
    }

    public String getID() { return ID; }
    public String getScheduleNumber() { return scheduleNumber; }
    public String getClassName() { return className; }
    public String getClassNum() { return classNum; }
    public String getCredit() { return credit; }
    public String getTeacher() { return teacher; }
    public String getExamTime() { return examTime; }
    public String getExamPlace() { return examPlace; }
    public String getExamTeacher() { return examTeacher; }
    public String getClassID() { return classID; }
    public String getEnglishName() { return englishName; }
    public String getChineseName() {return chineseName;}

    public void setID(String ID) { this.ID = ID; }
    public void setScheduleNumber(String scheduleNumber) { this.scheduleNumber = scheduleNumber; }
    public void setClassName(String className) { this.className = className; }
    public void setClassNum(String classNum) { this.classNum = classNum; }
    public void setCredit(String credit) { this.credit = credit; }
    public void setTeacher(String teacher) { this.teacher = teacher; }
    public void setExamTime(String examTime) { this.examTime = examTime; }
    public void setExamPlace(String examPlace) { this.examPlace = examPlace; }
    public void setExamTeacher(String examTeacher) { this.examTeacher = examTeacher; }
    public void setClassID(String classID) { this.classID = classID; }
    public void setEnglishName(String englishName) {this.englishName = englishName;}
    public void setChineseName(String chineseName) {this.chineseName = chineseName;}

    public String getS()
    {
        return chineseName;
    }
    public String getSMore(){
        return  "\n\n序号 : " + ID + "\n\n选课编号 : " + scheduleNumber + "\n\n课程代码 : " + classID + "\n\n课程名称 : " +
                chineseName + "\n\n教学班 : " + classNum + "\n\n学分 : " +credit + "\n\n授课教师 : " +teacher + "\n\n考试时间 : " +
                examTime + "\n\n考试地点 : " + examPlace + "\n\n监考老师 : " + examTeacher + "\n\n";
    }
}
