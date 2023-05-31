package com.questcompendium.model;

public class Hotel {

    private HotelList[] foHotelList;

    private String fbIsSuccess;

    public HotelList[] getFoHotelList() {
        return foHotelList;
    }

    public void setFoHotelList(HotelList[] foHotelList) {
        this.foHotelList = foHotelList;
    }

    public String getFbIsSuccess() {
        return fbIsSuccess;
    }

    public void setFbIsSuccess(String fbIsSuccess) {
        this.fbIsSuccess = fbIsSuccess;
    }

    @Override
    public String toString() {
        return "ClassPojo [foHotelList = " + foHotelList + ", fbIsSuccess = " + fbIsSuccess + "]";
    }


    public static class HotelList {
        public HotelList(String fiHotelId, String fsHotelName, String fdLong, String fdLat,
                         String fsAddress, String fsDistance, String fsImage) {
            this.fiHotelsId = fiHotelId;
            this.fsHotelName = fsHotelName;
            this.fdLong = fdLong;
            this.fdLat = fdLat;
            this.fsAddress = fsAddress;
            this.fsDistance = fsDistance;
            this.fsImage = fsImage;
        }

        private String fsAppBackgroundImage;

        private String fiHotelsId;

        private String fsHotelName;

        private String fdLong;

        private String fdLat;

        private String fsAddress;

        private String fsDistance;

        private String fsImage;
        private String fiLatitude;
        private String fiLongitude;

        public String getFiLongitude() {
            return fiLongitude;
        }

        public void setFiLongitude(String fiLongitude) {
            this.fiLongitude = fiLongitude;
        }

        public String getFiLatitude() {
            return fiLatitude;
        }

        public void setFiLatitude(String fiLatitude) {
            this.fiLatitude = fiLatitude;
        }

        public String getFsAppBackgroundImage() {
            return fsAppBackgroundImage;
        }

        public void setFsAppBackgroundImage(String fsAppBackgroundImage) {
            this.fsAppBackgroundImage = fsAppBackgroundImage;
        }

        public String getFiHotelsId() {
            return fiHotelsId;
        }

        public void setFiHotelsId(String fiHotelsId) {
            this.fiHotelsId = fiHotelsId;
        }

        public String getFiHotelId() {
            return fiHotelsId;
        }

        public void setFiHotelId(String fiHotelId) {
            this.fiHotelsId = fiHotelId;
        }

        public String getFsHotelName() {
            return fsHotelName;
        }

        public void setFsHotelName(String fsHotelName) {
            this.fsHotelName = fsHotelName;
        }

        public String getFdLong() {
            return fdLong;
        }

        public void setFdLong(String fdLong) {
            this.fdLong = fdLong;
        }

        public String getFdLat() {
            return fdLat;
        }

        public void setFdLat(String fdLat) {
            this.fdLat = fdLat;
        }

        public String getFsAddress() {
            return fsAddress;
        }

        public void setFsAddress(String fsAddress) {
            this.fsAddress = fsAddress;
        }

        public String getFsDistance() {
            return fsDistance;
        }

        public void setFsDistance(String fsDistance) {
            this.fsDistance = fsDistance;
        }

        public String getFsImage() {
            return fsImage;
        }

        public void setFsImage(String fsImage) {
            this.fsImage = fsImage;
        }

        @Override
        public String toString() {
            return "ClassPojo [fiHotelId = " + fiHotelsId + ", fsHotelName = " + fsHotelName + ", fdLong = " + fdLong + ", fdLat = " + fdLat + ", fsAddress = " + fsAddress + ", fsDistance = " + fsDistance + ", fsImage = " + fsImage + "]";
        }
    }

}

