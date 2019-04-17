package com.pojo;

import java.util.List;

public class CameraWarnFaceInformation {

    public int type;
    public String cid;
    public long time;
    public int upload_id;
    public int track_id;
    public List<String> faces;
    public String image;
    public int front_value;
    public int sharpen_ness;
    public float scale_out_y;
    public float scale_out_x;
    public int upload_face_size;
    public List<String> position;
    public int person_type;
    public String person_id;
    public String person_name;
    public String sex;
    public int age;
    public float confidence;

    @Override
    public String toString() {
        return "CameraWarnFaceInformation{" +
                "type=" + type +
                ", cid='" + cid + '\'' +
                ", time=" + time +
                ", upload_id=" + upload_id +
                ", track_id=" + track_id +
                ", faces=" + faces +
                ", image=" + image +
                ", front_value=" + front_value +
                ", sharpen_ness=" + sharpen_ness +
                ", scale_out_y=" + scale_out_y +
                ", scale_out_x=" + scale_out_x +
                ", upload_face_size=" + upload_face_size +
                ", position=" + position +
                ", person_type=" + person_type +
                ", person_id='" + person_id + '\'' +
                ", person_name='" + person_name + '\'' +
                ", sex='" + sex + '\'' +
                ", age=" + age +
                ", confidence=" + confidence +
                '}';
    }

}
