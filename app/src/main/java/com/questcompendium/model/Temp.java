package com.questcompendium.model;

public class Temp {
    private String dt;
    private String timezone;
    private String name;
    private Main main;
    private String cod;
    private String id;
    private String base;

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public class Main {
        private String temp;

        private String temp_min;

        private String grnd_level;

        private String humidity;

        private String pressure;

        private String sea_level;

        private String feels_like;

        private String temp_max;

        public String getTemp() {
            return temp;
        }

        public void setTemp(String temp) {
            this.temp = temp;
        }

        public String getTemp_min() {
            return temp_min;
        }

        public void setTemp_min(String temp_min) {
            this.temp_min = temp_min;
        }

        public String getGrnd_level() {
            return grnd_level;
        }

        public void setGrnd_level(String grnd_level) {
            this.grnd_level = grnd_level;
        }

        public String getHumidity() {
            return humidity;
        }

        public void setHumidity(String humidity) {
            this.humidity = humidity;
        }

        public String getPressure() {
            return pressure;
        }

        public void setPressure(String pressure) {
            this.pressure = pressure;
        }

        public String getSea_level() {
            return sea_level;
        }

        public void setSea_level(String sea_level) {
            this.sea_level = sea_level;
        }

        public String getFeels_like() {
            return feels_like;
        }

        public void setFeels_like(String feels_like) {
            this.feels_like = feels_like;
        }

        public String getTemp_max() {
            return temp_max;
        }

        public void setTemp_max(String temp_max) {
            this.temp_max = temp_max;
        }

        @Override
        public String toString() {
            return "ClassPojo [temp = " + temp + ", temp_min = " + temp_min + ", grnd_level = " + grnd_level + ", humidity = " + humidity + ", pressure = " + pressure + ", sea_level = " + sea_level + ", feels_like = " + feels_like + ", temp_max = " + temp_max + "]";
        }
    }

}
