package com.org.rivermanage.utils;

import java.util.List;

/**
 * Created by zhiqiang.com on 2017/10/22.
 */

public class Warning {


    /**
     * data : [{"audio":"2","createTime":"2017-11-12T00:00:00","description":"2","file":"2","id":2,"latitude":37.411336,"longitude":118.165059,"name":"jiang","shipin":null,"state":2,"uploader":"2","video":"2","yinpin":null,"zhaopian":null},{"audio":"1","createTime":"2019-11-11T00:00:00","description":"1","file":"1","id":3,"latitude":118.141488,"longitude":118.141488,"name":"yan","shipin":null,"state":1,"uploader":"1","video":"1","yinpin":null,"zhaopian":null},{"audio":"3","createTime":"2018-11-07T00:00:00","description":"3","file":"3","id":4,"latitude":37.415692,"longitude":118.086296,"name":"lu","shipin":null,"state":1,"uploader":"3","video":"3","yinpin":null,"zhaopian":null},{"audio":"44","createTime":"2001-01-03T00:00:00","description":"4","file":"4","id":5,"latitude":37.379799,"longitude":118.015006,"name":"chi","shipin":null,"state":2,"uploader":"4","video":"4","yinpin":null,"zhaopian":null},{"audio":"5","createTime":"2021-12-03T00:00:00","description":"5","file":"5","id":6,"latitude":37.370049,"longitude":118.065024,"name":"pi","shipin":null,"state":2,"uploader":"5","video":"55","yinpin":null,"zhaopian":null},{"audio":null,"createTime":"2017-10-22T18:53:11","description":"zhuanghuaile","file":null,"id":7,"latitude":39.922705,"longitude":116.416637,"name":"jingg","shipin":null,"state":1,"uploader":"phone1","video":null,"yinpin":null,"zhaopian":null},{"audio":null,"createTime":"2017-10-22T19:14:07","description":"????","file":null,"id":8,"latitude":39.922704,"longitude":116.416637,"name":"??2","shipin":null,"state":1,"uploader":"phone1","video":null,"yinpin":null,"zhaopian":null}]
     * message : null
     * success : true
     */

    private Object message;
    private boolean success;
    private List<DataBean> data;

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * audio : 2
         * createTime : 2017-11-12T00:00:00
         * description : 2
         * file : 2
         * id : 2
         * latitude : 37.411336
         * longitude : 118.165059
         * name : jiang
         * shipin : null
         * state : 2
         * uploader : 2
         * video : 2
         * yinpin : null
         * zhaopian : null
         */

        private String audio;
        private String createTime;
        private String description;
        private String file;
        private int id;
        private double latitude;
        private double longitude;
        private String name;
        private Object shipin;
        private int state;
        private String uploader;
        private String video;
        private Object yinpin;
        private Object zhaopian;

        public String getAudio() {
            return audio;
        }

        public void setAudio(String audio) {
            this.audio = audio;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getFile() {
            return file;
        }

        public void setFile(String file) {
            this.file = file;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Object getShipin() {
            return shipin;
        }

        public void setShipin(Object shipin) {
            this.shipin = shipin;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public String getUploader() {
            return uploader;
        }

        public void setUploader(String uploader) {
            this.uploader = uploader;
        }

        public String getVideo() {
            return video;
        }

        public void setVideo(String video) {
            this.video = video;
        }

        public Object getYinpin() {
            return yinpin;
        }

        public void setYinpin(Object yinpin) {
            this.yinpin = yinpin;
        }

        public Object getZhaopian() {
            return zhaopian;
        }

        public void setZhaopian(Object zhaopian) {
            this.zhaopian = zhaopian;
        }
    }
}
